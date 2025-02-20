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
    case Call("scanf@plt") =>
      flagCtx.add("readi", true)
      stringCtx.add(s"call _readi")
  }

  private def stringifyOperand(operand: Register | Pointer | Immediate | String): String = operand match {
    case n: Immediate => n.toString
    case r: Register  => stringifyRegister(r)
    case s: String    => s
  }

  def stringifyRegister(register: Register): String = register match {
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

  private def prependSize(size: Size, reg: String, keepTail: Boolean = true): String = size match {
    case W8  => (if keepTail then reg else reg.slice(0, 1)) + "L"
    case W16 => reg
    case W32 => "E" + reg
    case W64 => "R" + reg
  }

  private def appendSize(size: Size, reg: String): String = size match {
    case W8  => reg + "B"
    case W16 => reg + "W"
    case W32 => reg + "D"
    case W64 => reg
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
