package wacc

import TypedAST.{Call as TypedCall, Not as TypedNot, *}
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{KnownType}
import wacc.Size.*
import wacc.RenamedAST.SemType

import java.rmi.UnexpectedException

val NULL = 0
val TRUE = 1

// TODO: translateExpr consumes a location - make sure to free it after use in translateStmt

class Translator {

  // Constants
  val PAIR_SIZE = 16

  def translate(program: Program): List[Instruction] = {
    given translateCtx: ListContext[Instruction] = new ListContext()
    given locationCtx: LocationContext = new LocationContext()
    translateStmt(program.body)
    translateCtx.get
  }

  /** Convert a semantic type to a size
   *
   * @param t The semantic type to convert
   * @return The size of the semantic type
   */
  private def typeToSize(t: SemType): Size = t match {
    case IntType        => W32
    case BoolType       => W8
    case CharType       => W8
    case StringType     => W64
    case ArrayType(_)   => W64
    case PairType(_, _) => W64
    case _              => throw new UnexpectedException("Unexpected Error: Invalid type")
  }

  private def getSize(size: Int): Size = size match {
    case 1 => W8
    case 2 => W16
    case 4 => W32
    case 8 => W64
    case _ => throw new RuntimeException("Invalid size")
  }

  private def getTypeSize(ty: SemType): Int = ty match {
    case IntType        => 4 // TODO: Magic number
    case CharType       => 1
    case BoolType       => 1
    case StringType     => 8
    case ArrayType(_)   => 8
    case PairType(_, _) => 8
    case _              => throw new RuntimeException("Invalid type")
  }

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   */
  private def translateStmt(
      stmt: Stmt
  )(using instructionCtx: ListContext[Instruction], locationCtx: LocationContext): Unit = stmt match {
    case Skip => instructionCtx.add(Nop)

    case Decl(v, r) =>
      val dest = locationCtx.getNext(typeToSize(v.getType))
      translateRValue(r)
      locationCtx.addLocation(v, dest)

    case Asgn(l, r) =>
      val resultLoc = locationCtx.getNext(typeToSize(l.getType))
      translateRValue(r)
      val lLoc = getLValue(l)
      locationCtx.movLocLoc(lLoc, resultLoc)

    case Read(l) =>
      locationCtx.saveCallerRegisters()
      val lLoc = getLValue(l)
      l.getType match {
        case IntType =>
          instructionCtx.add(Call("read_int"))
          locationCtx.movLocLoc(lLoc, RAX(typeToSize(IntType)))
        case CharType =>
          instructionCtx.add(Call("read_char"))
          locationCtx.movLocLoc(lLoc, RAX(typeToSize(CharType)))
        case _ => throw new RuntimeException("Unexpected Error: Invalid type for read")
      }
      locationCtx.restoreCallerRegisters()

    case Free(e) =>
      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)

      // Check for null
      instructionCtx.add(dest match {
        case r: Register => Compare(r, NULL)
        case p: Pointer  => Compare(p, NULL)
      })
      instructionCtx.add(JmpEqual("free_null_error"))

      // Call free
      locationCtx.saveCallerRegisters()
      instructionCtx.add(Mov(RDI(typeToSize(e.getType)), dest))
      instructionCtx.add(Call("free"))
      locationCtx.restoreCallerRegisters()

