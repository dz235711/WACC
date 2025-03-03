package wacc

import TypedAST.{
  Add as TypedAdd,
  And as TypedAnd,
  Call as TypedCall,
  Not as TypedNot,
  Or as TypedOr,
  Sub as TypedSub,
  Greater as TypedGreater,
  *
}
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{?, KnownType, SemType}
import wacc.Size.*
import wacc.Condition.*

import java.rmi.UnexpectedException
import scala.collection.mutable
import java.util.regex.Pattern

type HeapLValue = Fst | Snd | ArrayElem

case class Label(label: String)

// Agreement: a translate function will:
// 1. put its result in the next available location at the time of its invocation
// 2. unreserve any locations it reserves

sealed class InstructionContext {
  private val instructionCtx = new ListContext[Instruction]()
  private val stringCtx = new ListContext[(String, Label)]()
  private var stringCounter = 0

  private var whileLoopCounter = 0

  /** Stores library functions in a set to prevent duplicates. */
  private val libFunctions: mutable.Set[List[Instruction]] = mutable.Set()

  /** Get the next string label
   *
   * @return The next string label
   */
  def getStringLabel: Label =
    val toReturn = Label(s".L.str$stringCounter")
    stringCounter += 1
    toReturn

  def getWhileLoopId: Int =
    whileLoopCounter += 1
    whileLoopCounter

  /** Get the the strings and list of instructions
   * 
   * @return A tuple of the string-label tuple list and the list of instructions
   */
  def get: (List[(String, Label)], List[Instruction]) = (stringCtx.get, instructionCtx.get)

  /** Add an instruction to the list of instructions
   * 
   * @param instruction The instruction to add
   */
  def addInstruction(instruction: Instruction): Unit = instructionCtx.add(instruction)

  /** Add a string to the list of strings
   * 
   * @param string The string to add
   */
  def addString(string: String, label: Label): Unit =
    val p = Pattern.compile("\"|\'|\\\\")
    val m = p.matcher(string)
    val sb = new StringBuffer()
    while (m.find()) {
      m.group() match {
        case "\\" => m.appendReplacement(sb, "\\\\")
        case "\"" => m.appendReplacement(sb, "\\\\\"")
        case "\'" => m.appendReplacement(sb, "\\\\\'")
      }
    }
    m.appendTail(sb)
    stringCtx.add(sb.toString(), label)

  /** Add a library function to the list of instructions
    *
    * @param funcBody The body of the library function to be added
    */
  def addLibraryFunction(funcLabel: Label): Unit = libFunctions += Clib.labelToFunc(funcLabel)

  def getLibraryFunctions: Set[List[Instruction]] = libFunctions.toSet
}

class Translator {

  // Constants
  /** The size of a pair in bytes */
  private val PAIR_SIZE = 16

  /** The size of a pointer */
  private val POINTER_SIZE = W64

  /** The size of an integer in bytes */
  private val INT_SIZE = 4

  /** The value of NULL */
  private val NULL = 0

  /** The value of TRUE */
  private val TRUE = 1

  /** The value of FALSE */
  private val FALSE = 0

  /** The minimum value of a char */
  private val MIN_CHAR = 0

  /** The maximum value of a char */
  private val MAX_CHAR = 127

  /** The minimum value for an array to be indexed */
  private val MIN_ARR_SIZE = 0

  /** The label for a user-defined function */
  private val FUNCTION_LABEL = "wacc_func_"

  /** The number of if statements so each jump label is unique */
  private var ifCounter = 0

  def translate(program: Program): (List[(String, Label)], List[Instruction]) = {
    given translateCtx: InstructionContext = new InstructionContext()
    given locationCtx: LocationContext = new LocationContext()

    // Translate the program body
    translateStmt(program.body)

    // Return 0 from main body
    translateCtx.addInstruction(Mov(RETURN(W64), 0))
    translateCtx.addInstruction(Pop(BASE_POINTER(W64)))
    translateCtx.addInstruction(Ret(None))

    // Translate all functions in the program
    program.fs.foreach { f => translateFunction(f) }

    // Add the library functions to the instruction context
    translateCtx.getLibraryFunctions.foreach { instrs => instrs.foreach(translateCtx.addInstruction) }

    translateCtx.get
  }

  /** Convert a semantic type to a size
   *
   * @param t The semantic type to convert
   * @return The size of the semantic type
   */
  private def typeToSize(t: SemType): Size =
    t match {
      case IntType        => W32
      case BoolType       => W8
      case CharType       => W8
      case StringType     => W64
      case ArrayType(_)   => W64
      case PairType(_, _) => W64
      // In the case of an unkown type, we know that we are in a pair element,
      // so we can just move the whole 8 bytes.
      case ? => W64
    }

