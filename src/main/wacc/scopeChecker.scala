package wacc

class renamer {
  private val uid: Int = 0

  def rename(p: ast.Program): scopedast.Program = renameProgram(p)

  private def renameProgram(p: ast.Program): scopedast.Program = ???
}
