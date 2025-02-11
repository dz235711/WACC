import wacc.Register
import wacc.Size

type Immediate = Int

sealed trait Pointer {
  val size: Size
}

/** Pointer for `[reg]` */
case class RegPointer(reg: Register)(val size: Size) extends Pointer

/** Pointer for `[reg + imm]` */
case class RegImmPointer(reg: Register, imm: Immediate)(val size: Size) extends Pointer

/** Pointer for `[reg1 + reg2]` */
case class RegRegPointer(reg1: Register, reg2: Register)(val size: Size) extends Pointer

/** Pointer for `[reg1 + scale * reg2]` */
case class RegScaleRegPointer(reg1: Register, scale: Int, reg2: Register)(val size: Size) extends Pointer

/** Pointer for `[reg1 + scale * reg2 + imm]` */
case class RegScaleRegImmPointer(reg1: Register, scale: Int, reg2: Register, imm: Immediate)(val size: Size)
    extends Pointer

/** Pointer for `[scale * reg + imm]` */
case class ScaleRegImmPointer(scale: Int, reg: Register, imm: Immediate)(val size: Size) extends Pointer
