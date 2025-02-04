package wacc

import wacc.scopedast.QualifiedName

class renamer {
  private var uid: Int = 0
  private var functionIds: Map[String, QualifiedName] = Map()

  private def generateUid(): Int = {
    this.uid += 1
    this.uid
  }

  /** Renames all functions and variables in the program.
   *
   * @param p The program to rename (ast)
   * @return The renamed program (scopedast)
   */
  def rename(p: ast.Program): scopedast.Program = {
    // Generate unique identifiers for all functions
    this.functionIds =
      p.fs.foldLeft(Map[String, QualifiedName]())((fids, f) => {
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

  /** Rename a statement.
   *
   * @param stmt The statement to rename
   * @param parentScope The scope of the parent statement
   * @param isFunc Whether the statement is in a function context
   * @return The renamed statement and the new scope
   */
  private def renameStmt(
      stmt: ast.Stmt,
      parentScope: Map[String, QualifiedName],
      isFunc: Boolean
  ): (scopedast.Stmt, Map[String, QualifiedName]) = stmt match {
    case ast.Skip => (scopedast.Skip, parentScope)
    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), t)
      (
        scopedast.Decl(t, scopedast.Ident(name), renameRValue(r, parentScope)),
        parentScope + (v.v -> name)
      )
    case ast.Asgn(l, r) =>
      (
        scopedast.Asgn(
          renameLValue(l, parentScope),
          renameRValue(r, parentScope)
        ),
        parentScope
      )
    case ast.Read(l) =>
      (scopedast.Read(renameLValue(l, parentScope)), parentScope)
    case ast.Free(e) =>
      (scopedast.Free(renameExpr(e, parentScope)), parentScope)
    case ast.Return(e) =>
      if (!isFunc) {
        // TODO: Error handling
      }
      (scopedast.Return(renameExpr(e, parentScope)), parentScope)
    case ast.Exit(e) =>
      (scopedast.Exit(renameExpr(e, parentScope)), parentScope)
    case ast.Print(e) =>
      (scopedast.Print(renameExpr(e, parentScope)), parentScope)
    case ast.PrintLn(e) =>
      (scopedast.PrintLn(renameExpr(e, parentScope)), parentScope)
    case ast.If(cond, s1, s2) =>
      val scopedCond = renameExpr(cond, parentScope)
      val scopedThen = renameStmt(s1, parentScope, isFunc)._1
      val scopedElse = renameStmt(s2, parentScope, isFunc)._1
      (scopedast.If(scopedCond, scopedThen, scopedElse), parentScope)
    case ast.While(cond, body) =>
      val scopedCond = renameExpr(cond, parentScope)
      val scopedBody = renameStmt(body, parentScope, isFunc)._1
      (scopedast.While(scopedCond, scopedBody), parentScope)
    case ast.Begin(body) =>
      (scopedast.Begin(renameStmt(body, parentScope, isFunc)._1), parentScope)
    case ast.Semi(s1, s2) =>
      (
        scopedast.Semi(
          renameStmt(s1, parentScope, isFunc)._1,
          renameStmt(s2, parentScope, isFunc)._1
        ),
        parentScope
      )
  }

  private def renameRValue(
      r: ast.RValue,
      parentScope: Map[String, QualifiedName]
  ): scopedast.RValue = ???

  private def renameLValue(
      l: ast.LValue,
      parentScope: Map[String, QualifiedName]
  ): scopedast.LValue = ???

  private def renameExpr(
      e: ast.Expr,
      parentScope: Map[String, QualifiedName]
  ): scopedast.Expr = ???

  private def renameFunc(f: ast.Func): scopedast.Func = ???
}
