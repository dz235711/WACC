package wacc

import wacc.Size.*
import wacc.Condition.*
import wacc.Register.*

type Operand = Register | Pointer | Immediate | Label | Option[Register] | String | Option[Immediate]

class x86Stringifier {

  /** Convert string literals and assembly IR to an x86-64 assembly string
   *
   * @param strings The list of string literal to label tuples
   * @param instructions The list of assembly IR instructions
   * @return The x86-64 assembly string
   */
  def stringify(strings: List[(String, Label)], instructions: List[Instruction]): String = {
    // TODO: Use string builder
    (List(
      NoPrefixSyntax(),
      GlobalMain(),
      SectionReadOnlyData()
    ) ++
      strings.flatMap((string, label) => {
        List(
          IntData(string.length),
          DefineLabel(label),
          Asciz(string)
        )
      }) ++
      List(
        Text(),
        DefineLabel(Label("main")),
        Push(BASE_POINTER)(W64),
        Mov(BASE_POINTER, STACK_POINTER)(W64)
      ) ++
      instructions)
      .map(instr => {
        // we first convert the instruction to a string
        val translated = stringifyLine(instr)

        // we then add indentation if the instruction is not a label
        if (translated.startsWith(".") || translated.endsWith(":")) translated
        else s"${" " * INDENTATION_SIZE}$translated"
      })
      .mkString("\n") ++ "\n"
  }

  private def stringifyLine(instr: Instruction): String = {
    instr match {
      case DefineLabel(Label(s)) => s"$s:"
      case Comment(s)            => s"# $s"
      case m @ Movzx(dest, src)  => s"movzx ${stringifyOperand(dest, m.destSize)}, ${stringifyOperand(src, m.size)}"
      case _ =>
        val operands: List[Operand] = instr.productIterator.toList.asInstanceOf[List[Operand]] // ew
        val size = instr.size

        stringifyInstr(instr) + " " + operands.map(stringifyOperand(_, size)).filterNot(_ == "").mkString(", ")
    }
  }

  private def stringifyJumpCondition(cond: Condition) = cond match {
    case NoCond       => "mp"
    case Equal        => "e"
    case NotEqual     => "ne"
    case Greater      => "g"
    case GreaterEqual => "ge"
    case Less         => "l"
    case LessEqual    => "le"
    case Zero         => "z"
    case NotZero      => "nz"
    case Carry        => "c"
    case NotCarry     => "nc"
    case Overflow     => "o"
    case NotOverflow  => "no"
    case Sign         => "s"
    case NotSign      => "ns"
    case Parity       => "p"
    case NotParity    => "np"
    case Above        => "a"
    case AboveEqual   => "ae"
    case Below        => "b"
    case BelowEqual   => "be"
  }

  /**
    * Converts an instruction into a string representation
    *
    * @param instr The instruction to convert to a string
    * @return a string representation of the instruction
    */
  private def stringifyInstr(
      instr: Instruction
  ): String = instr match {
    case Mov(_, _)               => s"mov"
    case Movzx(_, _)             => s"movzx"
    case Call(_)                 => s"call"
    case Ret(_)                  => s"ret"
    case Nop()                   => "nop"
    case Halt()                  => "hlt"
    case Push(_)                 => s"push"
    case Pop(_)                  => s"pop"
    case Lea(_, _)               => s"lea"
    case Cdq()                   => "cdq"
    case DefineLabel(_)          => s""
    case Jmp(cond, _)            => s"j${stringifyJumpCondition(cond)}"
    case And(_, _)               => s"and"
    case Or(_, _)                => s"or"
    case Xor(_, _)               => s"xor"
    case ShiftArithLeft(_, _)    => s"sal"
    case ShiftArithRight(_, _)   => s"sar"
    case ShiftLogicalLeft(_, _)  => s"shl"
    case ShiftLogicalRight(_, _) => s"shr"
    case Test(_, _)              => s"test"
    case Compare(_, _)           => s"cmp"
    case AddCarry(_, _)          => s"adc"
    case Add(_, _)               => s"add"
    case Dec(_)                  => s"dec"
    case Inc(_)                  => s"inc"
    case Div(_)                  => s"div"
    case SignedDiv(_)            => s"idiv"
    case Mul(_)                  => s"mul"
    case SignedMul(_, _, _)      => s"imul"
    case Neg(_)                  => s"neg"
    case Not(_)                  => s"not"
    case Sub(_, _)               => s"sub"
    case Comment(_)              => s"#"
    case NoPrefixSyntax()        => ".intel_syntax noprefix"
    case GlobalMain()            => ".globl main"
    case SectionReadOnlyData()   => ".section .rodata"
    case Text()                  => ".text"
    case IntData(_)              => s".int"
    case Asciz(_)                => s".asciz"
    case SetGreater(_)           => s"setg"
    case SetGreaterEqual(_)      => s"setge"
    case SetSmaller(_)           => s"setl"
    case SetSmallerEqual(_)      => s"setle"
    case SetEqual(_)             => s"sete"
  }

