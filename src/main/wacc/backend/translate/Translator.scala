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

  /** The number of while loops so each jump label is unique */
  private var whileLoopCounter = 0

  /** The number of if statements so each jump label is unique */
  private var ifCounter = 0

  /** The number of try statements so each jump label is unique */
  private var tryCounter = 0

  /** The number of location contexts so each location context can create unique labels internally */
  private var locationContextCounter = 0

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

  /** Get the next while loop labels
   * 
   * @return A tuple of the start and end labels of a while loop
   */
  def getWhileLoopLabels(): (Label, Label) = {
    val startLabel = Label(s"while_start_$whileLoopCounter")
    val endLabel = Label(s"while_end_$whileLoopCounter")
    whileLoopCounter += 1
    (startLabel, endLabel)
  }

  /** Get the next if labels
   * 
   * @return A tuple of the false and end labels of an if statement
   */
  def getIfLabels(): (Label, Label) = {
    val falseLabel = Label(s"if_false_$ifCounter")
    val endLabel = Label(s"if_end_$ifCounter")
    ifCounter += 1
    (falseLabel, endLabel)
  }

  /** Get the next catch-finally labels
   *
   * @return A tuple of the catch and finally labels of a try-catch-finally statement
   */
  def getTryLabels(): (Label, Label) = {
    val catchLabel = Label(s"catch_$tryCounter")
    val finallyLabel = Label(s"finally_$tryCounter")
    tryCounter += 1
    (catchLabel, finallyLabel)
  }

  /** Generate a unique location context id
   * 
   * @return A unique location context id
   */
  def generateLocCtxId: Int = {
    val toReturn = locationContextCounter
    locationContextCounter += 1
    toReturn
  }

  /** Get the strings and list of instructions
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
  private val PairBytes = 16

  /** The size of a pointer */
  private val PointerSize = W64

  /** The size of an integer */
  private val IntSize = Size(IntType)

  /** The size of a boolean */
  private val BoolSize = Size(BoolType)

  /** The size of a character */
  private val CharSize = Size(CharType)

  /** The value of NULL */
  private val Null = 0

  /** The value of TRUE */
  private val True = 1

  /** The value of FALSE */
  private val False = 0

  /** The minimum value of a char */
  private val MinChar = 0

  /** The maximum value of a char */
  private val MaxChar = 127

  /** The minimum value for an array to be indexed */
  private val MinArrSize = 0

  /** The label for a user-defined function */
  private val FunctionLabel = "wacc_func_"

  def translate(program: Program): (List[(String, Label)], List[Instruction]) = {
    given translateCtx: InstructionContext = new InstructionContext()
    given locationCtx: LocationContext = new LocationContext(true, translateCtx.generateLocCtxId)

    // Translate the program body
    translateStmt(program.body)

    // Return 0 from main body
    translateCtx.addInstruction(Mov(RETURN, 0)(W64))
    translateCtx.addInstruction(Pop(BASE_POINTER)(W64))
    translateCtx.addInstruction(Ret(None))

    // Translate all functions in the program
    program.fs.foreach { f => translateFunction(f) }

    // Add the library functions to the instruction context
    translateCtx.getLibraryFunctions.foreach { instrs => instrs.foreach(translateCtx.addInstruction) }

    translateCtx.get
  }

  /** Generates a function name from an id
   * 
   * @param id The id (integer) to generate the function name
   * @return The function name
   */
  private def getFunctionName(id: Int): Label = Label(s"$FunctionLabel$id")

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   */
  private def translateStmt(
      stmt: Stmt
  )(using instructionCtx: InstructionContext, locationCtx: LocationContext): Unit = stmt match {
    case Skip => instructionCtx.addInstruction(Nop())

    case Decl(v, r) =>
      translateRValue(r)
      locationCtx.addLocation(v)

    case Asgn(l, r) =>
      translateRValue(r)
      val resultLoc = locationCtx.reserveNext()

      l match {
        case id: Ident =>
          val dest = locationCtx.getLocation(id)
          locationCtx.movLocLoc(dest, resultLoc, Size(id.getType))

        case h: HeapLValue =>
          val hDest = getHeapLocation(h)
          locationCtx.regInstr2(
            hDest,
            resultLoc,
            PointerSize,
            Size(l.getType),
            { (reg1, reg2) => Mov(RegPointer(reg1), reg2)(Size(l.getType)) }
          )
      }
      locationCtx.unreserveLast()

    case Read(id: Ident) =>
      // Move the original value to RDI in case the read fails
      val args = List((locationCtx.getLocation(id), Size(id.getType)))
      locationCtx.setUpCall(args)
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
      val resultLoc = locationCtx.cleanUpCall(args.length)
      val idDest = locationCtx.getLocation(id)
      locationCtx.movLocLoc(idDest, resultLoc, Size(id.getType))

    case Read(h: HeapLValue) =>
      val readParamLoc = locationCtx.reserveNext()
      val pointerLoc = getHeapLocation(h)
      locationCtx.regInstr2(
        readParamLoc,
        pointerLoc,
        Size(h.getType),
        PointerSize,
        { (reg1, reg2) => Mov(reg1, RegPointer(reg2))(Size(h.getType)) }
      )

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

      // Move the original value to RDI in case the read fails
      val args = List((readParamLoc, Size(h.getType)))
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(readLabel))
      val resultLoc = locationCtx.cleanUpCall(args.length)

      locationCtx.unreserveLast()

      // Move the result into the original location.
      locationCtx.regInstr2(
        pointerLoc,
        resultLoc,
        PointerSize,
        Size(h.getType),
        { (reg1, reg2) => Mov(RegPointer(reg1), reg2)(Size(h.getType)) }
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
          locationCtx.regInstr1(l, Size(e.getType), { reg => Compare(reg, Null)(Size(e.getType)) })
          instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

          // Call free
          val args = List((l, Size(e.getType)))
          locationCtx.setUpCall(args)
          instructionCtx.addInstruction(Call(freeLabel))
          locationCtx.cleanUpCall(args.length)
        }
      )

    case Return(e) =>
      unary(e, l => locationCtx.cleanUpFunc(l, Size(e.getType), false))
      instructionCtx.addInstruction(Ret(None))

    case Exit(e) =>
      instructionCtx.addLibraryFunction(Clib.exitLabel)

      translateExpr(e)
      val dest = locationCtx.reserveNext()

      // Call exit
      val args = List((dest, Size(e.getType)))
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(Clib.exitLabel))
      locationCtx.cleanUpCall(args.length)

      locationCtx.unreserveLast()

    case Print(e) =>
      translateExpr(e)
      val dest = locationCtx.reserveNext()

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
            case r: Register => Add(r, IntSize.asBytes)(PointerSize)
            case p: Pointer  => Add(p, IntSize.asBytes)(PointerSize)
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
      val args = List((dest, Size(e.getType)))
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(printLabel))
      locationCtx.cleanUpCall(args.length)

      locationCtx.unreserveLast()

    case PrintLn(e) =>
      instructionCtx.addLibraryFunction(Clib.printlnLabel)
      // Print the expression
      translateStmt(Print(e))

      // Print a newline
      val args = List()
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(Clib.printlnLabel))
      locationCtx.cleanUpCall(args.length)

    case If(cond, s1, s2) =>
      val (falseLabel, endLabel) = instructionCtx.getIfLabels()

      branch(endLabel, falseLabel, cond, s1)
      translateStmt(s2)
      instructionCtx.addInstruction(DefineLabel(endLabel))

    case While(cond, body) =>
      val (startLabel, endLabel) = instructionCtx.getWhileLoopLabels()

      instructionCtx.addInstruction(DefineLabel(startLabel))
      branch(startLabel, endLabel, cond, body)

    case Begin(body) => translateStmt(body)

    case Semi(s1, s2) =>
      translateStmt(s1)
      translateStmt(s2)

    case Throw(e) =>
      if locationCtx.isMain && !locationCtx.inTryContext then
        // call exit
        translateStmt(Exit(e))
      else
        translateExpr(e)
        locationCtx.throwException()

    case TryCatchFinally(tryBody, catchIdent, catchBody, finallyBody) =>
      val (catchLabel, finallyLabel) = instructionCtx.getTryLabels()

      locationCtx.enterTryCatchBlock(catchLabel, finallyLabel)

      // Try block
      translateStmt(tryBody)
      locationCtx.exitTryBlock()

      instructionCtx.addInstruction(Jmp(NoCond, finallyLabel))

      // Catch block
      instructionCtx.addInstruction(DefineLabel(catchLabel))
      locationCtx.setCatchIdent(catchIdent)

      translateStmt(catchBody)
      locationCtx.exitCatchBlock()

      // Finally block
      instructionCtx.addInstruction(DefineLabel(finallyLabel))
      translateStmt(finallyBody)
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
    val dest = locationCtx.getNext
    translateExpr(cond)

    // if not cond
    instructionCtx.addInstruction(Test(dest, True)(Size(BoolType)))
    //   goto falseLabel
    instructionCtx.addInstruction(Jmp(Zero, falseLabel))
    // trueBody
    translateStmt(trueBody)
    // goto afterTrueLabel
    instructionCtx.addInstruction(Jmp(NoCond, afterTrueLabel))
    // falseLabel:
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
      val size = (Size(eTy).asBytes * es.size) + IntSize.asBytes

      // Malloc memory for the array to get the pointer to the array
      val tempSizeLocation = locationCtx.reserveNext()
      instructionCtx.addInstruction(tempSizeLocation match {
        case r: Register => Mov(r, size)(Size(IntType))
        case p: Pointer  => Mov(p, size)(Size(IntType))
      })
      val args = List((tempSizeLocation, Size(IntType)))
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(Clib.mallocLabel))
      val ptrLoc: Location = locationCtx.cleanUpCall(args.length)
      locationCtx.unreserveLast()

      // Move the pointer to the array to the next available location
      val arrayLoc = locationCtx.reserveNext()
      locationCtx.movLocLoc(arrayLoc, ptrLoc, PointerSize)

      // Store the size of the array
      locationCtx.regInstr1(
        arrayLoc,
        PointerSize,
        { reg => Mov(RegPointer(reg), es.size)(Size(IntType)) }
      )

      // Store the elements in the array
      es.zipWithIndex.foreach { (e, i) =>
        val expLoc = locationCtx.getNext
        translateExpr(e)
        val offset: Immediate = IntSize.asBytes + i * Size(e.getType).asBytes
        locationCtx.regInstr2(
          arrayLoc,
          expLoc,
          PointerSize,
          Size(e.getType),
          { (reg1, reg2) =>
            Mov(RegImmPointer(reg1, offset), reg2)(Size(e.getType))
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
      val tempSizeLocation = locationCtx.reserveNext()
      instructionCtx.addInstruction(tempSizeLocation match {
        case r: Register => Mov(r, PairBytes)(Size(IntType))
        case p: Pointer  => Mov(p, PairBytes)(Size(IntType))
      })

      // Malloc memory for the pair to get the pointer to the pair
      val args = List((tempSizeLocation, Size(IntType)))
      locationCtx.setUpCall(args)
      instructionCtx.addInstruction(Call(Clib.mallocLabel))
      val ptrLoc = locationCtx.cleanUpCall(args.length)

      locationCtx.unreserveLast()

      // Move the pointer to the pair to the next available location
      val pairLoc = locationCtx.reserveNext()
      locationCtx.movLocLoc(pairLoc, ptrLoc, PointerSize)

      // Store the first element in the pair
      val resultLoc1 = locationCtx.getNext
      translateExpr(e1)
      locationCtx.regInstr2(
        pairLoc,
        resultLoc1,
        PointerSize,
        Size(t1),
        { (reg1, reg2) => Mov(RegPointer(reg1), reg2)(Size(t1)) }
      )

      // Store the second element in the pair
      val resultLoc2 = locationCtx.getNext
      translateExpr(e2)
      val offsetSnd: Immediate = PairBytes / 2 // offset to the second element from the start of the pair
      locationCtx.regInstr2(
        pairLoc,
        resultLoc2,
        PointerSize,
        Size(t2),
        { (reg1, reg2) => Mov(RegImmPointer(reg1, offsetSnd), reg2)(Size(t2)) }
      )

      // Unreserve the pair location
      locationCtx.unreserveLast()

    case fs @ (Fst(_, _) | Snd(_, _)) =>
      val lv = fs match {
        case Fst(lv, _) => lv
        case Snd(lv, _) => lv
      }

      // Get the pair pointer location
      val pairPtrLoc = locationCtx.reserveNext()

      lv match {
        case id: Ident =>
          locationCtx.movLocLoc(pairPtrLoc, locationCtx.getLocation(id), PointerSize)
        case h: HeapLValue =>
          // The pointer to the pair is stored in the heap (not in a location)
          val pairPtrPtr = getHeapLocation(h)
          locationCtx.regInstr2(
            pairPtrPtr,
            pairPtrLoc,
            PointerSize,
            PointerSize,
            { (reg1, reg2) => Mov(reg2, RegPointer(reg1))(PointerSize) }
          )
      }

      // Check for null pair runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errNullLabel)
      pairPtrLoc match {
        case r: Register => instructionCtx.addInstruction(Compare(r, Null)(PointerSize))
        case p: Pointer  => instructionCtx.addInstruction(Compare(p, Null)(PointerSize))
      }
      instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

      // Dereference the pair pointer location into itself
      fs match {
        case Fst(_, _) =>
          locationCtx.regInstr1(pairPtrLoc, PointerSize, { reg => Mov(reg, RegPointer(reg))(PointerSize) })
        case Snd(_, _) =>
          locationCtx.regInstr1(
            pairPtrLoc,
            PointerSize,
            { reg => Mov(reg, RegImmPointer(reg, PairBytes / 2))(PointerSize) }
          )
      }

      locationCtx.unreserveLast()

    case TypedCall(v, args, ty) =>
      // Translate arguments into temporary locations
      val argLocations: List[(Location, Size)] = args.map { arg =>
        translateExpr(arg)
        val dest = locationCtx.reserveNext()
        (dest, Size(arg.getType))
      }
      // Save caller-save registers and set up arguments
      locationCtx.setUpCall(argLocations)
      // Call the function
      instructionCtx.addInstruction(Call(getFunctionName(v.id)))
      // Restore caller-save registers
      val returnDest = locationCtx.cleanUpCall(argLocations.length)

      // Free argument temp locations
      argLocations.foreach { _ =>
        locationCtx.unreserveLast()
      }

      val resultLoc = locationCtx.getNext
      locationCtx.movLocLoc(resultLoc, returnDest, Size(ty))

    case e: Expr => translateExpr(e)
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
          instructionCtx.addInstruction(Not(l)(BoolSize))
          // Truncate the new value to 1-bit
          l match {
            case r: Register => instructionCtx.addInstruction(And(r, 1)(BoolSize))
            case p: Pointer  => instructionCtx.addInstruction(And(p, 1)(BoolSize))
          }
        }
      )
    case Negate(e) =>
      unary(e, { l => instructionCtx.addInstruction(Neg(l)(IntSize)) })

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))
    case Len(e) =>
      val lenDest = locationCtx.reserveNext()
      val loc = e match {
        case id: Ident =>
          locationCtx.getLocation(id)
        case x =>
          translateExpr(x)
          locationCtx.getNext
      }

      locationCtx.regInstr2(
        loc,
        lenDest,
        PointerSize,
        IntSize,
        { (reg1, reg2) => Mov(reg2, RegPointer(reg1))(IntSize) }
      )
      locationCtx.unreserveLast()

    case Ord(e) =>
      val ordDest = locationCtx.reserveNext()
      // Move the result of the expression to the next available location
      unary(e, { l => locationCtx.movLocLoc(ordDest, l, IntSize) })
      // Zero extend the result
      locationCtx.regInstr1(ordDest, IntSize, { reg => Movzx(reg, reg)(IntSize)(CharSize) })
      locationCtx.unreserveLast()

    case Chr(e) =>
      instructionCtx.addLibraryFunction(Clib.errBadCharLabel)
      val chrDest = locationCtx.reserveNext()
      unary(
        e,
        { l =>
          // Check that the value is greater than the minimum char value
          locationCtx.regInstr1(l, IntSize, { reg => Compare(reg, MinChar)(IntSize) })
          instructionCtx.addInstruction(Jmp(Less, Clib.errBadCharLabel))

          // Check that the value is less than the maximum char value
          locationCtx.regInstr1(l, IntSize, { reg => Compare(reg, MaxChar)(IntSize) })
          instructionCtx.addInstruction(Jmp(Greater, Clib.errBadCharLabel))

          locationCtx.movLocLoc(chrDest, l, CharSize)
        }
      )
      locationCtx.unreserveLast()

    case Mult(e1, e2) =>
      binary(
        e1,
        e2,
        { (regOp1, locOp2) => SignedMul(Some(regOp1), locOp2, None)(IntSize) }
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
      val resultDest = locationCtx.reserveNext()

      // Check for division by zero runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errDivZeroLabel)
      locationCtx.regInstr1(
        resultDest,
        IntSize,
        { reg => Compare(reg, 0)(IntSize) }
      )
      instructionCtx.addInstruction(Jmp(Equal, Clib.errDivZeroLabel))

      // Move the dividend to the next available location
      translateExpr(dividendExp)
      val dividendDest = locationCtx.reserveNext()

      val resultReg = op match
        case _: Div => QUOT_REG
        case _: Mod => REM_REG

      // Signed division in x86-64 stores the quotient in RAX and the remainder in RDX
      // so we need to ensure we don't clobber those registers
      locationCtx.withDivRegisters(
        List(QUOT_REG, REM_REG), {
          // Move the dividend to EAX
          instructionCtx.addInstruction(Mov(QUOT_REG, dividendDest)(IntSize))
          // Sign extend EAX into RDX
          instructionCtx.addInstruction(Cdq())
          // Perform the division
          instructionCtx.addInstruction(SignedDiv(resultDest)(IntSize))
          // Move the quotient or remainder to the destination
          locationCtx.movLocLoc(resultDest, resultReg, IntSize)
        }
      )

      // Unreserve the locations
      locationCtx.unreserveLast()
      locationCtx.unreserveLast()

    case TypedAdd(e1, e2) =>
      binary(e1, e2, { (reg, loc) => Add(reg, loc)(IntSize) })

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))

    case TypedSub(e1, e2) =>
      binary(e1, e2, { (reg, loc) => Sub(reg, loc)(IntSize) })

      // Check for under/overflow runtime error
      instructionCtx.addLibraryFunction(Clib.printsLabel)
      instructionCtx.addLibraryFunction(Clib.errOverflowLabel)
      instructionCtx.addInstruction(Jmp(Overflow, Clib.errOverflowLabel))

    case TypedGreater(e1, e2) => cmpExp(e1, e2, { loc => SetGreater(loc)(BoolSize) })

    case GreaterEq(e1, e2) => cmpExp(e1, e2, { loc => SetGreaterEqual(loc)(BoolSize) })

    case Smaller(e1, e2) => cmpExp(e1, e2, { loc => SetSmaller(loc)(BoolSize) })

    case SmallerEq(e1, e2) => cmpExp(e1, e2, { loc => SetSmallerEqual(loc)(BoolSize) })

    case Equals(e1, e2) => cmpExp(e1, e2, { loc => SetEqual(loc)(BoolSize) })

    case NotEquals(e1, e2) =>
      val dest = locationCtx.getNext
      translateExpr(Equals(e1, e2))
      instructionCtx.addInstruction(Not(dest)(BoolSize))

    case TypedAnd(e1, e2) => binary(e1, e2, { (reg, loc) => And(reg, loc)(BoolSize) })

    case TypedOr(e1, e2) => binary(e1, e2, { (reg, loc) => Or(reg, loc)(BoolSize) })

    case IntLiter(x) =>
      val dest = locationCtx.getNext
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, x)(IntSize)
        case p: Pointer  => Mov(p, x)(IntSize)
      })

    case BoolLiter(b) =>
      val dest = locationCtx.getNext
      val bVal = if b then True else False
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, bVal)(BoolSize)
        case p: Pointer  => Mov(p, bVal)(BoolSize)
      })

    case CharLiter(c) =>
      val dest = locationCtx.getNext
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, c)(CharSize)
        case p: Pointer  => Mov(p, c)(CharSize)
      })

    case StringLiter(s) =>
      val label = instructionCtx.getStringLabel
      instructionCtx.addString(s, label)
      val dest = locationCtx.getNext

      // Load the address of the string into the destination
      val stringPointer: Pointer = RegImmPointer(INSTRUCTION_POINTER, label)
      locationCtx.regInstr1(dest, PointerSize, { reg => Lea(reg, stringPointer)(PointerSize) })

    case PairLiter =>
      val dest = locationCtx.getNext
      instructionCtx.addInstruction(dest match {
        case r: Register => Mov(r, Null)(PointerSize)
        case p: Pointer  => Mov(p, Null)(PointerSize)
      })

    case id: Ident =>
      val dest = locationCtx.getNext
      val loc = locationCtx.getLocation(id)
      locationCtx.movLocLoc(dest, loc, Size(id.getType))

    case elem: ArrayElem =>
      val dest = locationCtx.reserveNext()
      val loc = getHeapLocation(elem)

      locationCtx.regInstr2(
        dest,
        loc,
        Size(elem.getType),
        PointerSize,
        { (reg1, reg2) =>
          Mov(reg1, RegPointer(reg2))(Size(elem.getType))
        }
      )

      locationCtx.unreserveLast()

    case NestedExpr(e, _) => translateExpr(e)
  }

  /** Translates a function.
    *
    * @param f The function to translate
    */
  private def translateFunction(f: Func)(using instructionCtx: InstructionContext): Unit =
    // Define the function label
    instructionCtx.addInstruction(DefineLabel(getFunctionName(f.v.id)))

    // Set up the location context for the function
    given locationCtx: LocationContext = new LocationContext(false, instructionCtx.generateLocCtxId)

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
    val dest = locationCtx.reserveNext()
    translateExpr(e2)
    val e2Dest = locationCtx.getNext
    locationCtx.regInstr1(dest, Size(e2.getType), { reg => Compare(reg, e2Dest)(Size(e2.getType)) })
    // Move dest to a 1-byte location so that the setter can set the correct byte
    locationCtx.unreserveLast()
    val destByte = locationCtx.getNext
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
      case elem @ ArrayElem(v, es, _) =>
        // move the base address of the array to the next available location
        // reserve the base address of the array
        val baseDest = locationCtx.reserveNext()
        val baseLoc = locationCtx.getLocation(v)
        locationCtx.movLocLoc(baseDest, baseLoc, PointerSize)

        instructionCtx.addLibraryFunction(Clib.errArrBoundsLabel)

        // Calculate the final location
        es.zipWithIndex.foldLeft(v.getType) { (tyAcc, eWithIndex) =>
          val (e, i) = eWithIndex
          tyAcc match {
            case ArrayType(nextTy) =>
              // evaluate the index
              translateExpr(e)
              val indexDest = locationCtx.getNext

              // Check if the index is out of bounds runtime error
              instructionCtx.addInstruction(indexDest match {
                case r: Register => Compare(r, MinArrSize)(IntSize)
                case p: Pointer  => Compare(p, MinArrSize)(IntSize)
              })
              instructionCtx.addInstruction(Jmp(Less, Clib.errArrBoundsLabel))
              locationCtx.regInstr2(
                indexDest,
                baseDest,
                IntSize,
                PointerSize,
                { (indexReg, sizeReg) =>
                  Compare(indexReg, RegPointer(sizeReg))(IntSize)
                }
              )
              instructionCtx.addInstruction(Jmp(GreaterEqual, Clib.errArrBoundsLabel))

              // get the size of the type (for scaling)
              val tySize = Size(nextTy).asBytes

              locationCtx.regInstr2(
                baseDest,
                indexDest,
                PointerSize,
                IntSize,
                { (reg1, reg2) =>
                  // baseDest = baseDest + indexDest * tySize + INT_SIZE
                  if i != es.length - 1 then
                    Mov(reg1, RegScaleRegImmPointer(reg1, tySize, reg2, IntSize.asBytes))(PointerSize)
                  else
                    // If the next type is not an array, we are at the last element of the array
                    // so we don't need to scale the index
                    Lea(reg1, RegScaleRegImmPointer(reg1, tySize, reg2, IntSize.asBytes))(PointerSize)
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
          case r: Register => instructionCtx.addInstruction(Compare(r, Null)(PointerSize))
          case p: Pointer  => instructionCtx.addInstruction(Compare(p, Null)(PointerSize))
        }
        instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

        l match {
          case id: Ident =>
            locationCtx.regInstr1(
              pairPtrLoc,
              PointerSize,
              { reg => Lea(reg, RegPointer(reg))(PointerSize) }
            )
          case _ =>
            locationCtx.regInstr1(
              pairPtrLoc,
              PointerSize,
              { reg => Mov(reg, RegPointer(reg))(PointerSize) }
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
          case r: Register => instructionCtx.addInstruction(Compare(r, Null)(PointerSize))
          case p: Pointer  => instructionCtx.addInstruction(Compare(p, Null)(PointerSize))
        }
        instructionCtx.addInstruction(Jmp(Equal, Clib.errNullLabel))

        // Calculate the location of the second element
        val sndDest = locationCtx.getNext
        locationCtx.movLocLoc(sndDest, pairPtrLoc, PointerSize)

        // add the offset to the pointer
        val offset = PairBytes / 2
        l match {
          case id: Ident =>
            locationCtx.regInstr1(
              sndDest,
              PointerSize,
              { reg => Lea(reg, RegImmPointer(reg, offset))(PointerSize) }
            )
          case _ =>
            locationCtx.regInstr1(
              sndDest,
              PointerSize,
              { reg => Mov(reg, RegImmPointer(reg, offset))(PointerSize) }
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
    translateExpr(e)
    val dest = locationCtx.reserveNext()
    instr(dest)
    locationCtx.unreserveLast()

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
    val dest = locationCtx.reserveNext()
    if check1.isDefined then check1.get(dest)
    val e2Dest = locationCtx.getNext
    if check2.isDefined then check2.get(dest, e2Dest)
    translateExpr(e2)
    locationCtx.regInstr1(dest, Size(e1.getType), { reg => instr(reg, e2Dest) })
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
    Text()
  )

  /**
    * Creates a list of assembly instructions to define a read only string
    * 
    * @param label The label of the string constant
    * @param string The string to define
    * @return A list of assembly instructions that defines the read only string constant
    */
  private def createReadOnlyString(label: Label, string: String): List[Instruction] =
    SectionReadOnlyData() :: createString(label, string)

  /**
    * Creates a list of assembly instructions to define a function
    * 
    * @param label The label of the function
    * @param body The body of the function
    * @return A list of assembly instructions that defines the function, including the stack frame setup and teardown
    */
  private def createFunction(label: Label, body: List[Instruction]): List[Instruction] = List(
    DefineLabel(label),
    Push(BASE_POINTER)(PointerSize),
    Mov(BASE_POINTER, STACK_POINTER)(PointerSize)
  ) ::: body ::: List(
    Mov(STACK_POINTER, BASE_POINTER)(PointerSize),
    Mov(Register.R11, 0)(W64), // Mark as no exception
    Pop(BASE_POINTER)(PointerSize),
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
      And(STACK_POINTER, -16)(W64),
      Mov(ARG_2, ARG_1)(W32),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, IntFormatLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibPrintf),
      Mov(ARG_1, 0)(W64),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a character. */
  private val _printc = createReadOnlyString(CharacterFormatLabel, CharacterFormatSpecifier) ::: createFunction(
    printcLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Mov(ARG_2, ARG_1)(W8),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, CharacterFormatLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibPrintf),
      Mov(ARG_1, 0)(W64),
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
        And(STACK_POINTER, -16)(W64),
        Test(ARG_1, 1)(W8),
        Jmp(NotEqual, boolBranchTrue),
        Lea(ARG_3, RegImmPointer(INSTRUCTION_POINTER, falseLabel))(W64),
        Jmp(NoCond, boolBranchFalse),
        DefineLabel(boolBranchTrue),
        Lea(ARG_3, RegImmPointer(INSTRUCTION_POINTER, trueLabel))(W64),
        DefineLabel(boolBranchFalse),
        Mov(ARG_2, RegImmPointer(ARG_3, -4))(W32),
        Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, boolStrLabel))(W64),
        Mov(RETURN, 0)(W8),
        Call(ClibPrintf),
        Mov(ARG_1, 0)(W64),
        Call(ClibFlush)
      )
    )

  /** Subroutine for printing a string. */
  private val _prints = createReadOnlyString(StringFormatLabel, StringFormatSpecifier) ::: createFunction(
    printsLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Mov(ARG_3, ARG_1)(W64),
      Mov(ARG_2, RegImmPointer(ARG_1, -4))(W32),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, StringFormatLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibPrintf),
      Mov(ARG_1, 0)(W64),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a pair or an array. */
  private val _printp = createReadOnlyString(PointerFormatLabel, PointerFormatSpecifier) ::: createFunction(
    printpLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Mov(ARG_2, ARG_1)(W64),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, PointerFormatLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibPrintf),
      Mov(ARG_1, 0)(W64),
      Call(ClibFlush)
    )
  )

  /** Subroutine for printing a newline. */
  private val _println = createReadOnlyString(printlnStrLabel, printlnStr) ::: createFunction(
    printlnLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, printlnStrLabel))(W64),
      Call(ClibPuts),
      Mov(ARG_1, 0)(W64),
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
      And(STACK_POINTER, -16)(W64),
      Comment("Allocate space on the stack to store the read value"),
      Sub(STACK_POINTER, 16)(W64),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(STACK_POINTER), ARG_1)(W32),
      Lea(ARG_2, RegPointer(STACK_POINTER))(W64),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, IntReadLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibScanf),
      Mov(RETURN, RegPointer(STACK_POINTER))(W32),
      Add(STACK_POINTER, 16)(W64)
    )
  )

  /** Subroutine for reading an character. */
  private val _readc = createReadOnlyString(CharacterReadLabel, CharacterReadSpecifier) ::: createFunction(
    readcLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Comment("Allocate space on the stack to store the read value"),
      Sub(STACK_POINTER, 16)(W64),
      Comment("Store original value in case of EOF"),
      Mov(RegPointer(STACK_POINTER), ARG_1)(W8),
      Lea(ARG_2, RegPointer(STACK_POINTER))(W64),
      Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, CharacterReadLabel))(W64),
      Mov(RETURN, 0)(W8),
      Call(ClibScanf),
      Mov(RETURN, RegPointer(STACK_POINTER))(W8),
      Add(STACK_POINTER, 16)(W64)
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
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, OutOfMemoryStringLabel))(W64),
    Call(printsLabel),
    Mov(ARG_1, -1)(W8),
    Call(ClibExit),
    Ret(None)
  )

  /** Subroutine for a null pair error. */
  private val _errNull = createReadOnlyString(NullPairStringLabel, NullPairString) ::: List(
    DefineLabel(errNullLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, NullPairStringLabel))(W64),
    Call(printsLabel),
    Mov(ARG_1, -1)(W8),
    Call(ClibExit)
  )

  /** Subroutine for a division by zero error. */
  private val _errDivZero = createReadOnlyString(DivZeroStringLabel, DivZeroString) ::: List(
    DefineLabel(errDivZeroLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, DivZeroStringLabel))(W64),
    Call(printsLabel),
    Mov(ARG_1, -1)(W8),
    Call(ClibExit)
  )

  /** Subroutine for an overflow error. */
  private val _errOverflow = createReadOnlyString(OverflowStringLabel, OverflowString) ::: List(
    DefineLabel(errOverflowLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, OverflowStringLabel))(W64),
    Call(printsLabel),
    Mov(ARG_1, -1)(W8),
    Call(ClibExit)
  )

  /** Subroutine for a bad character error. */
  private val _errBadChar = createReadOnlyString(BadCharStringLabel, BadCharString) ::: List(
    DefineLabel(errBadCharLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, BadCharStringLabel))(W64),
    Mov(RETURN, 0)(W8),
    Call(ClibPrintf),
    Mov(ARG_1, 0)(W64),
    Call(ClibFlush),
    Mov(ARG_1, -1)(W8),
    Call(ClibExit)
  )

  /** Subroutine for an array bounds error. */
  private val _errArrBounds = createReadOnlyString(ArrBoundsStringLabel, ArrBoundsString) ::: List(
    DefineLabel(errArrBoundsLabel),
    Comment("Align stack to 16 bytes for external calls"),
    And(STACK_POINTER, -16)(W64),
    Lea(ARG_1, RegImmPointer(INSTRUCTION_POINTER, ArrBoundsStringLabel))(W64),
    Mov(RETURN, 0)(W8),
    Call(ClibPrintf),
    Mov(ARG_1, 0)(W64),
    Call(ClibFlush),
    Mov(ARG_1, -1)(W8),
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
      And(STACK_POINTER, -16)(W64),
      Call(ClibExit)
    )
  )

  /** Subroutine for allocating memory. Used for pairs and arrays. */
  private val _malloc = createFunction(
    mallocLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Call(ClibMalloc),
      Compare(RETURN, 0)(W64),
      Jmp(Equal, outOfMemoryLabel)
    )
  )

  /** Subroutine for freeing array memory on the heap. */
  private val _free = createFunction(
    freeLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Call(ClibFree)
    )
  )

  /** Subroutine for freeing pair memory on the heap. */
  private val _freepair = createFunction(
    freepairLabel,
    List(
      Comment("Align stack to 16 bytes for external calls"),
      And(STACK_POINTER, -16)(W64),
      Compare(ARG_1, 0)(W64),
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
