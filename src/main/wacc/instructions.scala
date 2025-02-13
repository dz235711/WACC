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

sealed trait Instruction

// Arithmetic instructions
case class AddCarry(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
case class Add(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
case class Dec(dest: Register | Pointer) extends Instruction
case class Inc(dest: Register | Pointer) extends Instruction
case class Div(src: Register | Pointer) extends Instruction
case class SignedDiv(src: Register | Pointer) extends Instruction
case class Mul(src: Register | Pointer) extends Instruction
// TODO: IMUL with default values
case class Neg(dest: Register | Pointer) extends Instruction
case class Not(dest: Register | Pointer) extends Instruction
case class Sub(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction

// Logical and bitwise instrucitons
case class And(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
case class Or(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
case class Xor(dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
case class ShiftArithLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftArithRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class Test(src1: Register | Pointer, src2: Immediate | Register) extends Instruction
case class Compare(src1: Register | Pointer, src2: Immediate | Register | Pointer) extends Instruction
