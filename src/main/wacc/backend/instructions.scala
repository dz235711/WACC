package wacc

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
sealed trait RestrictedInstruction extends Instruction {
  def apply(dest: Register | Pointer, src: Immediate | Register): this.type = this(dest, src)
  def apply(dest: Register, src: Immediate | Register | Pointer): this.type = this(dest, src)
}

// Arithmetic instructions
case class AddCarry private (dest: Register | Pointer, src: Immediate | Register | Pointer)
    extends RestrictedInstruction
case class Add(dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction
case class Dec(dest: Register | Pointer) extends Instruction
case class Inc(dest: Register | Pointer) extends Instruction
case class Div(src: Register | Pointer) extends Instruction
case class SignedDiv(src: Register | Pointer) extends Instruction
case class Mul(src: Register | Pointer) extends Instruction
case class SignedMul(dest: Option[Register], src1: Register | Pointer, src2: Option[Immediate])
case class Neg(dest: Register | Pointer) extends Instruction
case class Not(dest: Register | Pointer) extends Instruction
case class Sub(dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction

// Logical and bitwise instrucitons
case class And(dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction
case class Or(dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction
case class Xor(dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction
case class ShiftArithLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftArithRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalLeft(dest: Register | Pointer, count: Immediate) extends Instruction
case class ShiftLogicalRight(dest: Register | Pointer, count: Immediate) extends Instruction
case class Test(src1: Register | Pointer, src2: Immediate | Register) extends Instruction
case class Compare(src1: Register | Pointer, src2: Immediate | Register | Pointer) extends RestrictedInstruction

// Jump instructions
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
case class Mov private (dest: Register | Pointer, src: Immediate | Register | Pointer) extends RestrictedInstruction
case class Push(src: Register | Pointer | Immediate) extends Instruction
case class Pop(dest: Register | Pointer) extends Instruction
case class Lea(dest: Register, src: Pointer) extends Instruction

// Control transfer instructions
case class Call(label: String) extends Instruction
case class Ret(imm: Option[Immediate]) extends Instruction
object Nop extends Instruction
object Halt extends Instruction
