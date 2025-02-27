package wacc

import wacc.TypedAST.Ident

import wacc.Size.*

import scala.collection.mutable
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{?, KnownType, SemType}

import java.rmi.UnexpectedException

type Location = Register | Pointer

class LocationContext {

  /** Convert a semantic type to a size
   *
   * @param t The semantic type to convert
   * @return The size of the semantic type
   */
  private def typeToSize(t: SemType): Size = t match {
    case IntType        => W32
    case BoolType       => W8
    case CharType       => W8
    case StringType     => W64
    case ArrayType(_)   => W64
    case PairType(_, _) => W64
    case _              => throw new UnexpectedException("Unexpected Error: Invalid type")
  }

  /** Free registers */
  private val freeRegs: mutable.ListBuffer[Size => Register] = mutable.ListBuffer(
    RAX.apply,
    RBX.apply,
    R10.apply,
    R11.apply,
    R12.apply,
    R13.apply,
    R14.apply,
    R15.apply,
    // last 6 registers are caller-saved registers, in order
    R9.apply,
    R8.apply,
    RCX.apply,
    RDX.apply,
    RSI.apply,
    RDI.apply
  )

  /** The size of a pointer in bytes */
  private val POINTER_SIZE = 8

  /** Reserved registers in order, i.e. tail is latest reservation */
  private val reservedRegs = mutable.ListBuffer[Size => Register]()

  /** Number of stack locations which have been reserved */
  private var reservedStackLocs = 0

  /** Map from identifiers to locations */
  private val identMap = mutable.Map[Ident, Location]()

  // Register constants
  private val ReturnReg = RAX(W64)
  private val StackPointer = RSP(W64)
  private val BasePointer = RBP(W64)
  private val ArgRegs = List(RDI(W64), RSI(W64), RDX(W64), RCX(W64), R8(W64), R9(W64))
  private val CalleeSaved = List(RBX(W64), R12(W64), R13(W64), R14(W64), R15(W64))
  private val CallerSaved = List(RAX(W64), RCX(W64), RDX(W64), RSI(W64), RDI(W64), R8(W64), R9(W64), R10(W64), R11(W64))

  /** Get the next location to use, without actually using it
   *
   * @param size The size of the location
   * @return The next location to use
   */
  def getNext(size: Size): Location =
    if (freeRegs.nonEmpty) freeRegs.head(size)
    else RegImmPointer(RBP(W64), reservedStackLocs * POINTER_SIZE)(size)

  /** Get the location to use and reserve it
   *
   * @param size The size of the location
   * @return The reserved location
   */
  def reserveNext(size: Size): Location = {
    if (freeRegs.nonEmpty) {
      val reg = freeRegs.remove(freeRegs.length - 1)
      reservedRegs += reg
      reg(size)
    } else {
      val loc = RegImmPointer(RBP(W64), reservedStackLocs * POINTER_SIZE)(size)
      reservedStackLocs += 1
      loc
    }
  }

  /** Move the getNext pointer to the last location */
  def unreserveLast(): Unit = {
    if (reservedStackLocs > 0) {
      reservedStackLocs -= 1
    } else {
      val reg = reservedRegs.remove(reservedRegs.length - 1)
      freeRegs += reg
    }
  }

  /** Reserve and associate the next free location with an identifier
   *
   * @param v The identifier to associate with
   * @param size The size of the location
   */
  def addLocation(v: Ident, size: Size): Unit = {
    val loc = reserveNext(size)
    identMap(v) = loc
  }

  /** Get the location associated with an identifier
   *
   * @param v The identifier to get the location of
   * @return The location associated with the identifier
   */
  def getLocation(v: Ident): Location = identMap(v)

