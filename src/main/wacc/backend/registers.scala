package wacc

enum Size {
  case W8, W16, W32, W64

  def toBytes: Int = this match {
    case W8  => 1
    case W16 => 2
    case W32 => 4
    case W64 => 8
  }
}
trait Sizeable {
  val size: Size
}

sealed trait Register extends Sizeable

/** The program counter register. */
object RIP extends Register {
  val size: Size = Size.W64
}

/** The return register, saved by caller. */
case class RAX(size: Size) extends Register

/** General register, saved by callee. */
case class RBX(size: Size) extends Register

/** The register for the 4th function argument, saved by caller. */
case class RCX(size: Size) extends Register

/** The register for the 3rd function argument, saved by caller. */
case class RDX(size: Size) extends Register

/** The register for the 2nd function argument, saved by caller. */
case class RSI(size: Size) extends Register

/** The register for the 1st function argument, saved by caller. */
case class RDI(size: Size) extends Register

/** The stack pointer register, saved by callee. */
case class RSP(size: Size) extends Register

/** The base pointer register, saved by callee. */
case class RBP(size: Size) extends Register

/** The register for the 5th function argument, saved by caller. */
case class R8(size: Size) extends Register

/** The register for the 6th function argument, saved by caller. */
case class R9(size: Size) extends Register

/** General register, saved by caller. */
case class R10(size: Size) extends Register

/** General register, saved by caller. */
case class R11(size: Size) extends Register

/** General register, saved by callee. */
case class R12(size: Size) extends Register

/** General register, saved by callee. */
case class R13(size: Size) extends Register

/** General register, saved by callee. */
case class R14(size: Size) extends Register

/** General register, saved by callee. */
case class R15(size: Size) extends Register
