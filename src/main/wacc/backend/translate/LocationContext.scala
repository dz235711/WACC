package wacc

import wacc.TypedAST.{Call as TypedCall, *}
import scala.collection.mutable.{ListBuffer, Map as MMap}
import wacc.Size.*

type Location = Register | Pointer

class LocationContext {

  // Free registers
  private val freeRegs: ListBuffer[Size => Register] = ListBuffer(
    RAX.apply,
    RBX.apply,
    RCX.apply,
    RDX.apply,
    RSI.apply,
    RDI.apply,
    R8.apply,
    R9.apply,
    R10.apply,
    R11.apply,
    R12.apply,
    R13.apply,
    R14.apply,
    R15.apply
  )

  // Reserved locations in reverse order, i.e. head is latest reservation
  private val reserved = ListBuffer[Location]()

  /* Reserved registers in order, i.e. tail is latest reservation, this is to ensure oldest registers are temporarily
   * push/popped first to minimise stack usage */
  private val reservedRegs = ListBuffer[Register]()

  // Offset from the base pointer, i.e. the stack pointer
  var basePointerOffset = 0

  // Map from identifiers to locations
  private val identMap = MMap[Ident, Location]()

  // Register constants
  private val ArgRegs = List(RDI(W64), RSI(W64), RDX(W64), RCX(W64), R8(W64), R9(W64))
  private val CalleeSaved = List(RBX(W64), RBP(W64), R12(W64), R13(W64), R14(W64), R15(W64))
  private val CallerSaved = List(RAX(W64), RCX(W64), RDX(W64), RSI(W64), RDI(W64), R8(W64), R9(W64), R10(W64), R11(W64))

  /** Get the next location to use, without actually using it
   *
   * @param size The size of the location
   * @return The next location to use
   */
  def getNext(size: Size): Location = if (freeRegs.nonEmpty) freeRegs.head(size)
  else RegImmPointer(RBP(W64), basePointerOffset)(size)

  private def sizeToInt(size: Size): Int = size match {
    case W8  => 1
    case W16 => 2
    case W32 => 4
    case W64 => 8
  }

  /** Get the location to use and reserve it
   *
   * @param size The size of the location
   * @return The reserved location
   */
  def reserveNext(size: Size): Location = {
    val loc = getNext(size)
    loc +=: reserved
    loc match {
      case r: Register => reservedRegs += r
      case p: Pointer  => basePointerOffset -= sizeToInt(p.size)
    }
    loc
  }

  /** Move the getNext pointer to the last location */
  def unreserveLast(): Unit = {
    assert(reserved.nonEmpty)
    val loc = reserved.remove(0)
    loc match {
      case r: Register =>
        freeRegs += constructorFromInstance(r)
        reservedRegs.remove(reservedRegs.length - 1)
      case p: Pointer => basePointerOffset += sizeToInt(p.size)
    }
  }

  private def constructorFromInstance(r: Register): (Size => Register) = r match {
    case RAX(_) => RAX.apply
    case RBX(_) => RBX.apply
    case RCX(_) => RCX.apply
    case RDX(_) => RDX.apply
    case RSI(_) => RSI.apply
    case RDI(_) => RDI.apply
    case R8(_)  => R8.apply
    case R9(_)  => R9.apply
    case R10(_) => R10.apply
    case R11(_) => R11.apply
    case R12(_) => R12.apply
    case R13(_) => R13.apply
    case R14(_) => R14.apply
    case R15(_) => R15.apply
    case _      => throw new IllegalArgumentException("This register shouldn't be used in location context")
  }

  /** Associate a location with an identifier, assuming the location has already been reserved
   *
   * @param v The identifier to associate with
   * @param r The location to associate
   */
  def addLocation(v: Ident, r: Location): Unit = {
    identMap(v) = r
  }

  /** Get the location associated with an identifier
   *
   * @param v The identifier to get the location of
   * @return The location associated with the identifier
   */
  def getLocation(v: Ident): Location = identMap(v)

  private def pushLocs(regs: List[Location])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs) {
      instructionCtx.addInstruction(Push(r))
    }
  }

  private def popLocs(regs: List[Location])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs.reverse) {
      instructionCtx.addInstruction(Pop(r))
    }
  }

  /** Pop callee-saved registers from the stack */
  def restoreCalleeRegisters()(using instructionCtx: InstructionContext): Unit = popLocs(CalleeSaved)

  /** Move a value from one location to another
   *
   * @param dest The destination location
   * @param src The source location
   */
  def movLocLoc(dest: Location, src: Location): Unit = ???

  /** Perform some operation that forces the use of a register.
   * This is useful for operations that require a register as an operand, but you only have 2 locations.
   *
   * @param loc1 The first location
   * @param loc2 The second location/operand
   * @param op The operation to perform on the two locations, where the first location is guaranteed to be a register
   */
  def regInstr[Op2](loc1: Location, loc2: Op2, op: (Register, Op2) => Instruction): Unit = ???

  /** Perform some operation that forces the use of n registers. 
   * 
   * @param locs The locations to use
   * @param data The helper data to use
   * @param op The operation to perform on the locations
  */
  def regInstrN[A](locs: List[Location], data: A, op: (List[Register], A) => Instruction)(using
      instructionCtx: InstructionContext
  ): Unit =
    val sizeDiff = locs.length - freeRegs.length
    assert(sizeDiff - reservedRegs.length >= 0)
    val regPushed = reservedRegs.take(sizeDiff.max(0)).toList
    pushLocs(regPushed)
    op(freeRegs.toList.map(_(W64)) ++ regPushed, data)
    popLocs(regPushed)

  /** Perform some operation that forces the use of some register(s). These registers are saved and restored after the operation.
   *
   * @param regsToUse The registers modified by the operation
   * @param op The operation to perform
   */
  def withFreeRegisters(regsToUse: List[Register], op: => Unit)(using instructionCtx: InstructionContext): Unit = {
    pushLocs(regsToUse)
    op
    popLocs(regsToUse)
  }

  /** Saves caller registers and moves arguments to their intended registers/on the stack
    * 
    * @param argLocations The temporary locations of the arguments
    */
  def setUpCall(argLocations: List[Location])(using instructionCtx: InstructionContext): Unit = {
    pushLocs(CallerSaved)
    for ((argLoc, argReg) <- argLocations.take(ArgRegs.length).zip(ArgRegs)) {
      argLoc match {
        case r: Register => instructionCtx.addInstruction(Mov(argReg, r))
        case p: Pointer  => instructionCtx.addInstruction(Mov(argReg, p))
      }
    }
    pushLocs(argLocations.drop(ArgRegs.length))
  }

  /** Restore caller registers and save result to a location
    * 
    * @return The location of the result
    */
  def cleanUpCall()(using instructionCtx: InstructionContext): Location =
    popLocs(CallerSaved)
    RAX(W64)
}
