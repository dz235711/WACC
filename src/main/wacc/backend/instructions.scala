package wacc

type Immediate = Int

trait Pointer

/** Pointer for `[reg]` */
case class RegPointer(reg: Register) extends Pointer

/** Pointer for `[reg + imm]` */
case class RegImmPointer(reg: Register, imm: Immediate | Label) extends Pointer

/** Pointer for `[reg1 + reg2]` */
case class RegRegPointer(reg1: Register, reg2: Register) extends Pointer

/** Pointer for `[reg1 + scale * reg2]` */
case class RegScaleRegPointer(reg1: Register, scale: Int, reg2: Register) extends Pointer

/** Pointer for `[reg1 + scale * reg2 + imm]` */
case class RegScaleRegImmPointer(reg1: Register, scale: Int, reg2: Register, imm: Immediate | Label) extends Pointer

/** Pointer for `[scale * reg + imm]` */
case class ScaleRegImmPointer(scale: Int, reg: Register, imm: Immediate | Label) extends Pointer

sealed trait Instruction extends Product {
  val size: Size
}

// Arithmetic instructions
case class AddCarry private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object AddCarry {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): AddCarry = new AddCarry(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): AddCarry = new AddCarry(dest, src)(size)
}
case class Add private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Add {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Add = new Add(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Add = new Add(dest, src)(size)
}
case class Dec(dest: Register | Pointer)(val size: Size) extends Instruction
case class Inc(dest: Register | Pointer)(val size: Size) extends Instruction
case class Div(src: Register | Pointer)(val size: Size) extends Instruction
case class SignedDiv(src: Register | Pointer)(val size: Size) extends Instruction
case class Mul(src: Register | Pointer)(val size: Size) extends Instruction
case class SignedMul(dest: Option[Register], src1: Register | Pointer, src2: Option[Immediate])(val size: Size)
    extends Instruction
case class Neg(dest: Register | Pointer)(val size: Size) extends Instruction
case class Not(dest: Register | Pointer)(val size: Size) extends Instruction
case class Sub private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Sub {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Sub = new Sub(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Sub = new Sub(dest, src)(size)
}

// Logical and bitwise instructions
case class And private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object And {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): And = new And(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): And = new And(dest, src)(size)
}
case class Or private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Or {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Or = new Or(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Or = new Or(dest, src)(size)
}
case class Xor private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Xor {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Xor = new Xor(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Xor = new Xor(dest, src)(size)
}
case class ShiftArithLeft(dest: Register | Pointer, count: Immediate)(val size: Size) extends Instruction
case class ShiftArithRight(dest: Register | Pointer, count: Immediate)(val size: Size) extends Instruction
case class ShiftLogicalLeft(dest: Register | Pointer, count: Immediate)(val size: Size) extends Instruction
case class ShiftLogicalRight(dest: Register | Pointer, count: Immediate)(val size: Size) extends Instruction
case class Test(src1: Register | Pointer, src2: Immediate | Register)(val size: Size) extends Instruction
case class Compare private (src1: Register | Pointer, src2: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Compare {
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Compare = new Compare(dest, src)(size)
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Compare = new Compare(dest, src)(size)
}

// Jump instructions
enum Condition {
  case NoCond, Equal, NotEqual, Greater, GreaterEqual, Less, LessEqual, Zero, NotZero, Carry, NotCarry, Overflow,
    NotOverflow,
    Sign, NotSign, Parity, NotParity, Above, AboveEqual, Below, BelowEqual
}

case class DefineLabel(label: Label) extends Instruction {
  val size: Size = Size.W64
}
case class Jmp(cond: Condition, label: Label) extends Instruction {
  val size: Size = Size.W64
}

// Byte set Instructions
case class SetGreater(dest: Register | Pointer)(val size: Size) extends Instruction
case class SetGreaterEqual(dest: Register | Pointer)(val size: Size) extends Instruction
case class SetSmaller(dest: Register | Pointer)(val size: Size) extends Instruction
case class SetSmallerEqual(dest: Register | Pointer)(val size: Size) extends Instruction
case class SetEqual(dest: Register | Pointer)(val size: Size) extends Instruction

// Data transfer instructions
case class Mov private (dest: Register | Pointer, src: Immediate | Register | Pointer)(val size: Size)
    extends Instruction
object Mov {
  def apply(dest: Pointer, src: Immediate | Register)(size: Size): Mov = new Mov(dest, src)(size)
  def apply(dest: Register, src: Immediate | Register | Pointer)(size: Size): Mov = new Mov(dest, src)(size)
}
case class Movzx(dest: Register, src: Register | Pointer)(val size: Size) extends Instruction
case class Push(src: Register | Pointer | Immediate)(val size: Size) extends Instruction
case class Pop(dest: Register | Pointer)(val size: Size) extends Instruction
case class Lea(dest: Register, src: Pointer)(val size: Size) extends Instruction
case class Cdq() extends Instruction {
  val size: Size = Size.W64
}

// Control transfer instructions
case class Call(label: Label) extends Instruction {
  val size: Size = Size.W64
}
case class Ret(imm: Option[Immediate]) extends Instruction {
  val size: Size = Size.W64
}
case class Nop() extends Instruction {
  val size: Size = Size.W64
}
case class Halt() extends Instruction {
  val size: Size = Size.W64
}

// Comment instruction
case class Comment(comment: String) extends Instruction {
  val size: Size = Size.W64
}

// Memory sections and directives
case class NoPrefixSyntax() extends Instruction {
  val size: Size = Size.W64
}
case class GlobalMain() extends Instruction {
  val size: Size = Size.W64
}
case class SectionReadOnlyData() extends Instruction {
  val size: Size = Size.W64
}
case class Text() extends Instruction {
  val size: Size = Size.W64
}
case class IntData(length: Int) extends Instruction {
  val size: Size = Size.W64
}
case class Asciz(str: String) extends Instruction {
  val size: Size = Size.W64
}
