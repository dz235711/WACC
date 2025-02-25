package wacc

enum Size {
  case W8
  case W16
  case W32
  case W64
}

sealed trait Register {
  val width: Size
}

/** The program counter register. */
object RIP extends Register {
  val width = Size.W64
}

/** The return register, saved by caller. */
case class RAX(width: Size) extends Register

/** General register, saved by callee. */
case class RBX(width: Size) extends Register

/** The register for the 4th function argument, saved by caller. */
case class RCX(width: Size) extends Register

/** The register for the 3rd function argument, saved by caller. */
case class RDX(width: Size) extends Register

/** The register for the 2nd function argument, saved by caller. */
case class RSI(width: Size) extends Register

/** The register for the 1st function argument, saved by caller. */
case class RDI(width: Size) extends Register

/** The stack pointer register, saved by callee. */
case class RSP(width: Size) extends Register

/** The base pointer register, saved by callee. */
case class RBP(width: Size) extends Register

/** The register for the 5th function argument, saved by caller. */
case class R8(width: Size) extends Register

/** The register for the 6th function argument, saved by caller. */
case class R9(width: Size) extends Register

/** General register, saved by caller. */
case class R10(width: Size) extends Register

/** General register, saved by caller. */
case class R11(width: Size) extends Register

/** General register, saved by callee. */
case class R12(width: Size) extends Register

/** General register, saved by callee. */
case class R13(width: Size) extends Register

/** General register, saved by callee. */
case class R14(width: Size) extends Register

/** General register, saved by callee. */
case class R15(width: Size) extends Register
