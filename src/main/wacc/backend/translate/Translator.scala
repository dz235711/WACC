package wacc

import TypedAST.{Call as TypedCall, Not as TypedNot, *}
import wacc.RenamedAST.KnownType.{ArrayType, BoolType, CharType, IntType, PairType, StringType}
import wacc.RenamedAST.{KnownType, SemType}
import wacc.Size.*

import java.rmi.UnexpectedException

val NULL = 0
val TRUE = 1

// TODO: translateExpr consumes a location - make sure to free it after use in translateStmt

class Translator {
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
      locationCtx.moveToLVal(
        resultLoc,
        l
      )

    case Read(l) =>
      locationCtx.saveCallerRegisters()
      l.getType match {
        case IntType =>
          instructionCtx.add(Call("read_int"))
          locationCtx.moveToLVal(RAX(typeToSize(IntType)), l)
        case CharType =>
          instructionCtx.add(Call("read_char"))
          locationCtx.moveToLVal(RAX(typeToSize(CharType)), l)
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

  private def translateRValue(value: TypedAST.RValue) = ???

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
