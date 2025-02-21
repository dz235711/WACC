package wacc

import wacc.Size.*

class x86Stringifier {
  def stringify(instructions: List[Instruction]): String = {
    instructions
      .map(instr => {
        val translated = stringifyInstr(instr)
        if (translated.startsWith(".") || translated.endsWith(":")) translated
        else " " * INDENTATION_SIZE + translated
      })
      .mkString("\n")
  }

  private def stringifyInstr(
      instr: Instruction
  ): String = instr match {
    case Mov(dest, src)                 => s"mov ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Call(label)                    => s"call $label"
    case Ret(imm)                       => s"ret${ifDefined(imm, prefix = " ")}"
    case Nop                            => "nop"
    case Halt                           => "hlt"
    case Push(src)                      => s"push ${stringifyOperand(src)}"
    case Pop(dest)                      => s"pop ${stringifyOperand(dest)}"
    case Lea(dest, src)                 => s"lea ${stringifyRegister(dest)}, ${stringifyPointer(src)}"
    case DefineLabel(label)             => s"$label:"
    case Jmp(label)                     => s"jmp $label"
    case JmpEqual(label)                => s"je $label"
    case JmpNotEqual(label)             => s"jne $label"
    case JmpGreater(label)              => s"jg $label"
    case JmpGreaterEqual(label)         => s"jge $label"
    case JmpLess(label)                 => s"jl $label"
    case JmpLessEqual(label)            => s"jle $label"
    case JmpZero(label)                 => s"jz $label"
    case JmpNotZero(label)              => s"jnz $label"
    case JumpCarry(label)               => s"jc $label"
    case JumpNotCarry(label)            => s"jnc $label"
    case JumpOverflow(label)            => s"jo $label"
    case JumpNotOverflow(label)         => s"jno $label"
    case JumpSign(label)                => s"js $label"
    case JumpNotSign(label)             => s"jns $label"
    case JumpParity(label)              => s"jp $label"
    case JumpNotParity(label)           => s"jnp $label"
    case JumpAbove(label)               => s"ja $label"
    case JumpAboveEqual(label)          => s"jae $label"
    case JumpBelow(label)               => s"jb $label"
    case JumpBelowEqual(label)          => s"jbe $label"
    case And(dest, src)                 => s"and ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Or(dest, src)                  => s"or ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Xor(dest, src)                 => s"xor ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case ShiftArithLeft(dest, count)    => s"sal ${stringifyOperand(dest)}, $count"
    case ShiftArithRight(dest, count)   => s"sar ${stringifyOperand(dest)}, $count"
    case ShiftLogicalLeft(dest, count)  => s"shl ${stringifyOperand(dest)}, $count"
    case ShiftLogicalRight(dest, count) => s"shr ${stringifyOperand(dest)}, $count"
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
      s"imul ${ifDefined(dest, postfix = ", ")}${stringifyOperand(src1)}${ifDefined(src2, prefix = ", ")}"
    case Neg(dest)        => s"neg ${stringifyOperand(dest)}"
    case Not(dest)        => s"not ${stringifyOperand(dest)}"
    case Sub(dest, src)   => s"sub ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Comment(comment) => s"// $comment"
  }

  private def ifDefined(
      operand: Option[Register | Pointer | Immediate | String],
      prefix: String = "",
      postfix: String = ""
  ): String = operand match {
    case Some(operand) => s"$prefix${stringifyOperand(operand)}$postfix"
    case None          => ""
  }

  private def stringifyOperand(operand: Register | Pointer | Immediate | String): String = operand match {
    case n: Immediate => n.toString
    case r: Register  => stringifyRegister(r)
    case p: Pointer   => stringifyPointer(p)
    case s: String    => s
  }

  private def stringifyPointer(pointer: Pointer) = pointer match {
    case RegPointer(reg)                               => s"[$reg]"
    case RegImmPointer(reg, imm)                       => s"[$reg + $imm]"
    case RegRegPointer(reg1, reg2)                     => s"[$reg1 + $reg2]"
    case RegScaleRegPointer(reg1, scale, reg2)         => s"[$reg1 + $scale * $reg2]"
    case RegScaleRegImmPointer(reg1, scale, reg2, imm) => s"[$reg1 + $scale * $reg2 + $imm]"
    case ScaleRegImmPointer(scale, reg, imm)           => s"[$scale * $reg + $imm]"
  }

  private def stringifyRegister(register: Register): String = register match {
    case RAX(s) => prependSize(s, "AX", false)
    case RBX(s) => prependSize(s, "BX", false)
    case RCX(s) => prependSize(s, "CX", false)
    case RDX(s) => prependSize(s, "DX", false)
    case RSI(s) => prependSize(s, "SI")
    case RDI(s) => prependSize(s, "DI")
    case RSP(s) => prependSize(s, "SP")
    case RBP(s) => prependSize(s, "BP")
    case R8(s)  => appendSize(s, "R8")
    case R9(s)  => appendSize(s, "R9")
    case R10(s) => appendSize(s, "R10")
    case R11(s) => appendSize(s, "R11")
    case R12(s) => appendSize(s, "R12")
    case R13(s) => appendSize(s, "R13")
    case R14(s) => appendSize(s, "R14")
    case R15(s) => appendSize(s, "R15")
  }

  private def prependSize(size: Size, register: String, keepTail: Boolean = true): String = size match {
    case W8  => (if keepTail then register else register.slice(0, register.length - 1)) + "L"
    case W16 => register
    case W32 => "E" + register
    case W64 => "R" + register
  }

  private def appendSize(size: Size, register: String): String = size match {
    case W8  => register + "B"
    case W16 => register + "W"
    case W32 => register + "D"
    case W64 => register
  }
}
