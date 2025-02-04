package wacc

import wacc.scopedast.QualifiedName

class renamer {
  private var uid: Int = 0
  private var functionIds: Map[String, QualifiedName] = Map()

  private def generateUid(): Int = {
    this.uid += 1
    this.uid
  }

  /**
   * Renames all functions and variables in the program.
   * @param p The program to rename (ast)
   * @return The renamed program (scopedast)
   */
  def rename(p: ast.Program): scopedast.Program = {
    // Generate unique identifiers for all functions
    this.functionIds = p.fs.foldLeft(Map[String, QualifiedName]())((fids, f) => {
      val name = f.v.v
      val uid = generateUid()

      // Check for redeclaration of function
      if (fids.contains(name)) {
        // TODO: Error handling
        fids
      } else {
        fids + (name -> QualifiedName(name, uid, f.t))
      }
    })

    // Rename all functions and the body
    val renamedFuncs = p.fs.map(f => renameFunc(f))
    val renamedBody = renameStmt(p.body, Map(), false)._1

    // Return the renamed program
    scopedast.Program(renamedFuncs, renamedBody)
  }

  private def renameStmt(
      stmt: ast.Stmt,
      parentScope: Map[String, QualifiedName],
      isFunc: Boolean
  ): (scopedast.Stmt, Map[String, QualifiedName]) = ???

  private def renameFunc(f: ast.Func): scopedast.Func = ???
}
