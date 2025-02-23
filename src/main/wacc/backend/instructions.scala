package wacc

type Immediate = Int

sealed trait Pointer extends Location {
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
case class AddCarry private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object AddCarry {
  def apply(dest: Pointer, src: Immediate | Register): AddCarry = new AddCarry(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): AddCarry = new AddCarry(dest, src)
}
case class Add private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object Add {
  def apply(dest: Pointer, src: Immediate | Register): Add = new Add(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Add = new Add(dest, src)
}
case class Dec(dest: Register | Pointer) extends Instruction
case class Inc(dest: Register | Pointer) extends Instruction
case class Div(src: Register | Pointer) extends Instruction
case class SignedDiv(src: Register | Pointer) extends Instruction
case class Mul(src: Register | Pointer) extends Instruction
case class SignedMul(dest: Option[Register], src1: Register | Pointer, src2: Option[Immediate]) extends Instruction
case class Neg(dest: Register | Pointer) extends Instruction
case class Not(dest: Register | Pointer) extends Instruction
case class Sub private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object Sub {
  def apply(dest: Pointer, src: Immediate | Register): Sub = new Sub(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Sub = new Sub(dest, src)
}

// Logical and bitwise instructions
case class And private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object And {
  def apply(dest: Pointer, src: Immediate | Register): And = new And(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): And = new And(dest, src)
}
case class Or private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object Or {
  def apply(dest: Pointer, src: Immediate | Register): Or = new Or(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Or = new Or(dest, src)
}
case class Xor private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object Xor {
  def apply(dest: Pointer, src: Immediate | Register): Xor = new Xor(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Xor = new Xor(dest, src)
}
case class ShiftArithLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftArithRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class Test(src1: Register | Pointer, src2: Immediate | Register) extends Instruction
case class Compare private (src1: Register | Pointer, src2: Immediate | Register | Pointer) extends Instruction
object Compare {
  def apply(dest: Pointer, src: Immediate | Register): Compare = new Compare(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Compare = new Compare(dest, src)
}

// Jump instructions
case class DefineLabel(label: String) extends Instruction
case class Jmp(label: String) extends Instruction
case class JmpEqual(label: String) extends Instruction
case class JmpNotEqual(label: String) extends Instruction
case class JmpGreater(label: String) extends Instruction
case class JmpGreaterEqual(label: String) extends Instruction
case class JmpLess(label: String) extends Instruction
case class JmpLessEqual(label: String) extends Instruction
case class JmpZero(label: String) extends Instruction
case class JmpNotZero(label: String) extends Instruction
case class JumpCarry(label: String) extends Instruction
case class JumpNotCarry(label: String) extends Instruction
case class JumpOverflow(label: String) extends Instruction
case class JumpNotOverflow(label: String) extends Instruction
case class JumpSign(label: String) extends Instruction
case class JumpNotSign(label: String) extends Instruction
case class JumpParity(label: String) extends Instruction
case class JumpNotParity(label: String) extends Instruction
case class JumpAbove(label: String) extends Instruction
case class JumpAboveEqual(label: String) extends Instruction
case class JumpBelow(label: String) extends Instruction
case class JumpBelowEqual(label: String) extends Instruction

// Data transfer instructions
case class Mov private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends Instruction
object Mov {
  def apply(dest: Pointer, src: Immediate | Register): Mov = new Mov(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): Mov = new Mov(dest, src)
}
case class Push(src: Register | Pointer | Immediate) extends Instruction
case class Pop(dest: Register | Pointer) extends Instruction
case class Lea(dest: Register, src: Pointer) extends Instruction

// Control transfer instructions
case class Call(label: String) extends Instruction
case class Ret(imm: Option[Immediate]) extends Instruction
object Nop extends Instruction
object Halt extends Instruction

// Comment instruction
case class Comment(comment: String) extends Instruction