  /** Generates a function name from an id
   * 
   * @param id The id (integer) to generate the function name
   * @return The function name
   */
  private def getFunctionName(id: Int): Label = Label(s"$FUNCTION_LABEL$id")

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   */
  private def translateStmt(
      stmt: Stmt
  )(using instructionCtx: InstructionContext, locationCtx: LocationContext): Unit = stmt match {
    case Skip => instructionCtx.addInstruction(Nop)

    case Decl(v, r) =>
      translateRValue(r)
      locationCtx.addLocation(v, typeToSize(v.getType))

    case Asgn(l, r) =>
      translateRValue(r)
      val resultLoc = locationCtx.reserveNext(typeToSize(l.getType))

      l match {
        case id: Ident =>
          val dest = locationCtx.getLocation(id)
          locationCtx.movLocLoc(dest, resultLoc)

        case h: HeapLValue =>
          val hDest = getHeapLocation(h)
          locationCtx.regInstr2(
            hDest,
            resultLoc,
            { (reg1, reg2) => Mov(RegPointer(reg1(POINTER_SIZE))(typeToSize(l.getType)), reg2(typeToSize(l.getType))) }
          )
      }

    case Read(id: Ident) =>
      // Move the original value to RDI in case the read fails
      locationCtx.setUpCall(List(locationCtx.getLocation(id)))
      // Fetch the correct read label
      val readLabel = id.getType match {
        case IntType => {
          instructionCtx.addLibraryFunction(Clib.readiLabel)
          Clib.readiLabel
        }
        case CharType => {
          instructionCtx.addLibraryFunction(Clib.readcLabel)
          Clib.readcLabel
        }
        case _ => throw new RuntimeException("Unexpected Error: Invalid type for read")
      }
      // Call the read function
      instructionCtx.addInstruction(Call(readLabel))
      val resultLoc = locationCtx.cleanUpCall(Some(typeToSize(id.getType)))
      val idDest = locationCtx.getLocation(id)
      locationCtx.movLocLoc(idDest, resultLoc)

    case Read(h: HeapLValue) =>
      val readParamLoc = locationCtx.getNext(typeToSize(h.getType))
      val pointerLoc = getHeapLocation(h)
      locationCtx.regInstr2(
        readParamLoc,
        pointerLoc,
        { (reg1, reg2) => Mov(reg1(typeToSize(h.getType)), RegPointer(reg2(POINTER_SIZE))(typeToSize(h.getType))) }
      )

      // Move the original value to RDI in case the read fails
      locationCtx.setUpCall(List(readParamLoc))
      // Fetch the correct read label
      // TODO: Factor out duplication
      val readLabel = h.getType match {
        case IntType => {
          instructionCtx.addLibraryFunction(Clib.readiLabel)
          Clib.readiLabel
        }
        case CharType => {
          instructionCtx.addLibraryFunction(Clib.readcLabel)
          Clib.readcLabel
        }
        case _ => throw new RuntimeException("Unexpected Error: Invalid type for read")
      }
      // Call the read function
      instructionCtx.addInstruction(Call(readLabel))

      // Clean up and save the result.
      val resultLoc = locationCtx.cleanUpCall(Some(typeToSize(h.getType)))

      // Move the result into the original location.
      locationCtx.regInstr2(
        pointerLoc,
        resultLoc,
        { (reg1, reg2) => Mov(RegPointer(reg1(POINTER_SIZE))(typeToSize(h.getType)), reg2(typeToSize(h.getType))) }
      )

    case Free(e) =>
      val freeLabel = e.getType match {
        case PairType(_, _) => {
          instructionCtx.addLibraryFunction(Clib.freepairLabel)
          instructionCtx.addLibraryFunction(Clib.printsLabel)
          instructionCtx.addLibraryFunction(Clib.errNullLabel)
          Clib.freepairLabel
        }
        case ArrayType(_) => {
          instructionCtx.addLibraryFunction(Clib.freeLabel)
          Clib.freeLabel
        }
        case _ => throw new UnexpectedException("Invalid type")
      }

      // Check for null
      unary(
        e,
        { l =>
          // Check for runtime error
          instructionCtx.addLibraryFunction(Clib.printsLabel)
          instructionCtx.addLibraryFunction(Clib.errNullLabel)
          locationCtx.regInstr1(l, { reg => Compare(reg(typeToSize(e.getType)), NULL) })
          instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

          // Call free
          locationCtx.setUpCall(List(l))
          instructionCtx.addInstruction(Call(freeLabel))
        }
      )

      locationCtx.cleanUpCall(None)

    case Return(e) =>
      unary(e, locationCtx.cleanUpFunc)
      instructionCtx.addInstruction(Ret(None))

    case Exit(e) =>
      instructionCtx.addLibraryFunction(Clib.exitLabel)

      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)

      // Call exit
      locationCtx.setUpCall(List(dest))
      instructionCtx.addInstruction(Call(Clib.exitLabel))
      locationCtx.cleanUpCall(None)

