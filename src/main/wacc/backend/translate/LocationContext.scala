package wacc

import wacc.TypedAST.Ident

import wacc.Size.*
import wacc.Register.*

import scala.collection.mutable
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{KnownType, SemType}

import java.rmi.UnexpectedException

type Location = Register | Pointer

class LocationContext {

  /** Free registers */
  private val freeRegs: mutable.ListBuffer[Register] = mutable.ListBuffer(
    RBX,
    RCX,
    RDI,
    RSI,
    R8,
    R9,
    R10,
    R11,
    R12,
    R13,
    R14,
    R15
  )

  /** Reserved registers in order, i.e. tail is latest reservation */
  private val reservedRegs = mutable.ListBuffer[Register]()

  /** Number of stack locations which have been reserved */
  private var reservedStackLocs = 1

  /** Map from identifiers to locations */
  private val identMap = mutable.Map[Int, Location]()

  // Register constants
  private val ReturnReg = RAX
  private val EmptyRegs = List(RAX, RDX) // never used as a location
  private val StackPointer = RSP
  private val BasePointer = RBP
  private val ArgRegs = List(RDI, RSI, RDX, RCX, R8, R9)
  private val CalleeSaved = List(RBX, R12, R13, R14, R15)
  private val CallerSaved = List(RCX, RSI, RDI, R8, R9, R10, R11)

  /** Get the next location to use, without actually using it
   *
   * @return The next location to use
   */
  def getNext: Location =
    if (freeRegs.nonEmpty) freeRegs.last
    else RegImmPointer(BasePointer, -reservedStackLocs * PointerSize.asBytes)

  /** Get the location to use and reserve it
   *
   * @return The reserved location
   */
  def reserveNext()(using instructionCtx: InstructionContext): Location = {
    if (freeRegs.nonEmpty) {
      val reg = freeRegs.remove(freeRegs.length - 1)
      reservedRegs += reg
      reg
    } else {
      val loc = RegImmPointer(BasePointer, -reservedStackLocs * PointerSize.asBytes)
      reservedStackLocs += 1
      // decrement the stack pointer
      instructionCtx.addInstruction(Sub(StackPointer, PointerSize.asBytes)(PointerSize))
      loc
    }
  }

  /** Move the getNext pointer to the last location */
  def unreserveLast()(using instructionCtx: InstructionContext): Unit = {
    if (reservedStackLocs > 1) {
      reservedStackLocs -= 1
      // increment the stack pointer
      instructionCtx.addInstruction(Add(StackPointer, PointerSize.asBytes)(PointerSize))
    } else {
      val reg = reservedRegs.remove(reservedRegs.length - 1)
      freeRegs += reg
    }
  }

  /** Reserve and associate the next free location with an identifier
   *
   * @param v    The identifier to associate with
   */
  def addLocation(v: Ident)(using instruction: InstructionContext): Unit = {
    identMap(v.id) = reserveNext()
  }

  /** Get the location associated with an identifier
   *
   * @param v The identifier to get the location of
   * @return The location associated with the identifier
   */
  def getLocation(v: Ident): Location = identMap(v.id)