  private def pushLocs(regs: List[Location])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs)
      instructionCtx.addInstruction(Push(r))
  }

  private def popLocs(regs: List[Location])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs.reverse)
      instructionCtx.addInstruction(Pop(r))
  }

  /** Set up stack frame, assign parameters a location and push callee-saved registers onto the stack.
   * Run this at the start of a function.
   * 
   * @param params The parameters of the function
  */
  def setUpFunc(params: List[Ident])(using instructionCtx: InstructionContext): Unit =
    // params are stored in caller-saved registers in order, then on the stack

    // 1. push base pointer
    instructionCtx.addInstruction(Push(BasePointer))

    // 2. decrement stack pointer by number of callee-saved registers
    instructionCtx.addInstruction(Sub(StackPointer, CalleeSaved.length * POINTER_SIZE))

    // 3. save callee-saved registers
    for (reg <- CalleeSaved)
      instructionCtx.addInstruction(Push(reg))

    // 4. set up base pointer
    instructionCtx.addInstruction(Mov(BasePointer, StackPointer))

    // 5. assign parameters a location

    // For the first 6 parameters, assign them to the first 6 registers manually, removing them from freeRegs
    params
      .take(ArgRegs.length)
      .foreach(id => {
        val reg = freeRegs.remove(freeRegs.length - 1)(typeToSize(id.getType))
        identMap(id) = reg
      })

    // For the rest, move them into our stack frame, incrementing reservedStackLocs, using rax as a temporary register
    params
      .drop(ArgRegs.length)
      .zipWithIndex
      .foreach((id, index) => {
        val destLoc = RegImmPointer(RBP(W64), reservedStackLocs * POINTER_SIZE)(typeToSize(id.getType))

        // The parameter is at rbp - 8 * (index + CalleeSaved.length + 2)
        // This is because there's the return address, old base pointer, and callee-saved registers on the stack above
        // the current base pointer
        val currLoc = RegImmPointer(RBP(W64), -8 * (index + CalleeSaved.length + 2))(W64)
        instructionCtx.addInstruction(Mov(RAX(W64), currLoc))
        instructionCtx.addInstruction(Mov(destLoc, RAX(W64)))
        reservedStackLocs += 1
        identMap(id) = destLoc
      })

  /** Reset stack pointer and pop callee-saved registers from the stack, and set up the return value.
   * Run this at the end of a function just before returning.
   * 
   * @param retVal The location of the return value
   */
  def cleanUpFunc(retVal: Location)(using instructionCtx: InstructionContext): Unit = {
    // 1. set return value
    instructionCtx.addInstruction(Mov(ReturnReg, retVal))

    // 2. reset the stack pointer
    instructionCtx.addInstruction(Mov(StackPointer, BasePointer))

    // 3. pop callee-saved registers
    for (reg <- CalleeSaved.reverse)
      instructionCtx.addInstruction(Pop(reg))

    // 4. pop base pointer
    instructionCtx.addInstruction(Pop(BasePointer))

    // 5. return from function
    instructionCtx.addInstruction(Ret(None))
  }

  /** Saves caller registers and moves arguments to their intended registers/on the stack.
   * Run this just before calling a function.
   *
   * @param argLocations The temporary locations of the arguments
   */
  def setUpCall(argLocations: List[Location])(using instructionCtx: InstructionContext): Unit = {
//    pushLocs(CallerSaved)
//    for ((argLoc, argReg) <- argLocations.take(ArgRegs.length).zip(ArgRegs)) {
//      argLoc match {
//        case r: Register => instructionCtx.addInstruction(Mov(argReg, r))
//        case p: Pointer  => instructionCtx.addInstruction(Mov(argReg, p))
//      }
//    }
//    pushLocs(argLocations.drop(ArgRegs.length))
//    basePointerOffset -= (argLocations.length - ArgRegs.length).max(0) * 64
  }

  /** Restore caller registers and save result to a location
   * Run this just after calling a function.
   *
   * @return The location of the result
   */
  def cleanUpCall()(using instructionCtx: InstructionContext): Location =
    popLocs(CallerSaved)
    RAX(W64)

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
   * @param op The operation to perform on the locations as registers
  */
  def regInstrN[A](locs: List[Location], op: List[Register] => Instruction)(using
      instructionCtx: InstructionContext
  ): Unit = ???
//    val sizeDiff = locs.length - freeRegs.length
//    assert(sizeDiff - reservedRegs.length >= 0)
//    val regPushed = reservedRegs.take(sizeDiff.max(0)).toList
//    pushLocs(regPushed)
//    op(freeRegs.toList.map(_(W64)) ++ regPushed)
//    popLocs(regPushed)

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
}
