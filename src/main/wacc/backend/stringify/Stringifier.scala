package wacc

import wacc.Size.*

class Stringifier {
  def stringify(instructions: List[Instruction]): String = {
    given stringCtx: ListContext[String] = new ListContext()
    given flagCtx: MapContext[String, Boolean] = new MapContext()

    stringCtx.add(".intel_syntax noprefix")
    stringCtx.add(".globl main")
    stringCtx.add(".section .rodata")
    stringCtx.add(".text")
    stringCtx.add("main:")

    instructions.foreach(stringifyInstr)

    stringCtx.add("ret")

    if (flagCtx.get("exit").getOrElse(false)) then addExit
    if (flagCtx.get("readi").getOrElse(false)) then addReadInt

    stringCtx.get
      .map(instr => {
        if (instr.startsWith(".") || instr.endsWith(":")) instr
        else " " * INDENTATION_SIZE + instr
      })
      .mkString("\n")
  }

  private def stringifyInstr(
      instr: Instruction
  )(using stringCtx: ListContext[String], flagCtx: MapContext[String, Boolean]): Unit = instr match {
    case Mov(dest, src) =>
      val destStr = stringifyOperand(dest)
      val srcStr = stringifyOperand(src)
      stringCtx.add(s"mov $destStr, $srcStr")
    case Call("exit@plt") =>
      flagCtx.add("exit", true)
      stringCtx.add(s"call _exit")
    case Call("scanf@plt") =>
      flagCtx.add("readi", true)
      stringCtx.add(s"call _readi")
    case Call(label) => stringCtx.add(s"call $label")
    case Ret(imm) => stringCtx.add(s"ret${ifDefined(imm, prefix = " ")}")
    case Nop => stringCtx.add("nop")
    case Halt => stringCtx.add("hlt")
    case Push(src)  => stringCtx.add(s"push ${stringifyOperand(src)}")
    case Pop(dest)  => stringCtx.add(s"pop ${stringifyOperand(dest)}")
    case Lea(dest, src) => stringCtx.add(s"lea ${stringifyRegister(dest)}, ${stringifyPointer(src)}")
    case DefineLabel(label) => stringCtx.add(s"$label:")
    case Jmp(label) => stringCtx.add(s"jmp $label")
    case JmpEqual(label) => stringCtx.add(s"je $label")
    case JmpNotEqual(label) => stringCtx.add(s"jne $label")
    case JmpGreater(label) => stringCtx.add(s"jg $label")
    case JmpGreaterEqual(label) => stringCtx.add(s"jge $label")
    case JmpLess(label) => stringCtx.add(s"jl $label")
    case JmpLessEqual(label) => stringCtx.add(s"jle $label")
    case JmpZero(label) => stringCtx.add(s"jz $label")
    case JmpNotZero(label) => stringCtx.add(s"jnz $label")
    case JumpCarry(label) => stringCtx.add(s"jc $label")
    case JumpNotCarry(label) => stringCtx.add(s"jnc $label")
    case JumpOverflow(label) => stringCtx.add(s"jo $label")
    case JumpNotOverflow(label) => stringCtx.add(s"jno $label")
    case JumpSign(label) => stringCtx.add(s"js $label")
    case JumpNotSign(label) => stringCtx.add(s"jns $label")
    case JumpParity(label) => stringCtx.add(s"jp $label")
    case JumpNotParity(label) => stringCtx.add(s"jnp $label")
    case JumpAbove(label) => stringCtx.add(s"ja $label")
    case JumpAboveEqual(label) => stringCtx.add(s"jae $label")
    case JumpBelow(label) => stringCtx.add(s"jb $label")
    case JumpBelowEqual(label) => stringCtx.add(s"jbe $label")
    case And(dest, src) => stringCtx.add(s"and ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case Or(dest, src) => stringCtx.add(s"or ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case Xor(dest, src) => stringCtx.add(s"xor ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case ShiftArithLeft(dest, count) => stringCtx.add(s"sal ${stringifyOperand(dest)}, $count")
    case ShiftArithRight(dest, count) => stringCtx.add(s"sar ${stringifyOperand(dest)}, $count")
    case ShiftLogicalLeft(dest, count) => stringCtx.add(s"shl ${stringifyOperand(dest)}, $count")
    case ShiftLogicalRight(dest, count) => stringCtx.add(s"shr ${stringifyOperand(dest)}, $count")
    case Test(src1, src2) => stringCtx.add(s"test ${stringifyOperand(src1)}, ${stringifyOperand(src2)}")
    case Compare(dest, src) => stringCtx.add(s"cmp ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case AddCarry(dest, src) => stringCtx.add(s"adc ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case Add(dest, src) => stringCtx.add(s"add ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case Dec(dest) => stringCtx.add(s"dec ${stringifyOperand(dest)}")
    case Inc(dest) => stringCtx.add(s"inc ${stringifyOperand(dest)}")
    case Div(src) => stringCtx.add(s"div ${stringifyOperand(src)}")
    case SignedDiv(src) => stringCtx.add(s"idiv ${stringifyOperand(src)}")
    case Mul(src) => stringCtx.add(s"mul ${stringifyOperand(src)}")
    case SignedMul(dest, src1, src2) => stringCtx.add(s"imul ${ifDefined(dest, postfix = ", ")}${stringifyOperand(src1)}${ifDefined(src2, prefix = ", ")}")
    case Neg(dest) => stringCtx.add(s"neg ${stringifyOperand(dest)}")
    case Not(dest) => stringCtx.add(s"not ${stringifyOperand(dest)}")
    case Sub(dest, src) => stringCtx.add(s"sub ${stringifyOperand(dest)}, ${stringifyOperand(src)}")
    case Comment(comment) => stringCtx.add(s"# $comment")
  }

  private def ifDefined(operand: Option[Register | Pointer | Immediate | String], prefix: String = "", postfix: String = ""): String = operand match {
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

  private def addExit(using ctx: ListContext[String]): Unit = {
    ctx.add("_exit:")
    ctx.add("push rbp")
    ctx.add("mov rbp, rsp")
    ctx.add("and rsp, -16")
    ctx.add("call exit@plt")
    ctx.add("mov rsp, rbp")
    ctx.add("pop rbp")
    ctx.add("ret")
  }

  private def addReadInt(using ctx: ListContext[String]): Unit = {
    ctx.add(".section .rodata")
    ctx.add(".int 2")
    ctx.add(".L._readi_str0:")
    ctx.add(".asciz \"%d\"")
    ctx.add(".text")

    ctx.add("_readi:")
    ctx.add("push rbp")
    ctx.add("mov rbp, rsp")
    ctx.add("and rsp, -16")
    ctx.add("sub rsp, 16")
    ctx.add("mov dword ptr [rsp], edi")
    ctx.add("lea rsi, qword ptr [rsp]")
    ctx.add("lea rdi, [rip + .L._readi_str0]")
    ctx.add("mov al, 0")
    ctx.add("call scanf@plt")
    ctx.add("mov eax, dword ptr [rsp]")
    ctx.add("add rsp, 16")
    ctx.add("mov rsp, rbp")
    ctx.add("pop rbp")
    ctx.add("ret")
  }

}
