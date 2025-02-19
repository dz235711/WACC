package wacc

import wacc.Size.*

import scala.collection.immutable.Map

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

    if (flagCtx.get("exit").getOrElse(false)) then
      addExit
    if (flagCtx.get("readi").getOrElse(false)) then
      addReadInt

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
    case Nop => ()
    case Mov(dest, src) =>
      val destStr = stringifyOperand(dest)
      val srcStr = stringifyOperand(src)
      stringCtx.add(s"mov $destStr, $srcStr")
    case Call("exit@plt") =>
      flagCtx.add("exit", true)
      stringCtx.add(s"call _exit")
    case Call("puts@plt") => stringCtx.add(s"call _println")
  }

  private def stringifyOperand(operand: Register | Pointer | Immediate | String): String = operand match {
    case n: Immediate => n.toString
    case RDI(W32)     => "edi"
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
