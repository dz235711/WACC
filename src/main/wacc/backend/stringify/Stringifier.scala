package wacc

import wacc.Size.*

type Operand = Register | Pointer | Immediate | Label

class x86Stringifier {

  /** Convert string literals and assembly IR to an x86-64 assembly string
   *
   * @param strings The list of label to string literal tuples
   * @param instructions The list of assembly IR instructions
   * @return The x86-64 assembly string
   */
  def stringify(strings: List[(Label, String)], instructions: List[Instruction]): String = {
    // TODO: Use string builder
    (List(
      NoPrefixSyntax,
      GlobalMain,
      SectionReadOnlyData
    ) ++
      strings.flatMap((string, label) => {
        List(
          Comment(s"String literal $label is $string"),
          IntData(string.length),
          DefineLabel(label),
          Asciz(string)
        )
      }) ++
      List(
        Text,
        DefineLabel("main"),
        Push(RBP(W64)),
        Mov(RBP(W64), RSP(W64))
      ) ++
      instructions)
      .map(instr => {
        // we first convert the instruction to a string
        val translated = stringifyInstr(instr)

        // we then add indentation if the instruction is not a label
        if (translated.startsWith(".") || translated.endsWith(":")) translated
        else s"${" " * INDENTATION_SIZE}$translated"
      })
      .mkString("\n")
  }