    case Return(e) =>
      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)
      instructionCtx.add(Mov(RAX(typeToSize(e.getType)), dest))
      locationCtx.restoreCalleeRegisters()
      instructionCtx.add(Ret(None))

    case Exit(e) =>
      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)

      // Call exit
      instructionCtx.add(Mov(RDI(W8), dest))
      instructionCtx.add(Call("exit"))

    case Print(e) =>
      val dest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)

      // Call the print function corresponding to the type of the expression
      locationCtx.saveCallerRegisters()
      e.getType match
        case _ => ??? // TODO
      locationCtx.restoreCallerRegisters()

    case PrintLn(e) =>
      // Print the expression
      translateStmt(Print(e))

      // Print a newline
      locationCtx.saveCallerRegisters()
      instructionCtx.add(Call("print_ln"))
      locationCtx.restoreCallerRegisters()

    case If(cond, s1, s2) =>
      val falseLabel = s"if_false_${cond.hashCode()}"
      val endLabel = s"if_end_${cond.hashCode()}"

      branch(endLabel, falseLabel, cond, s1)
      translateStmt(s2)
      instructionCtx.add(DefineLabel(endLabel))

    case While(cond, body) =>
      val startLabel = s"while_start_${cond.hashCode()}"
      val endLabel = s"while_end_${cond.hashCode()}"

      instructionCtx.add(DefineLabel(startLabel))
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
  private def branch(afterTrueLabel: String, falseLabel: String, cond: Expr, trueBody: Stmt)(using
      instructionCtx: ListContext[Instruction],
      locationCtx: LocationContext
  ): Unit =
    val dest = locationCtx.getNext(typeToSize(cond.getType))
    translateExpr(cond)

    instructionCtx.add(Test(dest, TRUE))
    instructionCtx.add(JmpZero(falseLabel))
    translateStmt(trueBody)
    instructionCtx.add(Jmp(afterTrueLabel))
    instructionCtx.add(DefineLabel(falseLabel))

  private def translateRValue(value: TypedAST.RValue)(using
      instructionCtx: ListContext[Instruction],
      locationCtx: LocationContext
  ) = value match {
    case ArrayLiter(es, ty) =>
      // Calculate size needed for the array
      val typeSize = getTypeSize(ty)
      val size = 4 + ((es.size) * typeSize) // 4 bytes for the size of the array

      // Allocate memory for the array
      instructionCtx.add(Mov(RDI(W32), size))
      locationCtx.saveCallerRegisters()
      instructionCtx.add(Call("_malloc"))
      val ptr = locationCtx.setNextReg(RAX(W64))
      locationCtx.restoreCallerRegisters()

      // Store the size of the array and array elements
      instructionCtx.add(Mov(ptr, es.size))
      es.zipWithIndex.foreach { (e, i) =>
        val resultLoc = locationCtx.getNext(typeToSize(e.getType))
        translateExpr(e)
        val src = resultLoc match {
          case r: Register => r
          case p: Pointer  => locationCtx.setNextReg(p)
        }
        instructionCtx.add(Mov(RegImmPointer(ptr, (i * typeSize))(getSize(typeSize)), src))
      }
    case NewPair(e1, e2, PairType(t1, t2)) =>
      // Find the sizes of the pair elements
      val type1Size = getTypeSize(t1)
      val type2Size = getTypeSize(t2)

      // Allocate memory for the pair
      instructionCtx.add(Mov(RDI(W32), PAIR_SIZE))
      locationCtx.saveCallerRegisters()
      instructionCtx.add(Call("_malloc"))
      val ptr = locationCtx.setNextReg(RAX(W64))
      locationCtx.restoreCallerRegisters()

      // Store the pair elements
      val resultLoc1 = locationCtx.getNext
      translateExpr(e1)
      val src1 = resultLoc1 match {
        case r: Register => r
        case p: Pointer  => locationCtx.setNextReg(p)
      }
      instructionCtx.add(Mov(RegPointer(ptr)(getSize(type1Size)), src1))

      val resultLoc2 = locationCtx.getNext
      translateExpr(e2)
      val src2 = resultLoc2 match {
        case r: Register => r
        case p: Pointer  => locationCtx.setNextReg(p)
      }
      instructionCtx.add(Mov(RegImmPointer(ptr, (PAIR_SIZE / 2))(getSize(type2Size)), src2))
    case f @ Fst(_, ty) =>
      // Get the current location in the map of the Fst
      val fstLoc = getLValue(f)
      // Check this isn't null
      instructionCtx.add(fstLoc match {
        case r: Register => Compare(r, NULL)
        case p: Pointer  => Compare(p, NULL)
      })
      instructionCtx.add(JmpEqual("fst_null_error"))

      // Move this into the expected result location
      val resultLoc = locationCtx.getNext(typeToSize(ty))
      locationCtx.movLocLoc(resultLoc, fstLoc)
    case s @ Snd(_, ty) =>
      // Get the current location in the map of the Snd
      val sndLoc = getLValue(s)
      // Check this isn't null
      instructionCtx.add(sndLoc match {
        case r: Register => Compare(r, NULL)
        case p: Pointer  => Compare(p, NULL)
      })
      instructionCtx.add(JmpEqual("snd_null_error"))

      // Move this into the expected result location
      val resultLoc = locationCtx.getNext(typeToSize(ty))
      locationCtx.movLocLoc(resultLoc, sndLoc)

  }

  private def translateExpr(
      expr: TypedAST.Expr
  )(using instructionCtx: ListContext[Instruction], locationCtx: LocationContext): Unit = expr match {
    case TypedNot(e) => unary(e, { l => Not(l) })
    case Negate(e)   => unary(e, { l => Neg(l) })

    case Len(e) =>
      val lenDest =
        locationCtx.reserveNext(typeToSize(IntType)) // we want to move the size of the array to this location
      val arrDest = locationCtx.getNext(typeToSize(e.getType))
      translateExpr(e)
      locationCtx.movLocLoc(lenDest, arrDest)
      locationCtx.unreserveLast()

    case Ord(e) =>
      val ordDest = locationCtx.reserveNext(typeToSize(IntType))
      val charDest = locationCtx.getNext(typeToSize(CharType))
      translateExpr(e)
      locationCtx.movLocLoc(ordDest, charDest)
      locationCtx.unreserveLast()

    case Chr(e) =>
      val chrDest = locationCtx.reserveNext(typeToSize(CharType))
      val codeDest = locationCtx.getNext(typeToSize(IntType))
      translateExpr(e)
      locationCtx.movLocLoc(chrDest, codeDest)
      locationCtx.unreserveLast()

    case Mult(e1, e2) =>
      val multDest = locationCtx.reserveNext(typeToSize(IntType))
      translateExpr(e1)
      val e2Dest = locationCtx.getNext(typeToSize(IntType))
      translateExpr(e2)
      locationCtx.regInstr(multDest, e2Dest, { (regOp1, locOp2) => SignedMul(Some(regOp1), locOp2, None) })
      // TODO: runtime error if over/underflow
      locationCtx.unreserveLast()

    case Mod(e1, e2) =>
      val modDest = locationCtx.reserveNext(typeToSize(IntType))
      translateExpr(e2)
      // TODO: runtime error if divide by 0
      val e1Dest = locationCtx.getNext(typeToSize(IntType))
      translateExpr(e1)
      locationCtx.withFreeRegisters(
        List(RAX(typeToSize(IntType)), RDI(typeToSize(IntType))), {
          instructionCtx.add(Mov(RAX(typeToSize(IntType)), e1Dest))
          instructionCtx.add(SignedDiv(modDest))
          locationCtx.movLocLoc(modDest, RAX(typeToSize(IntType)))
        }
      )

  }

  /** Calculate and return the location of an LValue.
   *
   * @param l The LValue to calculate the location of
   * @return The location of the LValue, which can e.g. be written into or read from directly
   */
  private def getLValue(l: LValue): Location = ???

  private def unary(e: Expr, instr: Location => Instruction)(using
      instructionCtx: ListContext[Instruction],
      locationCtx: LocationContext
  ) =
    val dest = locationCtx.getNext(typeToSize(e.getType))
    translateExpr(e)
    instructionCtx.add(instr(dest))
}

