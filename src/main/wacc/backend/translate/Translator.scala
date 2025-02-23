package wacc

import TypedAST.{Call as TypedCall, *}
import wacc.RenamedAST.KnownType.{CharType, IntType}
import wacc.Size.*

class Translator {
  def translate(program: Program): List[Instruction] = {
    given translateCtx: ListContext[Instruction] = new ListContext()
    given locationCtx: LocationContext = new LocationContext()
    translateStmt(program.body)
    translateCtx.get
  }

  /* case class Read(l: LValue) extends Stmt case class Free(e: Expr) extends Stmt case class Return(e: Expr) extends
   * Stmt case class Exit(e: Expr) extends Stmt case class Print(e: Expr) extends Stmt case class PrintLn(e: Expr)
   * extends Stmt case class If(cond: Expr, s1: Stmt, s2: Stmt) extends Stmt case class While(cond: Expr, body: Stmt)
   * extends Stmt case class Begin(body: Stmt) extends Stmt case class Semi(s1: Stmt, s2: Stmt) extends Stmt */

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   * @param instructionCtx The context to add instructions to
   */
  private def translateStmt(
      stmt: Stmt
  )(using instructionCtx: ListContext[Instruction], locationCtx: LocationContext): Unit = stmt match {
    case Skip => instructionCtx.add(Nop)
    case Decl(v, r) =>
      val dest = locationCtx.getNext
      translateRValue(r) // translate the right hand side into the next available location
      locationCtx.addLocation(v, dest)
    case Asgn(l, r) =>
      val resultLoc = locationCtx.getNext
      translateRValue(r)
      locationCtx.moveToLocation(
        resultLoc,
        l
      ) // move the result to the location of the left hand side, and add the instructions to the context
    case Read(l) =>
      val resultLoc = locationCtx.getNext
      l.getType match {
        case IntType  => instructionCtx.add(Call("read_int"))
        case CharType => instructionCtx.add(Call("read_char"))
        case _        => throw new RuntimeException("Invalid type for read")
      }
      locationCtx.moveToLocation(resultLoc, l)
  }

  private def translateRValue(value: TypedAST.RValue) = ???
}
