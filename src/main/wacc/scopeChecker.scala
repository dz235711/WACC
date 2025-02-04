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
    val fids = p.fs.map(f => {
      val name = f.ident.v
      val uid = generateUid()
      val fqn = QualifiedName(name, uid, f.t)

      // Check for redeclaration of function
      if (this.functionIds.contains(name)) {
        // TODO: Error handling
      } else {
        // Add the function to the functionIds map if it is not already declared
        this.functionIds += (name -> fqn)
      }

      fqn
    })

    // Rename all functions and the body
    val renamedFuncs = p.fs.zip(fids).map(renameFunc.tupled)
    val renamedBody = renameStmt(p.body, Map(), Map(), false)._1

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
      currentScope: Map[String, QualifiedName],
      isFunc: Boolean
  ): (scopedast.Stmt, Map[String, QualifiedName]) = stmt match {

    case ast.Skip => (scopedast.Skip, currentScope)

    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), t)
      val scopedR = renameRValue(r, parentScope ++ currentScope)
      val scopedDecl = scopedast.Decl(t, scopedast.Ident(name), scopedR)

      // Check if the variable is already declared in the current scope
      if (currentScope.contains(v.v)) {
        // TODO: Error handling
        (scopedDecl, currentScope)
      } else {
        /* Only add the variable to the current scope if it is not already
         * declared */
        (scopedDecl, currentScope + (v.v -> name))
      }

    case ast.Asgn(l, r) =>
      (
        scopedast.Asgn(
          renameLValue(l, parentScope ++ currentScope),
          renameRValue(r, parentScope ++ currentScope)
        ),
        currentScope
      )

    case ast.Read(l) =>
      (
        scopedast.Read(renameLValue(l, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Free(e) =>
      (scopedast.Free(renameExpr(e, parentScope ++ currentScope)), currentScope)

    case ast.Return(e) =>
      if (!isFunc) {
        // TODO: Error handling
      }
      (
        scopedast.Return(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Exit(e) =>
      (scopedast.Exit(renameExpr(e, parentScope ++ currentScope)), currentScope)

    case ast.Print(e) =>
      (
        scopedast.Print(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.PrintLn(e) =>
      (
        scopedast.PrintLn(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.If(cond, s1, s2) =>
      val scopedCond = renameExpr(cond, parentScope ++ currentScope)
      val scopedThen =
        renameStmt(s1, parentScope ++ currentScope, Map(), isFunc)._1
      val scopedElse =
        renameStmt(s2, parentScope ++ currentScope, Map(), isFunc)._1
      (scopedast.If(scopedCond, scopedThen, scopedElse), currentScope)

    case ast.While(cond, body) =>
      val scopedCond = renameExpr(cond, parentScope ++ currentScope)
      val scopedBody =
        renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
      (scopedast.While(scopedCond, scopedBody), currentScope)

    case ast.Begin(body) =>
      (
        scopedast.Begin(
          renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
        ),
        currentScope
      )

    case ast.Semi(s1, s2) =>
      val (scopedS1, currentScopeS1) =
        renameStmt(s1, parentScope, currentScope, isFunc)
      val (scopedS2, currentScopeS2) =
        renameStmt(s2, parentScope, currentScopeS1, isFunc)
      (
        scopedast.Semi(scopedS1, scopedS2),
        currentScopeS2
      )
  }

  /** Rename an rvalue.
   *
   * @param r The rvalue to rename
   * @param scope The scope of the rvalue
   * @return The renamed rvalue
   */
  private def renameRValue(
      r: ast.RValue,
      scope: Map[String, QualifiedName]
  ): scopedast.RValue = r match {
    case ast.ArrayLiter(es) =>
      scopedast.ArrayLiter(es.map(e => renameExpr(e, scope)))
    case ast.NewPair(e1, e2) =>
      scopedast.NewPair(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Fst(l) => scopedast.Fst(renameLValue(l, scope))
    case ast.Snd(l) => scopedast.Snd(renameLValue(l, scope))
    case ast.Call(v, args) =>
      if (!functionIds.contains(v.v)) {
        // TODO: Error handling
      }
      scopedast.Call(
        scopedast.Ident(functionIds(v.v)),
        args.map(e => renameExpr(e, scope))
      )
    case e: ast.Expr => renameExpr(e, scope)
  }

  /** Rename an lvalue.
   *
   * @param l The lvalue to rename
   * @param scope The scope of the lvalue
   * @return The renamed lvalue
   */
  private def renameLValue(
      l: ast.LValue,
      scope: Map[String, QualifiedName]
  ): scopedast.LValue = l match {
    case ast.Fst(l)   => scopedast.Fst(renameLValue(l, scope))
    case ast.Snd(l)   => scopedast.Snd(renameLValue(l, scope))
    case ast.Ident(v) => scopedast.Ident(QualifiedName(v, generateUid(), ???))
    case ast.ArrayElem(v, es) =>
      scopedast.ArrayElem(
        scopedast.Ident(QualifiedName(v.v, generateUid(), ???)),
        es.map(e => renameExpr(e, scope))
      )
  }

  /** Rename an expression.
   *
   * @param e The expression to rename
   * @param scope The scope of the expression
   * @return The renamed expression
   */
  private def renameExpr(
      e: ast.Expr,
      scope: Map[String, QualifiedName]
  ): scopedast.Expr = e match {
    case ast.Not(e)    => scopedast.Not(renameExpr(e, scope))
    case ast.Negate(e) => scopedast.Negate(renameExpr(e, scope))
    case ast.Len(e)    => scopedast.Len(renameExpr(e, scope))
    case ast.Ord(e)    => scopedast.Ord(renameExpr(e, scope))
    case ast.Chr(e)    => scopedast.Chr(renameExpr(e, scope))
    case ast.Mult(e1, e2) =>
      scopedast.Mult(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Div(e1, e2) =>
      scopedast.Div(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Mod(e1, e2) =>
      scopedast.Mod(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Add(e1, e2) =>
      scopedast.Add(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Sub(e1, e2) =>
      scopedast.Sub(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Greater(e1, e2) =>
      scopedast.Greater(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.GreaterEq(e1, e2) =>
      scopedast.GreaterEq(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Smaller(e1, e2) =>
      scopedast.Smaller(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.SmallerEq(e1, e2) =>
      scopedast.SmallerEq(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Equals(e1, e2) =>
      scopedast.Equals(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.NotEquals(e1, e2) =>
      scopedast.NotEquals(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.And(e1, e2) =>
      scopedast.And(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Or(e1, e2) =>
      scopedast.Or(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.IntLiter(x)    => scopedast.IntLiter(x)
    case ast.BoolLiter(b)   => scopedast.BoolLiter(b)
    case ast.CharLiter(c)   => scopedast.CharLiter(c)
    case ast.StringLiter(s) => scopedast.StringLiter(s)
    case ast.PairLiter      => scopedast.PairLiter
    case ast.Ident(v) => scopedast.Ident(QualifiedName(v, generateUid(), ???))
    case ast.ArrayElem(v, es) =>
      scopedast.ArrayElem(
        scopedast.Ident(QualifiedName(v.v, generateUid(), ???)),
        es.map(e => renameExpr(e, scope))
      )
    case ast.NestedExpr(e) => scopedast.NestedExpr(renameExpr(e, scope))
  }

  /** Rename a function.
   *
   * @param f The function to rename
   * @param qualifiedName The qualified name of the function
   * @return The renamed function
   */
  private def renameFunc(
      f: ast.Func,
      qualifiedName: QualifiedName
  ): scopedast.Func = {
    // Construct a map of the parameters to use as a scope for the body
    val params: Map[String, QualifiedName] =
      f.params.foldLeft(Map())((params, param) => {
        val (t, id) = param

        // Check for redeclaration of parameter
        if (params.contains(id.v)) {
          // TODO: Error handling
          params
        } else {
          // Add the parameter to the params map if it is not already declared
          params + (id.v -> QualifiedName(id.v, generateUid(), t))
        }
      })

    val scopedBody = renameStmt(f.body, Map(), params, true)._1
    scopedast.Func(
      scopedast.Ident(qualifiedName),
      params.values.map(scopedast.Ident.apply).toList,
      scopedBody
    )
  }
}
