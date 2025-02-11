package wacc

enum RegisterWidth {
  case W8
  case W16
  case W32
  case W64
}

sealed trait Register {
  val width: RegisterWidth
}

/** The return register, saved by caller. */
case class RAX(width: RegisterWidth) extends Register

/** General register, saved by callee. */
case class RBX(width: RegisterWidth) extends Register

/** The register for the 4th function argument, saved by caller. */
case class RCX(width: RegisterWidth) extends Register

/** The register for the 3rd function argyment, saved by caller. */
case class RDX(width: RegisterWidth) extends Register

/** The register for the 2nd function argument, saved by caller. */
case class RSI(width: RegisterWidth) extends Register

/** The register for the 1st function argument, saved by caller. */
case class RDI(width: RegisterWidth) extends Register

/** The stack pointer register, saved by callee. */
case class RSP(width: RegisterWidth) extends Register

/** The base pointer register, saved by callee. */
case class RBP(width: RegisterWidth) extends Register

/** The register for the 5th function argument, saved by caller. */
case class R8(width: RegisterWidth) extends Register

/** The register for the 6th function argument, saved by caller. */
case class R9(width: RegisterWidth) extends Register

/** General register, saved by caller. */
case class R10(width: RegisterWidth) extends Register

/** General register, saved by caller. */
case class R11(width: RegisterWidth) extends Register

/** General register, saved by callee. */
case class R12(width: RegisterWidth) extends Register

/** General register, saved by callee. */
case class R13(width: RegisterWidth) extends Register

/** General register, saved by callee. */
case class R14(width: RegisterWidth) extends Register

/** General register, saved by callee. */
case class R15(width: RegisterWidth) extends Register