  /**
    * Converts an operand into a string representation
    *
    * @param operand The operand to convert
    * @return a string representation of the operand
    */
  private def stringifyOperand(operand: Operand, size: Size): String = operand match {
    case n: Immediate       => s"$n"
    case r: Register        => stringifyRegister(r, size)
    case p: Pointer         => stringifyPointer(p, size)
    case Label(s)           => s
    case Some(r: Register)  => stringifyRegister(r, size)
    case Some(n: Immediate) => s"$n"
    case s: String          => s"\"$s\""
    case _                  => ""
  }

  /**
    * Converts a pointer into a string representation
    *
    * @param pointer The pointer to stringify
    * @return a string representation of the pointer
    * @example `RegImm(Reg(RAX), Imm(4))(W64)` -> `qword ptr [rax+4]`
    */
  private def stringifyPointer(pointer: Pointer, size: Size): String =
    s"${ptrSize(size)} ${stringifyPointerArithmetic(pointer, size)}"

  /** Returns the sign symbol of an operand
   *
   * @param operand The operand to check
   * @return The sign symbol of the operand
   */
  private def signSymbol(operand: Immediate | Label): String = operand match {
    case n: Immediate if n < 0 => ""
    case _                     => "+"
  }

  /**
    * Converts pointer arithmetic into string representation (without a pointer size prefix)
    *
    * @param pointer The pointer to stringify
    * @return a string representation of the pointer arithmetic
    * @example `RegImm(Reg(RAX), Imm(4))(W64)` -> `[rax+4]`
    */
  private def stringifyPointerArithmetic(pointer: Pointer, size: Size): String = {
    val arithmetic = pointer match {
      case RegPointer(reg)           => s"${stringifyOperand(reg, W64)}"
      case RegImmPointer(reg, imm)   => s"${stringifyOperand(reg, W64)}${signSymbol(imm)}${stringifyOperand(imm, size)}"
      case RegRegPointer(reg1, reg2) => s"${stringifyOperand(reg1, W64)}+${stringifyOperand(reg2, W64)}"
      case RegScaleRegPointer(reg1, scale, reg2) =>
        s"${stringifyOperand(reg1, W64)}+${stringifyOperand(scale, size)}*${stringifyOperand(reg2, W64)}"
      case RegScaleRegImmPointer(reg1, scale, reg2, imm) =>
        s"${stringifyOperand(reg1, W64)}+${stringifyOperand(scale, size)}*${stringifyOperand(reg2, W64)}+${stringifyOperand(imm, size)}"
      case ScaleRegImmPointer(scale, reg, imm) =>
        s"${stringifyOperand(scale, size)}*${stringifyOperand(reg, W64)}+${stringifyOperand(imm, size)}"
    }

    s"[$arithmetic]"
  }

  /**
    * Converts a size into a string representation
    *
    * @param size The Size to convert
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
    * @param register The register to stringify
    * @return a string representation of the register
    */
  private def stringifyRegister(register: Register, size: Size): String = register match {
    case RIP => "rip"
    case RAX => prependSize(size, "ax", false)
    case RBX => prependSize(size, "bx", false)
    case RCX => prependSize(size, "cx", false)
    case RDX => prependSize(size, "dx", false)
    case RSI => prependSize(size, "si")
    case RDI => prependSize(size, "di")
    case RSP => prependSize(size, "sp")
    case RBP => prependSize(size, "bp")
    case R8  => appendSize(size, "r8")
    case R9  => appendSize(size, "r9")
    case R10 => appendSize(size, "r10")
    case R11 => appendSize(size, "r11")
    case R12 => appendSize(size, "r12")
    case R13 => appendSize(size, "r13")
    case R14 => appendSize(size, "r14")
    case R15 => appendSize(size, "r15")
  }

  /**
    * Prepends a size to a register
    *
    * @param size The size to prepend
    * @param register The register onto which the size should be prepended
    * @param keepTail Whether the tail of the size register should be kept (defaults to true)
    * @return the register with the size prepended
    */
  private def prependSize(size: Size, register: String, keepTail: Boolean = true): String = size match {
    /* Some registers, like RAX converts to AL whilst ones like RSI converts to SIL and therefore for RSI we keep the
     * tail ('I') whilst we chop the 'X' off RAX */
    case W8  => s"${if keepTail then register else register.slice(0, register.length - 1)}l"
    case W16 => register
    case W32 => s"e$register"
    case W64 => s"r$register"
  }

  /**
    * Appends a size to a register
    *
    * @param size The size to append
    * @param register The register onto which the size is appended
    * @return the register with the size appended
    */
  private def appendSize(size: Size, register: String): String = size match {
    case W8  => s"${register}b"
    case W16 => s"${register}w"
    case W32 => s"${register}d"
    case W64 => register
  }
}