/* case class Mod(e1: Expr, e2: Expr) extends Expr, IntType case class Add(e1: Expr, e2: Expr) extends Expr, IntType
 * case class Div(e1: Expr, e2: Expr) extends Expr, IntType case class Sub(e1: Expr, e2: Expr) extends Expr, IntType
 * case class Greater(e1: Expr, e2: Expr) extends Expr, BoolType case class GreaterEq(e1: Expr, e2: Expr) extends Expr,
 * BoolType case class Smaller(e1: Expr, e2: Expr) extends Expr, BoolType case class SmallerEq(e1: Expr, e2: Expr)
 * extends Expr, BoolType case class Equals(e1: Expr, e2: Expr) extends Expr, BoolType case class NotEquals(e1: Expr,
 * e2: Expr) extends Expr, BoolType case class And(e1:
 * Expr, e2: Expr) extends Expr, BoolType case class Or(e1: Expr, e2: Expr) extends Expr, BoolType
 *
 * case class IntLiter(x: Int) extends Expr, IntType case class BoolLiter(b: Boolean) extends Expr, BoolType case class
 * CharLiter(c: Char) extends Expr, CharType case class StringLiter(s: String) extends Expr, StringType object PairLiter
 * extends Expr, Type { def getType: SemType = PairType(?, ?) } case class Ident(id: Int, override val getType: SemType)
 * extends Expr, LValue case class ArrayElem(v: Ident, es: List[Expr], override val getType: SemType) extends Expr,
 * LValue case class NestedExpr(e: Expr, override val getType: SemType) extends Expr, Type */
