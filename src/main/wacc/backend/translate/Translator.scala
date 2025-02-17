package wacc

import TypedAST.{Call => TypedCall, _}
import wacc.Size.*

class Translator {
  def translate(program: Program): List[Instruction] = {
    given translateCtx: TranslateContext = new TranslateContext
    translateStmt(program.body)
    translateCtx.get
  }

  /** Translates a statement to a list of instructions
   *
   * @param stmt The statement to translate
   * @param ctx The context to add instructions to
   */
  private def translateStmt(stmt: Stmt)(using ctx: TranslateContext): Unit = stmt match {
    case Skip              => ctx.addInstr(Nop)
    case Exit(IntLiter(n)) => ctx.addInstr(Mov(RDI(W32), n)).addInstr(Call("exit@plt"))
    case _                 => ()
  }
}
