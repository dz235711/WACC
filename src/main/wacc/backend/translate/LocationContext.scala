package wacc

import wacc.TypedAST.Ident

import wacc.Size.*

import scala.collection.mutable
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{KnownType, SemType}

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
    RBX.apply,
    RCX.apply,
    RDI.apply,
    RSI.apply,
    R8.apply,
    R9.apply,
    R10.apply,
    R11.apply,
    R12.apply,
    R13.apply,
    R14.apply,
    R15.apply
  )

  /** The size of a pointer in bytes */
  private val PointerSize = 8

  /** Reserved registers in order, i.e. tail is latest reservation */
  private val reservedRegs = mutable.ListBuffer[Size => Register]()

  /** Number of stack locations which have been reserved */
  private var reservedStackLocs = 0

  /** Map from identifiers to locations */
  private val identMap = mutable.Map[Ident, Location]()

  // Register constants
  private val ReturnReg = RAX.apply
  private val EmptyRegs = List(RAX.apply, RDX.apply) // never used as a location
  private val StackPointer = RSP(W64)
  private val BasePointer = RBP(W64)
  private val ArgRegs = List(RDI.apply, RSI.apply, RDX.apply, RCX.apply, R8.apply, R9.apply)
  private val CalleeSaved = List(RBX.apply, R12.apply, R13.apply, R14.apply, R15.apply)
  private val CallerSaved = List(RCX.apply, RSI.apply, RDI.apply, R8.apply, R9.apply, R10.apply, R11.apply)

  /** Get the next location to use, without actually using it
   *
   * @param size The size of the location
   * @return The next location to use
   */
  def getNext(size: Size): Location =
    if (freeRegs.nonEmpty) freeRegs.last(size)
    else RegImmPointer(BasePointer, reservedStackLocs * PointerSize)(size)

  /** Get the location to use and reserve it
   *
   * @param size The size of the location
   * @return The reserved location
   */
  def reserveNext(size: Size)(using instructionCtx: InstructionContext): Location = {
    if (freeRegs.nonEmpty) {
      val reg = freeRegs.remove(freeRegs.length - 1)
      reservedRegs += reg
      reg(size)
    } else {
      val loc = RegImmPointer(BasePointer, reservedStackLocs * PointerSize)(size)
      reservedStackLocs += 1
      // decrement the stack pointer
      instructionCtx.addInstruction(Sub(StackPointer, PointerSize))
      loc
    }
  }

  /** Move the getNext pointer to the last location */
  def unreserveLast()(using instructionCtx: InstructionContext): Unit = {
    if (reservedStackLocs > 0) {
      reservedStackLocs -= 1
      // increment the stack pointer
      instructionCtx.addInstruction(Add(StackPointer, PointerSize))
    } else {
      val reg = reservedRegs.remove(reservedRegs.length - 1)
      freeRegs += reg
    }
  }

  /** Reserve and associate the next free location with an identifier
   *
   * @param v    The identifier to associate with
   * @param size The size of the location
   */
  def addLocation(v: Ident, size: Size)(using instruction: InstructionContext): Unit = {
    val loc = reserveNext(size)
    identMap(v) = loc
  }

  /** Get the location associated with an identifier
   *
   * @param v The identifier to get the location of
   * @return The location associated with the identifier
   */
  def getLocation(v: Ident): Location = identMap(v)

  private def pushLocs(regs: List[Size => Register])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs)
      instructionCtx.addInstruction(Push(r(W64)))
  }

  private def popLocs(regs: List[Size => Register])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs.reverse)
      instructionCtx.addInstruction(Pop(r(W64)))
  }

  /** Set up stack frame, assign parameters a location and push callee-saved registers onto the stack.
   *
   * @note Run this at the start of a function.
   * @param params The parameters of the function
   */
  def setUpFunc(params: List[Ident])(using instructionCtx: InstructionContext): Unit =
    instructionCtx.addInstruction(Comment("Setting up function"))

    // 1. push base pointer
    instructionCtx.addInstruction(Push(BasePointer))

    // 2. save callee-saved registers
    pushLocs(CalleeSaved)

    // 3. set up base pointer
    instructionCtx.addInstruction(Mov(BasePointer, StackPointer))

    // 4. assign parameters a location

    // For the first 6 parameters, assign them to the first 6 argument registers manually, removing them from freeRegs
    params
      .zip(ArgRegs)
      .foreach((id, reg) => {
        reg match {
          case rdx: RDX =>
            // rdx is used for division, so we need to keep it free
            // move it to the first caller-saved register instead
            instructionCtx.addInstruction(Mov(CallerSaved.head(W64), rdx))

            freeRegs -= CallerSaved.head
            identMap(id) = CallerSaved.head(typeToSize(id.getType))
          case r =>
            // move the parameter to the register
            freeRegs -= r
            identMap(id) = r(typeToSize(id.getType))
        }
      })

    // For the remaining parameters, assign them to the next available locations
    params
      .drop(ArgRegs.length)
      .zipWithIndex
      .foreach((id, index) => {
        val destLoc = getNext(typeToSize(id.getType))
        val currLoc = RegImmPointer(BasePointer, PointerSize * (index + CalleeSaved.length + 2))(W64)
        movLocLoc(destLoc, currLoc)
        addLocation(id, typeToSize(id.getType))
      })

    instructionCtx.addInstruction(Comment("Function set up complete"))

  /** Reset stack pointer and pop callee-saved registers from the stack, and set up the return value.
   *
   * @note Run this at the end of a function just before returning.
   * @param retVal The location of the return value
   */
  def cleanUpFunc(retVal: Location)(using instructionCtx: InstructionContext): Unit = {
    instructionCtx.addInstruction(Comment("Cleaning up function"))

    // 1. set return value
    instructionCtx.addInstruction(
      Mov(
        ReturnReg(retVal.size),
        retVal
      )
    )

    // 2. reset the stack pointer
    instructionCtx.addInstruction(Mov(StackPointer, BasePointer))

    // 3. pop callee-saved registers
    popLocs(CalleeSaved)

    // 4. pop base pointer
    instructionCtx.addInstruction(Pop(BasePointer))

    // 5. return from function
    instructionCtx.addInstruction(Ret(None))

    instructionCtx.addInstruction(Comment("Function clean up complete"))
  }

  /** Saves caller registers and moves arguments to their intended registers/on the stack.
   *
   * @note Run this just before calling a function.
   * @param argLocations The temporary locations of the arguments
   */
  def setUpCall(argLocations: List[Location])(using instructionCtx: InstructionContext): Unit = {
    instructionCtx.addInstruction(Comment("Setting up function call"))

    // 1. Save caller registers
    pushLocs(CallerSaved)

    // 2. Move first 6 arguments to their intended registers
    for ((argLoc, argReg) <- argLocations.zip(ArgRegs)) {
      argLoc match {
        case r: Register =>
          if (CallerSaved.contains(r))
            // If the location is a caller-saved register, it is now in the stack
            val newStackLoc =
              RegImmPointer(StackPointer, PointerSize * (CallerSaved.length - CallerSaved.indexOf(r)))(r.size)
            instructionCtx.addInstruction(Mov(argReg(r.size), newStackLoc))
          else instructionCtx.addInstruction(Mov(argReg(r.size), r))
        case p: Pointer => instructionCtx.addInstruction(Mov(argReg(p.size), p))
      }
    }

    // 3. Move remaining arguments to the stack
    for ((argLoc, index) <- argLocations.drop(ArgRegs.length).zipWithIndex) {
      argLoc match {
        case r: Register =>
          if (CallerSaved.contains(r))
            // If the location is a caller-saved register, it is now in the stack
            val newStackLoc =
              RegImmPointer(StackPointer, PointerSize * (CallerSaved.length + index - CallerSaved.indexOf(r)))(r.size)
            instructionCtx.addInstruction(Push(newStackLoc))
          else instructionCtx.addInstruction(Push(r))
        case p: Pointer => instructionCtx.addInstruction(Push(p))
      }
    }

    instructionCtx.addInstruction(Comment("Function call set up complete"))
  }

  /** Restore caller registers and save result to a location
   *
   * @note Run this just after calling a function.
   * @return The location of the result
   */
  def cleanUpCall(size: Option[Size])(using instructionCtx: InstructionContext): Location =
    instructionCtx.addInstruction(Comment("Cleaning up function call"))

    // 1. Restore caller registers
    popLocs(CallerSaved)

    instructionCtx.addInstruction(Comment("Function call clean up complete"))

    // 2. Return result location
    ReturnReg(size.getOrElse(W64))

  /** Move a value from one location to another
   *
   * @param dest The destination location
   * @param src  The source location
   */
  def movLocLoc(dest: Location, src: Location)(using instructionCtx: InstructionContext): Unit =
    dest match {
      case destR: Register => instructionCtx.addInstruction(Mov(destR, src))
      case destP: Pointer =>
        src match {
          case srcR: Register => instructionCtx.addInstruction(Mov(destP, srcR))
          case srcP: Pointer =>
            val emptyReg = EmptyRegs.head(srcP.size)
            instructionCtx.addInstruction(Mov(emptyReg, srcP))
            instructionCtx.addInstruction(Mov(destP, emptyReg))
        }
    }

  /** Perform some operation that forces the use of 1 register.
   * This is useful for operations that require a register as an operand, but you only have a location.
   *
   * @param loc1 The first location
   * @param op   The operation to perform on the two locations, where the first location is guaranteed to be a register
   */
  def regInstr1(loc1: Location, op: (Size => Register) => Instruction)(using instructionCtx: InstructionContext): Unit =
    val emptyReg = EmptyRegs.head
    val loc1Size = loc1.size

    // Move the location to a register, perform the operation, then move the result back
    instructionCtx.addInstruction(Mov(emptyReg(loc1Size), loc1))
    instructionCtx.addInstruction(op(emptyReg))
    instructionCtx.addInstruction(loc1 match {
      case r: Register => Mov(r, emptyReg(loc1Size))
      case p: Pointer  => Mov(p, emptyReg(loc1Size))
    })

  /** Perform some operation that forces the use of 2 registers.
   * This is useful for operations that require 2 registers as an operands, but you only have a location.
   *
   * @param loc1 The first location
   * @param loc2 The second location
   * @param op   The operation to perform on the locations as registers
   */
  def regInstr2(loc1: Location, loc2: Location, op: (Size => Register, Size => Register) => Instruction)(using
      instructionCtx: InstructionContext
  ): Unit =
    val emptyReg1 = EmptyRegs.last
    val emptyReg2 = EmptyRegs.last

    val loc1Size = loc1.size
    val loc2Size = loc2.size

    // Move the locations to registers, perform the operation, then move the results back
    instructionCtx.addInstruction(Mov(emptyReg1(loc1Size), loc1))
    instructionCtx.addInstruction(Mov(emptyReg2(loc2Size), loc2))
    instructionCtx.addInstruction(op(emptyReg1, emptyReg2))
    instructionCtx.addInstruction(loc1 match {
      case r: Register => Mov(r, emptyReg1(loc1Size))
      case p: Pointer  => Mov(p, emptyReg1(loc1Size))
    })
    instructionCtx.addInstruction(loc2 match {
      case r: Register => Mov(r, emptyReg2(loc2Size))
      case p: Pointer  => Mov(p, emptyReg2(loc2Size))
    })

  /** Perform some operation that forces the use of division registers (rax, rdx). They are guaranteed to be free.
   *
   * @param op The operation to perform on the division registers
   */
  def withDivRegisters(op: => Unit): Unit = op // We've guaranteed that the division registers are free
}
