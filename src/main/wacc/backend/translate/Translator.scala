package wacc

import TypedAST.{Call => TypedCall, _}
import wacc.Size.*

class Translator {
  def translate(program: Program): List[Instruction] = {
    given translateCtx: ListContext[Instruction] = new ListContext()
    translateStmt(program.body)
    translateCtx.get
  }

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   * @param ctx The context to add instructions to
   */
  private def translateStmt(stmt: Stmt)(using ctx: ListContext[Instruction]): Unit = stmt match {
    case Skip              => ctx.add(Nop)
    case Exit(IntLiter(n)) => ctx.add(Mov(RDI(W32), n)).add(Call("exit@plt"))
  }
}
