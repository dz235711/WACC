package wacc

import wacc.Size.*

type Operand = Register | Pointer | Immediate | String

class x86Stringifier {
  def stringify(instructions: List[Instruction]): String = {
    instructions
      .map(instr => {
        val translated = stringifyInstr(instr)
        if (translated.startsWith(".") || translated.endsWith(":")) translated
        else s"${" " * INDENTATION_SIZE}translated"
      })
      .mkString("\n")
  }

  private def stringifyInstr(
      instr: Instruction
  ): String = instr match {
    case Mov(dest, src)                 => s"mov ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Call(label)                    => s"call ${stringifyOperand(label)}"
    case Ret(imm)                       => s"ret${stringifyOperand(imm, prefix = " ")}"
    case Nop                            => "nop"
    case Halt                           => "hlt"
    case Push(src)                      => s"push ${stringifyOperand(src)}"
    case Pop(dest)                      => s"pop ${stringifyOperand(dest)}"
    case Lea(dest, src)                 => s"lea ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case DefineLabel(label)             => s"${stringifyOperand(label)}:"
    case Jmp(label)                     => s"jmp ${stringifyOperand(label)}"
    case JmpEqual(label)                => s"je ${stringifyOperand(label)}"
    case JmpNotEqual(label)             => s"jne ${stringifyOperand(label)}"
    case JmpGreater(label)              => s"jg ${stringifyOperand(label)}"
    case JmpGreaterEqual(label)         => s"jge ${stringifyOperand(label)}"
    case JmpLess(label)                 => s"jl ${stringifyOperand(label)}"
    case JmpLessEqual(label)            => s"jle ${stringifyOperand(label)}"
    case JmpZero(label)                 => s"jz ${stringifyOperand(label)}"
    case JmpNotZero(label)              => s"jnz ${stringifyOperand(label)}"
    case JumpCarry(label)               => s"jc ${stringifyOperand(label)}"
    case JumpNotCarry(label)            => s"jnc ${stringifyOperand(label)}"
    case JumpOverflow(label)            => s"jo ${stringifyOperand(label)}"
    case JumpNotOverflow(label)         => s"jno ${stringifyOperand(label)}"
    case JumpSign(label)                => s"js ${stringifyOperand(label)}"
    case JumpNotSign(label)             => s"jns ${stringifyOperand(label)}"
    case JumpParity(label)              => s"jp ${stringifyOperand(label)}"
    case JumpNotParity(label)           => s"jnp ${stringifyOperand(label)}"
    case JumpAbove(label)               => s"ja ${stringifyOperand(label)}"
    case JumpAboveEqual(label)          => s"jae ${stringifyOperand(label)}"
    case JumpBelow(label)               => s"jb ${stringifyOperand(label)}"
    case JumpBelowEqual(label)          => s"jbe ${stringifyOperand(label)}"
    case And(dest, src)                 => s"and ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Or(dest, src)                  => s"or ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Xor(dest, src)                 => s"xor ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case ShiftArithLeft(dest, count)    => s"sal ${stringifyOperand(dest)}, ${stringifyOperand(count)}"
    case ShiftArithRight(dest, count)   => s"sar ${stringifyOperand(dest)}, ${stringifyOperand(count)}"
    case ShiftLogicalLeft(dest, count)  => s"shl ${stringifyOperand(dest)}, ${stringifyOperand(count)}"
    case ShiftLogicalRight(dest, count) => s"shr ${stringifyOperand(dest)}, ${stringifyOperand(count)}"
    case Test(src1, src2)               => s"test ${stringifyOperand(src1)}, ${stringifyOperand(src2)}"
    case Compare(dest, src)             => s"cmp ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case AddCarry(dest, src)            => s"adc ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Add(dest, src)                 => s"add ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Dec(dest)                      => s"dec ${stringifyOperand(dest)}"
    case Inc(dest)                      => s"inc ${stringifyOperand(dest)}"
    case Div(src)                       => s"div ${stringifyOperand(src)}"
    case SignedDiv(src)                 => s"idiv ${stringifyOperand(src)}"
    case Mul(src)                       => s"mul ${stringifyOperand(src)}"
    case SignedMul(dest, src1, src2) =>
      s"imul ${stringifyOperand(dest, postfix = ", ")}${stringifyOperand(src1)}${stringifyOperand(src2, prefix = ", ")}"
    case Neg(dest)        => s"neg ${stringifyOperand(dest)}"
    case Not(dest)        => s"not ${stringifyOperand(dest)}"
    case Sub(dest, src)   => s"sub ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Comment(comment) => s"// $comment"
  }

