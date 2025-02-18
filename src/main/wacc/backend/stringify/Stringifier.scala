package wacc

import wacc.Size.*

class Stringifier {
  def stringify(instructions: List[Instruction]): String = {
    given stringCtx: MutableContext[String] = new MutableContext()

    stringCtx.add(".intel_syntax noprefix")
    stringCtx.add(".globl main")
    stringCtx.add(".section .rodata")
    stringCtx.add(".text")
    stringCtx.add("main:")

    instructions.foreach(stringifyInstr)

    stringCtx.add("ret")

    stringCtx.add("_exit:")
    stringCtx.add("push rbp")
    stringCtx.add("mov rbp, rsp")
    stringCtx.add("and rsp, -16")
    stringCtx.add("call exit@plt")
    stringCtx.add("mov rsp, rbp")
    stringCtx.add("pop rbp")
    stringCtx.add("ret")

    stringCtx.get
      .map(instr => {
        if (instr.startsWith(".") || instr.endsWith(":")) instr
        else " " * INDENTATION_SIZE + instr
      })
      .mkString("\n")
  }

  private def stringifyInstr(instr: Instruction)(using ctx: MutableContext[String]): Unit = instr match {
    case Nop => ()
    case Mov(dest, src) =>
      val destStr = stringifyOperand(dest)
      val srcStr = stringifyOperand(src)
      ctx.add(s"mov $destStr, $srcStr")
    case Call("exit@plt") => ctx.add(s"call _exit")
    case _                => ()
  }

  private def stringifyOperand(operand: Register | Pointer | Immediate | String): String = operand match {
    case n: Immediate => n.toString
    case RDI(W32)     => "edi"
  }

}