  private def pushLocs(regs: List[Register])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs)
      instructionCtx.addInstruction(Push(r)(PointerSize))
  }

  private def popLocs(regs: List[Register])(using instructionCtx: InstructionContext): Unit = {
    for (r <- regs.reverse)
      instructionCtx.addInstruction(Pop(r)(PointerSize))
  }

  /** Set up stack frame, assign parameters a location and push callee-saved registers onto the stack.
   *
   * @note Run this at the start of a function.
   * @param params The parameters of the function
   */
  def setUpFunc(params: List[Ident])(using instructionCtx: InstructionContext): Unit =
    instructionCtx.addInstruction(Comment("Setting up function"))

    // 1. push base pointer
    instructionCtx.addInstruction(Push(BasePointer)(PointerSize))

    // 2. save callee-saved registers
    pushLocs(CalleeSaved)

    // 3. set up base pointer
    instructionCtx.addInstruction(Mov(BasePointer, StackPointer)(PointerSize))

    // 4. assign parameters a location

    // For the first 6 parameters, assign them to the first 6 argument registers manually, removing them from freeRegs
    params
      .zip(ArgRegs)
      .foreach((id, reg) => {
        reg match {
          case RDX =>
            // rdx is used for division, so we need to keep it free
            // move it to the first caller-saved register instead
            instructionCtx.addInstruction(Mov(CallerSaved.head, RDX)(Size(id.getType)))

            freeRegs -= CallerSaved.head
            identMap(id.id) = CallerSaved.head
          case r =>
            // move the parameter to the register
            freeRegs -= r
            identMap(id.id) = r
        }
      })

    // For the remaining parameters, assign them to the next available locations
    params
      .drop(ArgRegs.length)
      .reverse
      .zipWithIndex
      .foreach((id, index) => {
        val destLoc = getNext
        /* The location of the parameter is the base pointer + the size of the callee-saved registers + 2 (base pointer
         * and return address) + the index of the parameter (they are in reverse order) */
        val currLoc = RegImmPointer(BasePointer, PointerSize.asBytes * (CalleeSaved.length + 2 + index))
        movLocLoc(destLoc, currLoc, Size(id.getType))
        addLocation(id)
      })

    instructionCtx.addInstruction(Comment("Function set up complete"))

  /** Reset stack pointer and pop callee-saved registers from the stack, and set up the return value.
   *
   * @note Run this at the end of a function just before returning.
   * @param retVal The location of the return value
   * @param retSize The size of the return value
   */
  def cleanUpFunc(retVal: Location, retSize: Size)(using instructionCtx: InstructionContext): Unit = {
    instructionCtx.addInstruction(Comment("Cleaning up function"))

    // 1. set return value
    instructionCtx.addInstruction(Mov(ReturnReg, retVal)(retSize))

    // 2. reset the stack pointer
    instructionCtx.addInstruction(Mov(StackPointer, BasePointer)(PointerSize))

    // 3. pop callee-saved registers
    popLocs(CalleeSaved)

    // 4. pop base pointer
    instructionCtx.addInstruction(Pop(BasePointer)(PointerSize))

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
              RegImmPointer(StackPointer, PointerSize.asBytes * (CallerSaved.length - CallerSaved.indexOf(r) - 1))
            instructionCtx.addInstruction(Mov(argReg, newStackLoc)(PointerSize))
          else instructionCtx.addInstruction(Mov(argReg, r)(PointerSize))
        case p: Pointer => instructionCtx.addInstruction(Mov(argReg, p)(PointerSize))
      }
    }

    // 3. Move remaining arguments to the stack
    for ((argLoc, index) <- argLocations.drop(ArgRegs.length).zipWithIndex) {
      argLoc match {
        case r: Register =>
          if (CallerSaved.contains(r))
            // If the location is a caller-saved register, it is now in the stack
            val newStackLoc =
              RegImmPointer(
                StackPointer,
                PointerSize.asBytes * (CallerSaved.length + index - CallerSaved.indexOf(r) - 1)
              )
            instructionCtx.addInstruction(Push(newStackLoc)(PointerSize))
          else instructionCtx.addInstruction(Push(r)(PointerSize))
        case p: Pointer => instructionCtx.addInstruction(Push(p)(PointerSize))
      }
    }

    instructionCtx.addInstruction(Comment("Function call set up complete"))
  }

  /** Restore caller registers and save result to a location
   *
   * @note Run this just after calling a function.
   * @return The location of the result
   */
  def cleanUpCall(numArgs: Int)(using instructionCtx: InstructionContext): Location =
    instructionCtx.addInstruction(Comment("Cleaning up function call"))

    // 1. Discard arguments
    instructionCtx.addInstruction(
      Add(StackPointer, PointerSize.asBytes * (numArgs - ArgRegs.length).max(0))(PointerSize)
    )

    // 2. Restore caller registers
    popLocs(CallerSaved)

    instructionCtx.addInstruction(Comment("Function call clean up complete"))

    // 3. Return result location
    ReturnReg

  /** Move a value from one location to another
   *
   * @param dest The destination location
   * @param src  The source location
   * @param size The size of the value to move
   */
  def movLocLoc(dest: Location, src: Location, size: Size)(using instructionCtx: InstructionContext): Unit =
    dest match {
      case destR: Register => instructionCtx.addInstruction(Mov(destR, src)(size))
      case destP: Pointer =>
        src match {
          case srcR: Register => instructionCtx.addInstruction(Mov(destP, srcR)(size))
          case srcP: Pointer =>
            val emptyReg = EmptyRegs.head
            instructionCtx.addInstruction(Mov(emptyReg, srcP)(size))
            instructionCtx.addInstruction(Mov(destP, emptyReg)(size))
        }
    }

  /** Perform some operation that forces the use of 1 register.
   * This is useful for operations that require a register as an operand, but you only have a location.
   *
   * @param loc1 The first location
   * @param size The size of the data in the first location
   * @param op   The operation to perform on the two locations, where the first location is guaranteed to be a register
   */
  def regInstr1(loc1: Location, size: Size, op: Register => Instruction)(using
      instructionCtx: InstructionContext
  ): Unit =
    val emptyReg = EmptyRegs.head

    // Move the location to a register, perform the operation, then move the result back
    instructionCtx.addInstruction(Mov(emptyReg, loc1)(size))
    instructionCtx.addInstruction(op(emptyReg))
    instructionCtx.addInstruction(loc1 match {
      case r: Register => Mov(r, emptyReg)(size)
      case p: Pointer  => Mov(p, emptyReg)(size)
    })

  /** Perform some operation that forces the use of 2 registers.
   * This is useful for operations that require 2 registers as an operands, but you only have a location.
   *
   * @param loc1 The first location
   * @param loc2 The second location
   * @param size1 The size of the data in the first location
   * @param size2 The size of the data in the second location
   * @param op   The operation to perform on the locations as registers
   */
  def regInstr2(loc1: Location, loc2: Location, size1: Size, size2: Size, op: (Register, Register) => Instruction)(using
      instructionCtx: InstructionContext
  ): Unit =
    val emptyReg1 = EmptyRegs.head
    val emptyReg2 = EmptyRegs.last

    // Move the locations to registers, perform the operation, then move the results back
    instructionCtx.addInstruction(Mov(emptyReg1, loc1)(size1))
    instructionCtx.addInstruction(Mov(emptyReg2, loc2)(size2))
    instructionCtx.addInstruction(op(emptyReg1, emptyReg2))
    instructionCtx.addInstruction(loc1 match {
      case r: Register => Mov(r, emptyReg1)(size1)
      case p: Pointer  => Mov(p, emptyReg1)(size1)
    })
    instructionCtx.addInstruction(loc2 match {
      case r: Register => Mov(r, emptyReg2)(size2)
      case p: Pointer  => Mov(p, emptyReg2)(size2)
    })

  /** Perform some operation that forces the use of division registers (rax, rdx). They are guaranteed to be free.
   *
   * @param op The operation to perform on the division registers
   */
  def withDivRegisters(op: => Unit): Unit = op // We've guaranteed that the division registers are free
}