  private def stringifyOperand(
      operand: Option[Operand],
      prefix: String = "",
      postfix: String = ""
  ): String = operand match {
    case Some(operand) => s"$prefix${stringifyOperand(operand)}$postfix"
    case None          => ""
  }

  private def stringifyOperand(operand: Operand): String = operand match {
    case n: Immediate => s"$n"
    case r: Register  => stringifyRegister(r)
    case p: Pointer   => stringifyPointer(p)
    case s: String    => s
  }

  private def stringifyPointer(pointer: Pointer): String = s"${ptrSize(pointer.size)} ${pointer match {
      case RegPointer(reg)           => s"[${stringifyOperand(reg)}]"
      case RegImmPointer(reg, imm)   => s"[${stringifyOperand(reg)}+${stringifyOperand(imm)}]"
      case RegRegPointer(reg1, reg2) => s"[${stringifyOperand(reg1)}+${stringifyOperand(reg2)}]"
      case RegScaleRegPointer(reg1, scale, reg2) =>
        s"[${stringifyOperand(reg1)}+${stringifyOperand(scale)}*${stringifyOperand(reg2)}]"
      case RegScaleRegImmPointer(reg1, scale, reg2, imm) =>
        s"[${stringifyOperand(reg1)}+${stringifyOperand(scale)}*${stringifyOperand(reg2)}+${stringifyOperand(imm)}]"
      case ScaleRegImmPointer(scale, reg, imm) =>
        s"[${stringifyOperand(scale)}*${stringifyOperand(reg)}+${stringifyOperand(imm)}]"
    }}"

  private def ptrSize(size: Size): String = s"${size match {
      case W8  => "byte"
      case W16 => "word"
      case W32 => "dword"
      case W64 => "qword"
    }} ptr"

  private def stringifyRegister(register: Register): String = register match {
    case RAX(s) => prependSize(s, "ax", false)
    case RBX(s) => prependSize(s, "bx", false)
    case RCX(s) => prependSize(s, "cx", false)
    case RDX(s) => prependSize(s, "dx", false)
    case RSI(s) => prependSize(s, "si")
    case RDI(s) => prependSize(s, "di")
    case RSP(s) => prependSize(s, "sp")
    case RBP(s) => prependSize(s, "bp")
    case R8(s)  => appendSize(s, "r8")
    case R9(s)  => appendSize(s, "r9")
    case R10(s) => appendSize(s, "r10")
    case R11(s) => appendSize(s, "r11")
    case R12(s) => appendSize(s, "r12")
    case R13(s) => appendSize(s, "r13")
    case R14(s) => appendSize(s, "r14")
    case R15(s) => appendSize(s, "r15")
  }

  private def prependSize(size: Size, register: String, keepTail: Boolean = true): String = size match {
    case W8  => s"${(if keepTail then register else register.slice(0, register.length - 1))}l"
    case W16 => register
    case W32 => s"e$register"
    case W64 => s"r$register"
  }

  private def appendSize(size: Size, register: String): String = size match {
    case W8  => s"${register}b"
    case W16 => s"${register}w"
    case W32 => s"${register}d"
    case W64 => register
  }
}