    case Print(e) =>
      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)

      // Call the print function corresponding to the type of the expression
      val printLabel = e.getType match {
        case IntType => {
          instructionCtx.addLibraryFunction(Clib.printiLabel)
          Clib.printiLabel
        }
        case CharType => {
          instructionCtx.addLibraryFunction(Clib.printcLabel)
          Clib.printcLabel
        }
        case BoolType => {
          instructionCtx.addLibraryFunction(Clib.printbLabel)
          Clib.printbLabel
        }
        case StringType => {
          instructionCtx.addLibraryFunction(Clib.printsLabel)
          Clib.printsLabel
        }
        case ArrayType(CharType) => {
          // Increment the pointer to the start of the string (skip the size)
          instructionCtx.addInstruction(dest match {
            case r: Register => Add(r, INT_SIZE)
            case p: Pointer  => Add(p, INT_SIZE)
          })
          instructionCtx.addLibraryFunction(Clib.printsLabel)
          Clib.printsLabel
        }
        case ArrayType(_) => {
          instructionCtx.addLibraryFunction(Clib.printpLabel)
          Clib.printpLabel
        }
        case PairType(_, _) => {
          instructionCtx.addLibraryFunction(Clib.printpLabel)
          Clib.printpLabel
        }
        case _ => throw new RuntimeException("Invalid type")
      }
      locationCtx.setUpCall(List(dest))
      instructionCtx.addInstruction(Call(printLabel))
      locationCtx.cleanUpCall(None)

    case PrintLn(e) =>
      instructionCtx.addLibraryFunction(Clib.printlnLabel)
      // Print the expression
      translateStmt(Print(e))

      // Print a newline
      locationCtx.setUpCall(List()) // TODO: You can call println right after print, so this can be optimised
      instructionCtx.addInstruction(Call(Clib.printlnLabel))
      locationCtx.cleanUpCall(None)

    case If(cond, s1, s2) =>
      val falseLabel = Label(s"if_false_${ifCounter}")
      val endLabel = Label(s"if_end_${ifCounter}")
      ifCounter += 1

      branch(endLabel, falseLabel, cond, s1)
      translateStmt(s2)
      instructionCtx.addInstruction(DefineLabel(endLabel))

    case While(cond, body) =>
      val startLabel = Label(s"while_start_${instructionCtx.getWhileLoopId}")
      val endLabel = Label(s"while_end_${instructionCtx.getWhileLoopId}")

      instructionCtx.addInstruction(DefineLabel(startLabel))
      branch(startLabel, endLabel, cond, body)

    case Begin(body) => translateStmt(body)

    case Semi(s1, s2) =>
      translateStmt(s1)
      translateStmt(s2)
  }

  /**
   * {{{
   * if not cond
   *   goto falseLabel
   * trueBody
   * goto afterTrueLabel
   * falseLabel:
   * }}}
   *
   * @param afterTrueLabel The label to jump to after the true body
   * @param falseLabel The label to jump to if the condition is false
   * @param cond The condition to check
   * @param trueBody The body to execute if the condition is true
   */
  private def branch(afterTrueLabel: Label, falseLabel: Label, cond: Expr, trueBody: Stmt)(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Unit =
    val dest = locationCtx.getNext(typeToSize(cond.getType))
    translateExpr(cond)

    instructionCtx.addInstruction(Test(dest, TRUE))
    instructionCtx.addInstruction(Jmp(Zero, falseLabel))
    translateStmt(trueBody)
    instructionCtx.addInstruction(Jmp(NoCond, afterTrueLabel))
    instructionCtx.addInstruction(DefineLabel(falseLabel))

  /** Translates an RValue to and stores the result in the next available location at the time of invocation.
   *
   * @param value The RValue to translate
   */
  private def translateRValue(value: TypedAST.RValue)(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Unit = value match {
    case ArrayLiter(es, ArrayType(eTy)) =>
      instructionCtx.addLibraryFunction(Clib.mallocLabel)
      instructionCtx.addLibraryFunction(Clib.outOfMemoryLabel)
      instructionCtx.addLibraryFunction(Clib.printsLabel)

      // Calculate size needed for the array
      val size = (typeToSize(eTy).toBytes * es.size) + INT_SIZE

      // Allocate memory for the array and get the pointer to the array
      val tempSizeLocation = locationCtx.getNext(W32)
      instructionCtx.addInstruction(tempSizeLocation match {
        case r: Register => Mov(r, size)
        case p: Pointer  => Mov(p, size)
      })
      locationCtx.setUpCall(List(tempSizeLocation))
      instructionCtx.addInstruction(Call(Clib.mallocLabel))
      val ptrLoc: Location = locationCtx.cleanUpCall(Some(typeToSize(ArrayType(eTy))))

      // Move the pointer to the array to the next available location
      val arrayLoc = locationCtx.reserveNext(POINTER_SIZE)
      locationCtx.movLocLoc(arrayLoc, ptrLoc)

      // Store the size of the array
      locationCtx.regInstr1(
        arrayLoc,
        { reg => Mov(RegPointer(reg(POINTER_SIZE))(POINTER_SIZE), es.size) }
      )

      // Store the elements in the array
      es.zipWithIndex.foreach { (e, i) =>
        val expLoc = locationCtx.getNext(typeToSize(e.getType))
        translateExpr(e)
        val offset: Immediate = INT_SIZE + i * typeToSize(e.getType).toBytes
        locationCtx.regInstr2(
          arrayLoc,
          expLoc,
          { (reg1, reg2) =>
            Mov(RegImmPointer(reg1(POINTER_SIZE), offset)(typeToSize(e.getType)), reg2(typeToSize(e.getType)))
          }
        )
      }

      // Unreserve the array location
      locationCtx.unreserveLast()

    case NewPair(e1, e2, PairType(t1, t2)) =>
      instructionCtx.addLibraryFunction(Clib.mallocLabel)
      instructionCtx.addLibraryFunction(Clib.outOfMemoryLabel)
      instructionCtx.addLibraryFunction(Clib.printsLabel)

      // Move the size of the pair to the next available location
      val tempSizeLocation = locationCtx.getNext(W32)
      instructionCtx.addInstruction(tempSizeLocation match {
        case r: Register => Mov(r, PAIR_SIZE)
        case p: Pointer  => Mov(p, PAIR_SIZE)
      })

      // Allocate memory for the pair and get the pointer to the pair
      locationCtx.setUpCall(List(tempSizeLocation))
      instructionCtx.addInstruction(Call(Clib.mallocLabel))
      val ptrLoc = locationCtx.cleanUpCall(Some(typeToSize(PairType(t1, t2))))

      // Move the pointer to the pair to the next available location
      val pairLoc = locationCtx.reserveNext(W64) // W64 because it's a pointer
      locationCtx.movLocLoc(pairLoc, ptrLoc)

      // Store the first element in the pair
      val resultLoc1 = locationCtx.getNext(typeToSize(t1))
      translateExpr(e1)
      locationCtx.regInstr2(
        pairLoc,
        resultLoc1,
        { (reg1, reg2) => Mov(RegPointer(reg1(POINTER_SIZE))(typeToSize(t1)), reg2(typeToSize(t1))) }
      )

      // Store the second element in the pair
      val resultLoc2 = locationCtx.getNext(typeToSize(t2))
      translateExpr(e2)
      val offsetSnd: Immediate = PAIR_SIZE / 2 // offset to the second element from the start of the pair
      locationCtx.regInstr2(
        pairLoc,
        resultLoc2,
        { (reg1, reg2) => Mov(RegImmPointer(reg1(POINTER_SIZE), offsetSnd)(typeToSize(t2)), reg2(typeToSize(t2))) }
      )

      // Unreserve the pair location
      locationCtx.unreserveLast()
    case f @ Fst(_, ty) =>
      // Get the current location in the map of the Fst
      val fstLoc = getHeapLocation(f)

      // Move this into the expected result location
      val dest = locationCtx.getNext(typeToSize(ty))

      locationCtx.regInstr2(
        dest,
        fstLoc,
        { (reg1, reg2) => Mov(reg1(typeToSize(ty)), RegPointer(reg2(POINTER_SIZE))(typeToSize(ty))) }
      )

    case s @ Snd(_, ty) =>
      // Get the current location in the map of the Snd
      val sndLoc = getHeapLocation(s)

      // Move this into the expected result location
      val dest = locationCtx.getNext(typeToSize(ty))

      locationCtx.regInstr2(
        sndLoc,
        dest,
        { (reg1, reg2) => Mov(reg2(typeToSize(ty)), RegPointer(reg1(POINTER_SIZE))(typeToSize(ty))) }
      )

    case TypedCall(v, args, ty) =>
      // Translate arguments into temporary locations
      val argLocations: List[Location] = args.map { arg =>
        translateExpr(arg)
        val dest = locationCtx.reserveNext(typeToSize(arg.getType))
        dest
      }
      // Save caller-save registers and set up arguments
      locationCtx.setUpCall(argLocations)
      // Call the function
      instructionCtx.addInstruction(Call(getFunctionName(v.id)))
      // Restore caller-save registers
      val returnDest = locationCtx.cleanUpCall(Some(typeToSize(ty)))

      // Free argument temp locations
      argLocations.foreach { _ =>
        locationCtx.unreserveLast()
      }

      val resultLoc = locationCtx.getNext(typeToSize(ty))
      locationCtx.movLocLoc(resultLoc, returnDest)

    case e: Expr => translateExpr(e)
    case v =>
      throw new UnexpectedException(s"Unexpected Error: Invalid RValue \"$v\"")
  }

  /** Translates an expression. The result of the expression is stored in the next available location at the time of
   * invocation.
   *
   * @param expr The expression to translate
   */
  private def translateExpr(
      expr: TypedAST.Expr
  )(using instructionCtx: InstructionContext, locationCtx: LocationContext): Unit = expr match {
    case TypedNot(e) =>
      unary(
        e,
        { l =>
          instructionCtx.addInstruction(Not(l))
          // Truncate the new value to 1-bit
          l match {
            case r: Register => instructionCtx.addInstruction(And(r, 1))
            case p: Pointer  => instructionCtx.addInstruction(And(p, 1))
          }
        }
      )
    case Negate(e) =>
      unary(e, { l => instructionCtx.addInstruction(Neg(l)) })

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))
    case Len(e) =>
      val lenDest = locationCtx.reserveNext(typeToSize(IntType)) // Length is stored in a 4-byte location here

      val loc = e match
        case id: Ident =>
          locationCtx.getLocation(id)
        case x =>
          translateExpr(x)
          locationCtx.getNext(typeToSize(x.getType))

      locationCtx.regInstr2(
        loc,
        lenDest,
        { (reg1, reg2) => Mov(reg2(typeToSize(IntType)), RegPointer(reg1(POINTER_SIZE))(typeToSize(IntType))) }
      )

    case Ord(e) =>
      val ordDestExtended = locationCtx.getNext(typeToSize(IntType))
      val ordDest = locationCtx.reserveNext(typeToSize(CharType))
      unary(e, { l => locationCtx.movLocLoc(ordDest, l) })
      locationCtx.regInstr1(ordDestExtended, { reg => Movzx(reg(typeToSize(IntType)), reg(typeToSize(CharType))) })
      locationCtx.unreserveLast()

    case Chr(e) =>
      instructionCtx.addLibraryFunction(Clib.errBadCharLabel)
      val chrDest = locationCtx.reserveNext(typeToSize(IntType))
      unary(
        e,
        { l =>
          locationCtx.regInstr1(l, { reg => Compare(reg(typeToSize(e.getType)), MIN_CHAR) })
          instructionCtx.addInstruction(Jmp(LessEqual, Clib.errBadCharLabel))
          locationCtx.regInstr1(l, { reg => Compare(reg(typeToSize(e.getType)), MAX_CHAR) })
          instructionCtx.addInstruction(Jmp(GreaterEqual, Clib.errBadCharLabel))
          locationCtx.movLocLoc(chrDest, l)
        }
      )
      locationCtx.unreserveLast()

    case Mult(e1, e2) =>
      binary(
        e1,
        e2,
        { (regOp1, locOp2) => SignedMul(Some(regOp1), locOp2, None) }
      )

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))

    case op @ (Div(_, _) | Mod(_, _)) =>
      val (dividendExp, divisorExp) = op match {
        case Div(d, v) => (d, v)
        case Mod(d, v) => (d, v)
      }
      // Move the divisor to the eventual destination of the result (first available location)
      translateExpr(divisorExp)
      val resultDest = locationCtx.reserveNext(typeToSize(IntType))

      // Check for division by zero runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errDivZeroLabel)
      locationCtx.regInstr1(
        resultDest,
        { reg => Compare(reg(typeToSize(IntType)), 0) }
      )
      instructionCtx.addInstruction(Jmp(Equal, Clib.errDivZeroLabel))

      // Move the dividend to the next available location
      translateExpr(dividendExp)
      val dividendDest = locationCtx.reserveNext(typeToSize(IntType))

      val resultReg = op match
        case _: Div => QUOT_REG.apply
        case _: Mod => REM_REG.apply

      // Signed division in x86-64 stores the quotient in RAX and the remainder in RDX
      // so we need to ensure we don't clobber those registers
      locationCtx.withDivRegisters(
        List(QUOT_REG(typeToSize(IntType)), REM_REG(typeToSize(IntType))), {
          // Move the dividend to EAX
          instructionCtx.addInstruction(Mov(QUOT_REG(typeToSize(IntType)), dividendDest))
          // Sign extend EAX into RDX
          instructionCtx.addInstruction(Cdq)
          // Perform the division
          instructionCtx.addInstruction(SignedDiv(resultDest))
          // Move the quotient or remainder to the destination
          locationCtx.movLocLoc(resultDest, resultReg(typeToSize(IntType)))
        }
      )

      // Unreserve the locations
      locationCtx.unreserveLast()
      locationCtx.unreserveLast()

    case TypedAdd(e1, e2) =>
      binary(e1, e2, Add.apply)

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))

    case TypedSub(e1, e2) =>
      binary(e1, e2, Sub.apply)

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))

    case TypedGreater(e1, e2) => cmpExp(e1, e2, SetGreater.apply)

    case GreaterEq(e1, e2) => cmpExp(e1, e2, SetGreaterEqual.apply)

    case Smaller(e1, e2) => cmpExp(e1, e2, SetSmaller.apply)

    case SmallerEq(e1, e2) => cmpExp(e1, e2, SetSmallerEqual.apply)

    case Equals(e1, e2) => cmpExp(e1, e2, SetEqual.apply)

    case NotEquals(e1, e2) =>
      val dest = locationCtx.getNext(typeToSize(BoolType))
      translateExpr(Equals(e1, e2))
      instructionCtx.addInstruction(Not(dest))

    case TypedAnd(e1, e2) => binary(e1, e2, And.apply)

    case TypedOr(e1, e2) => binary(e1, e2, Or.apply)

    case IntLiter(x) =>
      val dest = locationCtx.getNext(typeToSize(IntType))
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, x)
        case p: Pointer  => Mov(p, x)
      })

    case BoolLiter(b) =>
      val dest = locationCtx.getNext(typeToSize(BoolType))
      val bVal = if b then TRUE else FALSE
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, bVal)
        case p: Pointer  => Mov(p, bVal)
      })

    case CharLiter(c) =>
      val dest = locationCtx.getNext(typeToSize(CharType))
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, c)
        case p: Pointer  => Mov(p, c)
      })

    case StringLiter(s) =>
      val label = instructionCtx.getStringLabel
      instructionCtx.addString(s, label)
      val dest = locationCtx.getNext(typeToSize(StringType))

      // Load the address of the string into the destination
      val stringPointer: Pointer = RegImmPointer(INSTRUCTION_POINTER, label)(typeToSize(StringType))
      locationCtx.regInstr1(dest, { reg => Lea(reg(POINTER_SIZE), stringPointer) })

    case PairLiter =>
      val dest = locationCtx.getNext(typeToSize(PairType(?, ?)))
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, NULL)
        case p: Pointer  => Mov(p, NULL)
      })

    case id: Ident =>
      val dest = locationCtx.getNext(typeToSize(id.getType))
      val loc = locationCtx.getLocation(id)
      locationCtx.movLocLoc(dest, loc)

    case elem: ArrayElem =>
      val dest = locationCtx.reserveNext(typeToSize(elem.getType))
      val loc = getHeapLocation(elem)

      locationCtx.regInstr2(
        dest,
        loc,
        { (reg1, reg2) =>
          Mov(reg1(typeToSize(elem.getType)), RegPointer(reg2(POINTER_SIZE))(typeToSize(elem.getType)))
        }
      )

      locationCtx.unreserveLast()

    case NestedExpr(e, ty) => translateExpr(e)
  }

  /** Translates a function.
    *
    * @param f The function to translate
    */
  private def translateFunction(f: Func)(using instructionCtx: InstructionContext): Unit =
    // Define the function label
    instructionCtx.addInstruction(DefineLabel(getFunctionName(f.v.id)))

    // Set up the location context for the function
    given locationCtx: LocationContext = new LocationContext()

    // Set up stack frame and save callee-save registers
    locationCtx.setUpFunc(f.params)

    // Translate the function body
    translateStmt(f.body)
    // Restoration of callee-save registers and stack frame clean up handled by Return, since all function
    // bodies are returning blocks

  /** Compare two expressions and set the result of the comparison in the next available location at the time of
   * invocation.
   *
   * @param e1 The first expression to compare
   * @param e2 The second expression to compare
   * @param setter The function to set the result of the comparison (e.g. SetGreater, SetEqual, etc.)
   */
  private def cmpExp(e1: Expr, e2: Expr, setter: Location => Instruction)(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Unit =
    translateExpr(e1)
    val dest = locationCtx.reserveNext(typeToSize(e1.getType))
    translateExpr(e2)
    val e2Dest = locationCtx.getNext(typeToSize(e2.getType))
    locationCtx.regInstr1(dest, { reg => Compare(reg(typeToSize(e2.getType)), e2Dest) })
    // Move dest to a 1-byte location so that the setter can set the correct byte
    locationCtx.unreserveLast()
    val destByte = locationCtx.getNext(W8)
    instructionCtx.addInstruction(setter(destByte))

  /** Get the location of an LValue that is stored on the heap
   *
   * @param l The LValue to get the location of
   * @return The location of the pointer to the LValue
   */
  private def getHeapLocation(l: HeapLValue)(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Location = {
    l match {
      case elem @ ArrayElem(v, es, ty) =>
        // move the base address of the array to the next available location
        // reserve the base address of the array
        val baseDest = locationCtx.reserveNext(typeToSize(v.getType))
        val baseLoc = locationCtx.getLocation(v)
        locationCtx.movLocLoc(baseDest, baseLoc)

        instructionCtx.addLibraryFunction(Clib.errArrBoundsLabel)

        // Calculate the final location
        es.zipWithIndex.foldLeft(v.getType) { (tyAcc, eWithIndex) =>
          val (e, i) = eWithIndex
          tyAcc match {
            case ArrayType(nextTy) =>
              // evaluate the index
              translateExpr(e)
              val indexDest = locationCtx.getNext(typeToSize(e.getType))

              // Check if the index is out of bounds runtime error
              instructionCtx.addInstruction(indexDest match {
                case r: Register => Compare(r, MIN_ARR_SIZE)
                case p: Pointer  => Compare(p, MIN_ARR_SIZE)
              })
              instructionCtx.addInstruction(Jmp(Less, (Clib.errArrBoundsLabel)))
              locationCtx.regInstr2(
                indexDest,
                baseDest,
                { (indexReg, sizeReg) =>
                  Compare(indexReg(typeToSize(e.getType)), RegPointer(sizeReg(POINTER_SIZE))(typeToSize(IntType)))
                }
              )
              instructionCtx.addInstruction(Jmp(GreaterEqual, Clib.errArrBoundsLabel))

              // get the size of the type (for scaling)
              val tySize = typeToSize(nextTy).toBytes

              locationCtx.regInstr2(
                baseDest,
                indexDest,
                { (reg1, reg2) =>
                  // baseDest = baseDest + indexDest * tySize + INT_SIZE
                  if i != es.length - 1 then
                    Mov(
                      reg1(POINTER_SIZE),
                      RegScaleRegImmPointer(reg1(POINTER_SIZE), tySize, reg2(POINTER_SIZE), INT_SIZE)(POINTER_SIZE)
                    )
                  else
                    // If the next type is not an array, we are at the last element of the array
                    // so we don't need to scale the index
                    Lea(
                      reg1(POINTER_SIZE),
                      RegScaleRegImmPointer(reg1(POINTER_SIZE), tySize, reg2(POINTER_SIZE), INT_SIZE)(
                        typeToSize(nextTy)
                      )
                    )
                }
              )
              nextTy
            case _ => throw new RuntimeException("Invalid type")
          }
        }

        locationCtx.unreserveLast()

        // Return the final location
        baseDest

      case Fst(l, ty) =>
        // Get the location of the pointer to the pair
        val pairPtrLoc = l match {
          case id: Ident     => locationCtx.getLocation(id)
          case h: HeapLValue => getHeapLocation(h)
        }

        // Check for null pair runtime error
        instructionCtx.addLibraryFunction(Clib.printsLabel)
        instructionCtx.addLibraryFunction(Clib.errNullLabel)
        pairPtrLoc match {
          case r: Register => instructionCtx.addInstruction(Compare(r, NULL))
          case p: Pointer  => instructionCtx.addInstruction(Compare(p, NULL))
        }
        instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

        l match {
          case id: Ident =>
            locationCtx.regInstr1(
              pairPtrLoc,
              { reg => Lea(reg(POINTER_SIZE), RegPointer(reg(POINTER_SIZE))(typeToSize(ty))) }
            )
          case _ =>
            locationCtx.regInstr1(
              pairPtrLoc,
              { reg => Mov(reg(POINTER_SIZE), RegPointer(reg(POINTER_SIZE))(POINTER_SIZE)) }
            )
        }

        pairPtrLoc

      case snd @ Snd(l, ty) =>
        // Get the location of the pointer to the pair
        val pairPtrLoc = l match {
          case id: Ident     => locationCtx.getLocation(id)
          case h: HeapLValue => getHeapLocation(h)
        }

        // Check for null pair runtime error
        instructionCtx.addLibraryFunction(Clib.printsLabel)
        instructionCtx.addLibraryFunction(Clib.errNullLabel)
        pairPtrLoc match {
          case r: Register => instructionCtx.addInstruction(Compare(r, NULL))
          case p: Pointer  => instructionCtx.addInstruction(Compare(p, NULL))
        }
        instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

        // Calculate the location of the second element
        val sndDest = locationCtx.getNext(typeToSize(PairType(?, ty)))
        locationCtx.movLocLoc(sndDest, pairPtrLoc)

        // add the offset to the pointer
        val offset = PAIR_SIZE / 2
        l match {
          case id: Ident =>
            locationCtx.regInstr1(
              sndDest,
              { reg => Lea(reg(POINTER_SIZE), RegImmPointer(reg(POINTER_SIZE), offset)(typeToSize(ty))) }
            )
          case _ =>
            locationCtx.regInstr1(
              sndDest,
              { reg => Mov(reg(POINTER_SIZE), RegImmPointer(reg(POINTER_SIZE), offset)(POINTER_SIZE)) }
            )
        }

        sndDest
    }
  }

  /** Translate a unary operation and store the result in the next available location at the time of invocation.
   *
   * @param e The expression to translate
   * @param instr The instruction to perform on the expression
   */
  private def unary(e: Expr, instr: Location => Unit)(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Unit =
    val dest = locationCtx.getNext(typeToSize(e.getType))
    translateExpr(e)
    instr(dest)

  /**
    * Translate a binary operation and store the result in the next available location at the time of invocation.
    *
    * @param e1 The first expression to translate
    * @param e2 The second expression to translate
    * @param instr The instruction to perform on the two expressions
    * @param check1 Overflow check for the first expression
    * @param check2 Overflow check for both expressions
    */
  private def binary(
      e1: Expr,
      e2: Expr,
      instr: (Register, Location) => Instruction,
      check1: Option[Location => Unit] = None,
      check2: Option[(Location, Location) => Unit] = None
  )(using
      instructionCtx: InstructionContext,
      locationCtx: LocationContext
  ): Unit = {
    translateExpr(e1)
    val dest = locationCtx.reserveNext(typeToSize(e1.getType))
    if check1.isDefined then check1.get(dest)
    val e2Dest = locationCtx.getNext(typeToSize(e2.getType))
    if check2.isDefined then check2.get(dest, e2Dest)
    translateExpr(e2)
    locationCtx.regInstr1(dest, { reg => instr(reg(typeToSize(e2.getType)), e2Dest) })
    locationCtx.unreserveLast()
  }
}

/** C library functions that might be used in the translator */
object Clib {

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
    Push(BASE_POINTER(W64)),
    Mov(BASE_POINTER(W64), STACK_POINTER(W64))
  ) ::: body ::: List(
    Mov(STACK_POINTER(W64), BASE_POINTER(W64)),
    Pop(BASE_POINTER(W64)),
    Ret(None)
  )

  // C library function labels
  private val ClibExit = Label("exit@plt")
  private val ClibFlush = Label("fflush@plt")
  private val ClibFree = Label("free@plt")
  private val ClibMalloc = Label("malloc@plt")
  private val ClibPrintf = Label("printf@plt")
  private val ClibPuts = Label("puts@plt")
  private val ClibScanf = Label("scanf@plt")

  // ---- PRINT FUNCTIONS ----
  private val IntFormatLabel = Label(".intFormat")
  private val CharacterFormatLabel = Label(".charFormat")
  private val falseLabel = Label(".false")
  private val trueLabel = Label(".true")
  private val boolStrLabel = Label(".boolStr")
  private val StringFormatLabel = Label(".stringFormat")
  private val PointerFormatLabel = Label(".pointerFormat")
  private val printlnStrLabel = Label(".printlnStr")

  private val IntFormatSpecifier = "%d"
  private val CharacterFormatSpecifier = "%c"
  private val falseStr = "false"
  private val trueStr = "true"
  private val boolStr = "%.*s"
  private val StringFormatSpecifier = "%.*s"
  private val PointerFormatSpecifier = "%p"
  private val printlnStr = ""

  val printiLabel = Label("_printi")
  val printcLabel = Label("_printc")
  val printbLabel = Label("_printb")
  val printsLabel = Label("_prints")
  val printpLabel = Label("_printp")
  val printlnLabel = Label("_println")

  /** Subroutine for printing an integer. */
  private val _printi = createReadOnlyString(IntFormatLabel, IntFormatSpecifier) ::: createFunction(
    printiLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Mov(ARG_2(W32), ARG_1(W32)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, IntFormatLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibPrintf),
      Mov(ARG_1(W64), 0),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a character. */
  private val _printc = createReadOnlyString(CharacterFormatLabel, CharacterFormatSpecifier) ::: createFunction(
    printcLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Mov(ARG_2(W8), ARG_1(W8)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, CharacterFormatLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibPrintf),
      Mov(ARG_1(W64), 0),
      Call(ClibFlush)
    )
  )

  private val boolBranchFalse = Label("_printbFalse")
  private val boolBranchTrue = Label("_printbTrue")

  private val _printb = createReadOnlyString(falseLabel, falseStr)
    ::: createReadOnlyString(trueLabel, trueStr)
    ::: createReadOnlyString(boolStrLabel, boolStr)
    ::: createFunction(
      printbLabel,
      List(
        Comment("Align stack to 16 bytes for external calls"),
        And(STACK_POINTER(W64), -16),
        Test(ARG_1(W8), 1),
        Jmp(NotEqual, boolBranchTrue),
        Lea(ARG_3(W64), RegImmPointer(INSTRUCTION_POINTER, falseLabel)(W64)),
        Jmp(NoCond, boolBranchFalse),
        DefineLabel(boolBranchTrue),
        Lea(ARG_3(W64), RegImmPointer(INSTRUCTION_POINTER, trueLabel)(W64)),
        DefineLabel(boolBranchFalse),
        Mov(ARG_2(W32), RegImmPointer(ARG_3(W64), -4)(W32)),
        Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, boolStrLabel)(W64)),
        Mov(RETURN(W8), 0),
        Call(ClibPrintf),
        Mov(ARG_1(W64), 0),
        Call(ClibFlush)
      )
    )

  /** Subroutine for printing a string. */
  private val _prints = createReadOnlyString(StringFormatLabel, StringFormatSpecifier) ::: createFunction(
    printsLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Mov(ARG_3(W64), ARG_1(W64)),
      Mov(ARG_2(W32), RegImmPointer(ARG_1(W64), -4)(W32)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, StringFormatLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibPrintf),
      Mov(ARG_1(W64), 0),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a pair or an array. */
  private val _printp = createReadOnlyString(PointerFormatLabel, PointerFormatSpecifier) ::: createFunction(
    printpLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Mov(ARG_2(W64), ARG_1(W64)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, PointerFormatLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibPrintf),
      Mov(ARG_1(W64), 0),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a newline. */
  private val _println = createReadOnlyString(printlnStrLabel, printlnStr) ::: createFunction(
    printlnLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, printlnStrLabel)(W64)),
      Call(ClibPuts),
      Mov(ARG_1(W64), 0),
      Call(ClibFlush)
    )
  )

  // ---- READ FUNCTIONS ----
  private val IntReadLabel = Label(".intRead")
  private val CharacterReadLabel = Label(".charRead")

  private val IntReadSpecifier = "%d"
  private val CharacterReadSpecifier = " %c"

  val readiLabel = Label("_readi")
  val readcLabel = Label("_readc")

  /** Subroutine for reading an integer. */
  private val _readi = createReadOnlyString(IntReadLabel, IntReadSpecifier) ::: createFunction(
    readiLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Comment("Allocate space on the stack to store the read value"),
      Sub(STACK_POINTER(W64), 16),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(STACK_POINTER(W64))(W32), ARG_1(W32)),
      Lea(ARG_2(W64), RegPointer(STACK_POINTER(W64))(W64)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, IntReadLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibScanf),
      Mov(RETURN(W32), RegPointer(STACK_POINTER(W64))(W32)),
      Add(STACK_POINTER(W64), 16)
    )
  )

  /** Subroutine for reading an character. */
  private val _readc = createReadOnlyString(CharacterReadLabel, CharacterReadSpecifier) ::: createFunction(
    readcLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Comment("Allocate space on the stack to store the read value"),
      Sub(STACK_POINTER(W64), 16),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(STACK_POINTER(W64))(W8), ARG_1(W8)),
      Lea(ARG_2(W64), RegPointer(STACK_POINTER(W64))(W64)),
      Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, CharacterReadLabel)(W64)),
      Mov(RETURN(W8), 0),
      Call(ClibScanf),
      Mov(RETURN(W8), RegPointer(STACK_POINTER(W64))(W8)),
      Add(STACK_POINTER(W64), 16)
    )
  )

  /// ---- ERRORS ----
  val outOfMemoryLabel = Label("_outOfMemory")
  val errNullLabel = Label("_errNull")
  val errDivZeroLabel = Label("_errDivZero")
  val errOverflowLabel = Label("_errOverflow")
  val errBadCharLabel = Label("_errBadChar")
  val errArrBoundsLabel = Label("_errArrBounds")

  private val OutOfMemoryStringLabel = Label(".outOfMemoryString")
  private val NullPairStringLabel = Label(".nullPairString")
  private val DivZeroStringLabel = Label(".divZeroString")
  private val OverflowStringLabel = Label(".overflowString")
  private val BadCharStringLabel = Label(".badCharString")
  private val ArrBoundsStringLabel = Label(".arrBoundsString")

  private val OutOfMemoryString = "fatal error: out of memory"
  private val NullPairString = "fatal error: null pair dereferenced or freed"
  private val DivZeroString = "fatal error: division or modulo by zero"
  private val OverflowString = "fatal error: integer overflow or underflow occurred"
  private val BadCharString = "fatal error: int %d is not ascii character 0-127"
  private val ArrBoundsString = "fatal error: array index out of bounds"

  /** Subroutine for an out of memory error. */
  private val _outOfMemory = createReadOnlyString(OutOfMemoryStringLabel, OutOfMemoryString) ::: List(
    DefineLabel(outOfMemoryLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, OutOfMemoryStringLabel)(W64)),
    Call(printsLabel),
    Mov(ARG_1(W8), -1),
    Call(ClibExit),
    Ret(None)
  )

  /** Subroutine for a null pair error. */
  private val _errNull = createReadOnlyString(NullPairStringLabel, NullPairString) ::: List(
    DefineLabel(errNullLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, NullPairStringLabel)(W64)),
    Call(printsLabel),
    Mov(ARG_1(W8), -1),
    Call(ClibExit)
  )

  /** Subroutine for a division by zero error. */
  private val _errDivZero = createReadOnlyString(DivZeroStringLabel, DivZeroString) ::: List(
    DefineLabel(errDivZeroLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, DivZeroStringLabel)(W64)),
    Call(printsLabel),
    Mov(ARG_1(W8), -1),
    Call(ClibExit)
  )

  /** Subroutine for an overflow error. */
  private val _errOverflow = createReadOnlyString(OverflowStringLabel, OverflowString) ::: List(
    DefineLabel(errOverflowLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, OverflowStringLabel)(W64)),
    Call(printsLabel),
    Mov(ARG_1(W8), -1),
    Call(ClibExit)
  )

  /** Subroutine for a bad character error. */
  private val _errBadChar = createReadOnlyString(BadCharStringLabel, BadCharString) ::: List(
    DefineLabel(errBadCharLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, BadCharStringLabel)(W64)),
    Mov(RETURN(W8), 0),
    Call(ClibPrintf),
    Mov(ARG_1(W64), 0),
    Call(ClibFlush),
    Mov(ARG_1(W8), -1),
    Call(ClibExit)
  )

  /** Subroutine for an array bounds error. */
  private val _errArrBounds = createReadOnlyString(ArrBoundsStringLabel, ArrBoundsString) ::: List(
    DefineLabel(errArrBoundsLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER(W64), -16),
    Lea(ARG_1(W64), RegImmPointer(INSTRUCTION_POINTER, ArrBoundsStringLabel)(W64)),
    Mov(RETURN(W8), 0),
    Call(ClibPrintf),
    Mov(ARG_1(W64), 0),
    Call(ClibFlush),
    Mov(ARG_1(W8), -1),
    Call(ClibExit)
  )

  // ---- EXIT AND HEAP FUNCTIONS ----
  val exitLabel = Label("_exit")
  val mallocLabel = Label("_malloc")
  val freeLabel = Label("_free")
  val freepairLabel = Label("_freepair")

  /** Subroutine for exiting the program. */
  private val _exit = createFunction(
    exitLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Call(ClibExit)
    )
  )

  /** Subroutine for allocating memory. Used for pairs and arrays. */
  private val _malloc = createFunction(
    mallocLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Call(ClibMalloc),
      Compare(RETURN(W64), 0),
      Jmp(Equal, outOfMemoryLabel)
    )
  )

  /** Subroutine for freeing array memory on the heap. */
  private val _free = createFunction(
    freeLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Call(ClibFree)
    )
  )

  /** Subroutine for freeing pair memory on the heap. */
  private val _freepair = createFunction(
    freepairLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER(W64), -16),
      Compare(ARG_1(W64), 0),
      Jmp(Equal, errNullLabel),
      Call(ClibFree)
    )
  )

  val labelToFunc: Map[Label, List[Instruction]] = Map(
    printiLabel -> _printi,
    printcLabel -> _printc,
    printbLabel -> _printb,
    printsLabel -> _prints,
    printpLabel -> _printp,
    printlnLabel -> _println,
    readiLabel -> _readi,
    readcLabel -> _readc,
    exitLabel -> _exit,
    mallocLabel -> _malloc,
    freeLabel -> _free,
    freepairLabel -> _freepair,
    outOfMemoryLabel -> _outOfMemory,
    errNullLabel -> _errNull,
    errDivZeroLabel -> _errDivZero,
    errOverflowLabel -> _errOverflow,
    errBadCharLabel -> _errBadChar,
    errArrBoundsLabel -> _errArrBounds
  )
}
