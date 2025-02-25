package wacc

import wacc.Size.*

type Operand = Register | Pointer | Immediate | String

class x86Stringifier {

  /**
    * Converts a list of instructions into a string representation
    *
    * @param instructions
    * @return a string representation of the instructions
    */
  def stringify(instructions: List[Instruction]): String = {
    instructions
      .map(instr => {
        // we first convert the instruction to a string
        val translated = stringifyInstr(instr)

        // we then add indentation if the instruction is not a label
        if (translated.startsWith(".") || translated.endsWith(":")) translated
        else s"${" " * INDENTATION_SIZE}translated"
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
    case Neg(dest)           => s"neg ${stringifyOperand(dest)}"
    case Not(dest)           => s"not ${stringifyOperand(dest)}"
    case Sub(dest, src)      => s"sub ${stringifyOperand(dest)}, ${stringifyOperand(src)}"
    case Comment(comment)    => s"# $comment"
    case NoPrefixSyntax      => ".intel_syntax noprefix"
    case GlobalMain          => ".globl main"
    case SectionReadOnlyData => ".section .rodata"
    case Text                => ".text"
    case IntData(value)      => s".int $value"
    case Asciz(string)       => s".asciz $string"
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
    */
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

object Stringifier {

  /**
   * Creates a list of assembly instructions to define a string
   * 
   * @param label The label of the string constant
   * @param string The string to define
   * @return A list of assembly instructions that defines the string constant
   */
  private def createString(label: Label, string: String): List[Instruction] = List(
    IntData(string.length),
    DefineLabel(label),
    Asciz(string),
    Text
  )

  /**
    * Creates a list of assembly instructions to define a read only string
    * 
    * @param label The label of the string constant
    * @param string The string to define
    * @return A list of assembly instructions that defines the read only string constant
    */
  private def createReadOnlyString(label: Label, string: String): List[Instruction] =
    SectionReadOnlyData :: createString(label, string)

  /**
    * Creates a list of assembly instructions to define a function
    * 
    * @param label The label of the function
    * @param body The body of the function
    * @return A list of assembly instructions that defines the function, including the stack frame setup and teardown
    */
  private def createFunction(label: Label, body: List[Instruction]): List[Instruction] = List(
    DefineLabel(label),
    Push(RBP(W64)),
    Mov(RBP(W64), RSP(W64))
  ) ::: body ::: List(
    Mov(RSP(W64), RBP(W64)),
    Pop(RBP(W64)),
    Ret(None)
  )

  // C library functions
  private val ClibExit = "exit@plt"
  private val ClibFlush = "fflush@plt"
  private val ClibFree = "free@plt"
  private val ClibMalloc = "malloc@plt"
  private val ClibPrintf = "printf@plt"
  private val ClibPuts = "puts@plt"
  private val ClibScanf = "scanf@plt"

  private val IntFormatLabel = ".intFormat"
  private val IntFormatSpecifier = "%d"

  private val CharacterFormatLabel = ".charFormat"
  private val CharacterFormatSpecifier = "%c"

  private val StringFormatLabel = ".stringFormat"
  private val StringFormatSpecifier = "%.*s"

  private val PointerFormatLabel = ".pointerFormat"
  private val PointerFormatSpecifier = "%p"

  /** Subroutine for printing an integer. */
  private val _printi = createReadOnlyString(IntFormatLabel, IntFormatSpecifier) ::: createFunction(
    "_printi",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Mov(RSI(W32), RDI(W32)),
      Lea(RDI(W64), RegImmPointer(RIP, IntFormatLabel)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibPrintf),
      Mov(RDI(W64), 0),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a character. */
  private val _printc = createReadOnlyString(CharacterFormatLabel, CharacterFormatSpecifier) ::: createFunction(
    "_printc",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Mov(RSI(W8), RDI(W8)),
      Lea(RDI(W64), RegImmPointer(RIP, CharacterFormatLabel)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibPrintf),
      Mov(RDI(W64), 0),
      Call(ClibFlush),
      Mov(RSP(W64), RBP(W64))
    )
  )

  private val printsLabel = "_prints"

  /** Subroutine for printing a string. */
  private val _prints = createReadOnlyString(StringFormatLabel, StringFormatSpecifier) ::: createFunction(
    printsLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Mov(RSI(W32), RegImmPointer(RDI(W64), -4)(W32)),
      Lea(RDI(W64), RegImmPointer(RIP, StringFormatLabel)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibPrintf),
      Mov(RDI(W64), 0),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a pair or an array. */
  private val _printp = createReadOnlyString(PointerFormatLabel, PointerFormatSpecifier) ::: createFunction(
    "_printp",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Mov(RSI(W64), RDI(W64)),
      Lea(RDI(W64), RegImmPointer(RIP, PointerFormatSpecifier)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibPrintf),
      Mov(RDI(W64), 0),
      Call(ClibFlush)
    )
  )

  private val printlnStrLabel = ".printlnStr"
  private val printlnStr = ""

  /** Subroutine for printing a newline. */
  private val _println = createReadOnlyString(printlnStrLabel, printlnStr) ::: createFunction(
    "_println",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Lea(RDI(W64), RegImmPointer(RIP, printlnStrLabel)(W64)),
      Call(ClibPuts),
      Mov(RDI(W64), 0),
      Call(ClibFlush)
    )
  )

  private val IntReadLabel = ".intRead"
  private val IntReadSpecifier = "%d"

  private val CharacterReadLabel = ".charRead"
  private val CharacterReadSpecifier = " %c"

  /** Subroutine for reading an integer. */
  private val _readi = createReadOnlyString(IntReadLabel, IntReadSpecifier) ::: createFunction(
    "_readi",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Comment("Allocate space on the stack to store the read value"),
      Sub(RSP(W64), 16),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(RSP(W64))(W32), RDI(W32)),
      Lea(RSI(W64), RegPointer(RSP(W64))(W64)),
      Lea(RDI(W64), RegImmPointer(RIP, IntReadLabel)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibScanf),
      Mov(RAX(W32), RegPointer(RSP(W64))(W32)),
      Add(RSP(W64), 16)
    )
  )

  /** Subroutine for reading an character. */
  private val _readc = createReadOnlyString(CharacterReadLabel, CharacterReadSpecifier) ::: createFunction(
    "_readc",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Comment("Allocate space on the stack to store the read value"),
      Sub(RSP(W64), 16),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(RSP(W64))(W8), RDI(W8)),
      Lea(RSI(W64), RegPointer(RSP(W64))(W64)),
      Lea(RDI(W64), RegImmPointer(RIP, CharacterReadLabel)(W64)),
      Mov(RAX(W8), 0),
      Call(ClibScanf),
      Mov(RAX(W8), RegPointer(RSP(W64))(W8)),
      Add(RSP(W64), 16)
    )
  )

  /** Subroutine for exiting the program. */
  private val _exit = createFunction(
    "_exit",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Call(ClibExit)
    )
  )

  /** Subroutine for allocating memory. Used for pairs and arrays. */
  private val _malloc = createFunction(
    "_malloc",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Call(ClibMalloc),
      Compare(RAX(W64), 0),
      JmpEqual(outOfMemoryLabel)
    )
  )

  private val OutOfMemoryStringLabel = ".outOfMemoryString"
  private val OutOfMemoryString = "fatal error: out of memory\n"

  private val outOfMemoryLabel = "_outOfMemory"

  /** Subroutine for an out of memory error. */
  private val _outOfMemory = createReadOnlyString(OutOfMemoryStringLabel, OutOfMemoryString) ::: List(
    DefineLabel(outOfMemoryLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(RSP(W64), -16),
    Lea(RDI(W64), RegImmPointer(RIP, OutOfMemoryStringLabel)(W64)),
    Call(printsLabel),
    Mov(RDI(W8), -1),
    Call(ClibExit),
    Ret(None)
  )

  /** Subroutine for freeing array memory on the heap. */
  private val _free = createFunction(
    "_free",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Call(ClibFree)
    )
  )

  /** Subroutine for freeing pair memory on the heap. */
  private val _freepair = createFunction(
    "_freepair",
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(RSP(W64), -16),
      Compare(RDI(W64), 0),
      JmpEqual(errNullLabel),
      Call(ClibFree)
    )
  )

  private val NullPairStringLabel = ".nullPairString"
  private val NullPairString = "fatal error: null pair dereferenced or freed\n"

  private val errNullLabel = "_errNull"

  /** Subroutine for a null pair error. */
  private val _errNull = createReadOnlyString(NullPairStringLabel, NullPairString) ::: List(
    DefineLabel(errNullLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(RSP(W64), -16),
    Lea(RDI(W64), RegImmPointer(RIP, NullPairStringLabel)(W64)),
    Call(printsLabel),
    Mov(RDI(W8), -1),
    Call(ClibExit)
  )
}
