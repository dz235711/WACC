package wacc

import wacc.Size.*

class Stringifier {
  def stringify(instructions: List[Instruction]): String = {
    given stringCtx: StringifyContext = new StringifyContext

    stringCtx.addInstr(".intel_syntax noprefix")
    stringCtx.addInstr(".globl main")
    stringCtx.addInstr(".section .rodata")
    stringCtx.addInstr(".text")
    stringCtx.addInstr("main:")

    instructions.foreach(stringifyInstr)

    stringCtx.addInstr("ret")

    stringCtx.addInstr("_exit:")
    stringCtx.addInstr("push rbp")
    stringCtx.addInstr("mov rbp, rsp")
    stringCtx.addInstr("and rsp, -16")
    stringCtx.addInstr("call exit@plt")
    stringCtx.addInstr("mov rsp, rbp")
    stringCtx.addInstr("pop rbp")
    stringCtx.addInstr("ret")

    stringCtx.get
  }

  private def stringifyInstr(instr: Instruction)(using ctx: StringifyContext): Unit = instr match {
    case Nop => ()
    case Mov(dest, src) =>
      val destStr = stringifyOperand(dest)
      val srcStr = stringifyOperand(src)
      ctx.addInstr(s"mov $destStr, $srcStr")
    case Call("exit@plt") => ctx.addInstr(s"call _exit")
    case _                => ()
  }

  private def stringifyOperand(operand: Register | Pointer | Immediate | String): String = operand match {
    case n: Immediate => n.toString
    case RDI(W32)     => "edi"
  }

}
