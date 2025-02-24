package wacc

import TypedAST.{Call as TypedCall, *}
import wacc.RenamedAST.KnownType.{CharType, IntType}
import wacc.Size.*

val NULL = 0
val TRUE = 1

class Translator {
  def translate(program: Program): List[Instruction] = {
    given translateCtx: ListContext[Instruction] = new ListContext()
    given locationCtx: LocationContext = new LocationContext()
    translateStmt(program.body)
    translateCtx.get
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
      val dest = locationCtx.getNext
      translateRValue(r)
      locationCtx.addLocation(v, dest)

    case Asgn(l, r) =>
      val resultLoc = locationCtx.getNext
      translateRValue(r)
      locationCtx.moveToLocation(
        resultLoc,
        l
      )

    case Read(l) =>
      locationCtx.saveCallerRegisters()
      l.getType match {
        case IntType =>
          instructionCtx.add(Call("read_int"))
          locationCtx.moveToLocation(RAX(W32), l)
        case CharType =>
          instructionCtx.add(Call("read_char"))
          locationCtx.moveToLocation(RAX(W8), l)
        case _ => throw new RuntimeException("Unexpected Error: Invalid type for read")
      }
      locationCtx.restoreCallerRegisters()

    case Free(e) =>
      val dest = locationCtx.getNext
      translateExpr(e)

      // Check for null
      instructionCtx.add(dest match {
        case r: Register => Compare(r, NULL)
        case p: Pointer  => Compare(p, NULL)
      })
      instructionCtx.add(JmpEqual("free_null_error"))

      // Call free
      locationCtx.saveCallerRegisters()
      instructionCtx.add(Mov(RDI(W64), dest))
      instructionCtx.add(Call("free"))
      locationCtx.restoreCallerRegisters()

    case Return(e) =>
      val dest = locationCtx.getNext
      translateExpr(e)
      instructionCtx.add(Mov(RAX(W64), dest))
      locationCtx.restoreCalleeRegisters()
      instructionCtx.add(Ret(None))

    case Exit(e) =>
      val dest = locationCtx.getNext
      translateExpr(e)

      // Call exit
      instructionCtx.add(Mov(RDI(W8), dest))
      instructionCtx.add(Call("exit"))

    case Print(e) =>
      val dest = locationCtx.getNext
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
    val dest = locationCtx.getNext
    translateExpr(cond)

    instructionCtx.add(Test(dest, TRUE))
    instructionCtx.add(JmpZero(falseLabel))
    translateStmt(trueBody)
    instructionCtx.add(Jmp(afterTrueLabel))
    instructionCtx.add(DefineLabel(falseLabel))

  private def translateRValue(value: TypedAST.RValue) = ???
  private def translateExpr(value: TypedAST.Expr) = ???
}