  /**
    * Converts an instruction into a string representation
    *
    * @param instr
    * @return a string representation of the instruction
    */
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
    case Lea(dest, src)                 => s"lea ${stringifyOperand(dest)}, ${{ stringifyPointerArithmetic(src) }}"
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
    case JmpCarry(label)                => s"jc ${stringifyOperand(label)}"
    case JmpNotCarry(label)             => s"jnc ${stringifyOperand(label)}"
    case JmpOverflow(label)             => s"jo ${stringifyOperand(label)}"
    case JmpNotOverflow(label)          => s"jno ${stringifyOperand(label)}"
    case JmpSign(label)                 => s"js ${stringifyOperand(label)}"
    case JmpNotSign(label)              => s"jns ${stringifyOperand(label)}"
    case JmpParity(label)               => s"jp ${stringifyOperand(label)}"
    case JmpNotParity(label)            => s"jnp ${stringifyOperand(label)}"
    case JmpAbove(label)                => s"ja ${stringifyOperand(label)}"
    case JmpAboveEqual(label)           => s"jae ${stringifyOperand(label)}"
    case JmpBelow(label)                => s"jb ${stringifyOperand(label)}"
    case JmpBelowEqual(label)           => s"jbe ${stringifyOperand(label)}"
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
    case Neg(dest)             => s"neg ${stringifyOperand(dest)}"
    case Not(dest)             => s"not ${stringifyOperand(dest)}"
    case Sub(dest, src)        => s"sub ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Comment(comment)      => s"# $comment"
    case NoPrefixSyntax        => ".intel_syntax noprefix"
    case GlobalMain            => ".globl main"
    case SectionReadOnlyData   => ".section .rodata"
    case Text                  => ".text"
    case IntData(value)        => s".int $value"
    case Asciz(string)         => s".asciz \"$string\""
    case SetGreater(dest)      => s"setg ${stringifyOperand(dest)}"
    case SetGreaterEqual(dest) => s"setge ${stringifyOperand(dest)}"
    case SetSmaller(dest)      => s"setl ${stringifyOperand(dest)}"
    case SetSmallerEqual(dest) => s"setle ${stringifyOperand(dest)}"
    case SetEqual(dest)        => s"sete ${stringifyOperand(dest)}"
  }

  /**
    * Converts an operand into a string representation iff it is defined
    *
    * @param operand
    * @param prefix
    * @param postfix
    * @return a string representation of the operand
    */
  private def stringifyOperand(
      operand: Option[Operand],
      prefix: String = "",
      postfix: String = ""
  ): String = operand match {
    case Some(operand) => s"$prefix${stringifyOperand(operand)}$postfix"
    case None          => ""
  }

  /**
    * Converts an operand into a string representation
    *
    * @param operand
    * @param prefix
    * @param postfix
    * @return a string representation of the operand
    */
  private def stringifyOperand(operand: Operand): String = operand match {
    case n: Immediate => s"$n"
    case r: Register  => stringifyRegister(r)
    case p: Pointer   => stringifyPointer(p)
    case s: String    => s
  }

  /**
    * Converts a pointer into a string representation
    *
    * @param pointer
    * @return a string representation of the pointer
    * @example `RegImm(Reg(RAX), Imm(4))(W64)` -> `qword ptr [rax+4]`
    */
  private def stringifyPointer(pointer: Pointer): String =
    s"${ptrSize(pointer.size)} ${stringifyPointerArithmetic(pointer)}"

  /** Returns the sign symbol of an operand
   *
   * @param operand The operand to check
   * @return The sign symbol of the operand
   */
  def signSymbol(operand: Immediate | Label): String = operand match {
    case n: Immediate if n < 0 => ""
    case _                     => "+"
  }

  /**
    * Converts pointer arithmetic into string representation (without a pointer size prefix)
    *
    * @param pointer
    * @return a string representation of the pointer arithmetic
    * @example `RegImm(Reg(RAX), Imm(4))(W64)` -> `[rax+4]`
    */
  private def stringifyPointerArithmetic(pointer: Pointer): String = {
    val arithmetic = pointer match {
      case RegPointer(reg)           => s"${stringifyOperand(reg)}"
      case RegImmPointer(reg, imm)   => s"${stringifyOperand(reg)}${signSymbol(imm)}${stringifyOperand(imm)}"
      case RegRegPointer(reg1, reg2) => s"${stringifyOperand(reg1)}+${stringifyOperand(reg2)}"
      case RegScaleRegPointer(reg1, scale, reg2) =>
        s"${stringifyOperand(reg1)}+${stringifyOperand(scale)}*${stringifyOperand(reg2)}"
      case RegScaleRegImmPointer(reg1, scale, reg2, imm) =>
        s"${stringifyOperand(reg1)}+${stringifyOperand(scale)}*${stringifyOperand(reg2)}+${stringifyOperand(imm)}"
      case ScaleRegImmPointer(scale, reg, imm) =>
        s"${stringifyOperand(scale)}*${stringifyOperand(reg)}+${stringifyOperand(imm)}"
    }

    s"[$arithmetic]"
  }

  /**
    * Converts a size into a string representation
    *
    * @param size
    * @return a string representation of the size as a ptr
    */
  private def ptrSize(size: Size): String = s"${size match {
      case W8  => "byte"
      case W16 => "word"
      case W32 => "dword"
      case W64 => "qword"
    }} ptr"

  /**
    * Converts a register into a string representation
    *
    * @param register
    * @return a string representation of the register
    */
  private def stringifyRegister(register: Register): String = register match {
    case RIP    => "rip"
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

  /**
    * Prepends a size to a register
    *
    * @param size
    * @param register
    * @param keepTail
    * @return the register with the size prepended
    */
  private def prependSize(size: Size, register: String, keepTail: Boolean = true): String = size match {
    /* Some registers, like RAX converts to AL whilst ones like RSI converts to SIL and therefore for RSI we keep the
     * tail ('I') whilst we chop the 'X' off RAX */
    case W8  => s"${(if keepTail then register else register.slice(0, register.length - 1))}l"
    case W16 => register
    case W32 => s"e$register"
    case W64 => s"r$register"
  }

  /**
    * Appends a size to a register
    *
    * @param size
    * @param register
    * @return the register with the size appended
    */
  private def appendSize(size: Size, register: String): String = size match {
    case W8  => s"${register}b"
    case W16 => s"${register}w"
    case W32 => s"${register}d"
    case W64 => register
  }
}
