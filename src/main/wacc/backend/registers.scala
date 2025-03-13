package wacc

import wacc.RenamedAST.SemType
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.?

enum Size {
  case W8, W16, W32, W64

  def asBytes: Int = this match {
    case W8  => 1
    case W16 => 2
    case W32 => 4
    case W64 => 8
  }
}

object Size {

  /** Constructs a size given a semantic type.
   * 
   * @param semType The semantic type
   * @return The size of the semantic type
   */
  def apply(semType: SemType): Size = semType match {
    case IntType        => W32
    case BoolType       => W8
    case CharType       => W8
    case StringType     => W64
    case ArrayType(_)   => W64
    case PairType(_, _) => W64
    case ?              => W64
  }
}

/** The size of a pointer. */
val PointerSize = Size.W64

enum Register {

  /** The return register, saved by caller. */
  case RAX

  /** General register, saved by callee. */
  case RBX

  /** The register for the 4th function argument, saved by caller. */
  case RCX

  /** The register for the 3rd function argument, saved by caller. */
  case RDX

  /** The register for the 2nd function argument, saved by caller. */
  case RSI

  /** The register for the 1st function argument, saved by caller. */
  case RDI

  /** The stack pointer register, saved by callee. */
  case RSP

  /** The base pointer register, saved by callee. */
  case RBP

  /** The register for the 5th function argument, saved by caller. */
  case R8

  /** The register for the 6th function argument, saved by caller. */
  case R9

  /** General register, saved by caller. */
  case R10

  /** General register, saved by caller. */
  case R11

  /** General register, saved by callee. */
  case R12

  /** General register, saved by callee. */
  case R13

  /** General register, saved by callee. */
  case R14

  /** General register, saved by callee. */
  case R15

  /** Instruction pointer. */
  case RIP
}

/**  The program counter register. */
val INSTRUCTION_POINTER = Register.RIP

/** The stack pointer register. */
val STACK_POINTER = Register.RSP

/** The base pointer register. */
val BASE_POINTER = Register.RBP

/** The register for the 1st function argument. */
val ARG_1 = Register.RDI

/** The register for the 2nd function argument. */
val ARG_2 = Register.RSI

/** The register for the 3rd function argument. */
val ARG_3 = Register.RDX

/** The register for the return value. */
val RETURN = Register.RAX

/** Th quotient register for division. */
val QUOT_REG = Register.RAX

/** The remainder register for division. */
val REM_REG = Register.RDX
