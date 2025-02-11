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

case class RAX(width: RegisterWidth) extends Register
case class RBX(width: RegisterWidth) extends Register
case class RCX(width: RegisterWidth) extends Register
case class RDX(width: RegisterWidth) extends Register
case class RSI(width: RegisterWidth) extends Register
case class RDI(width: RegisterWidth) extends Register
case class RSP(width: RegisterWidth) extends Register
case class RBP(width: RegisterWidth) extends Register
case class R8(width: RegisterWidth) extends Register
case class R9(width: RegisterWidth) extends Register
case class R10(width: RegisterWidth) extends Register
case class R11(width: RegisterWidth) extends Register
case class R12(width: RegisterWidth) extends Register
case class R13(width: RegisterWidth) extends Register
case class R14(width: RegisterWidth) extends Register
case class R15(width: RegisterWidth) extends Register
