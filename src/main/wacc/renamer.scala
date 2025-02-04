package wacc

import wacc.renamedast.{KnownType, QualifiedName, ?}

class renamer {
  private var uid: Int = 0
  private var functionIds: Map[String, QualifiedName] = Map()

  /** Generates a unique identifier.
   *
   * @return The unique identifier
   */
  private def generateUid(): Int = {
    this.uid += 1
    this.uid
  }

  /** Renames all functions and variables in the program.
   *
   * @param p The program to rename (ast)
   * @return The renamed program (renamedast)
   */
  def rename(p: ast.Program): renamedast.Program = {
    // Generate unique identifiers for all functions
    val fids = p.fs.map(f => {
      val (t, id) = f.decl
      val name = id.v
      val uid = generateUid()
      val fqn = QualifiedName(name, uid, translateType(t))

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
    renamedast.Program(renamedFuncs, renamedBody)
  }

  /** Translates between a syntactic type and a semantic type.
   *
   * @param synType The syntactic type
   * @return The semantic type
   * */
  private def translateType(synType: ast.Type | ast.PairElemType): KnownType =
    synType match {
      case ast.IntType()    => KnownType.Int
      case ast.BoolType()   => KnownType.Bool
      case ast.CharType()   => KnownType.Char
      case ast.StringType() => KnownType.String
      case ast.ArrayType(t) => KnownType.Array(translateType(t))
      case ast.ErasedPair() => KnownType.Pair(?, ?)
      case ast.PairType(t1, t2) =>
        KnownType.Pair(translateType(t1), translateType(t2))
    }

  /** Rename a function.
   *
   * @param f             The function to rename
   * @param qualifiedName The qualified name of the function
   * @return The renamed function
   */
  private def renameFunc(
      f: ast.Func,
      qualifiedName: QualifiedName
  ): renamedast.Func = {
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
          params + (id.v -> QualifiedName(
            id.v,
            generateUid(),
            translateType(t)
          ))
        }
      })

    val renamedBody = renameStmt(f.body, Map(), params, true)._1
    renamedast.Func(
      renamedast.Ident(qualifiedName),
      params.values.map(renamedast.Ident.apply).toList,
      renamedBody
    )
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
  ): (renamedast.Stmt, Map[String, QualifiedName]) = stmt match {

    case ast.Skip() => (renamedast.Skip, currentScope)

    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), translateType(t))
      val renamedR = renameRValue(r, parentScope ++ currentScope)
      val renamedDecl =
        renamedast.Decl(translateType(t), renamedast.Ident(name), renamedR)

      // Check if the variable is already declared in the current scope
      if (currentScope.contains(v.v)) {
        // TODO: Error handling
        (renamedDecl, currentScope)
      } else {
        /* Only add the variable to the current scope if it is not already
         * declared */
        (renamedDecl, currentScope + (v.v -> name))
      }

    case ast.Asgn(l, r) =>
      (
        renamedast.Asgn(
          renameLValue(l, parentScope ++ currentScope),
          renameRValue(r, parentScope ++ currentScope)
        ),
        currentScope
      )

    case ast.Read(l) =>
      (
        renamedast.Read(renameLValue(l, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Free(e) =>
      (
        renamedast.Free(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Return(e) =>
      if (!isFunc) {
        // TODO: Error handling
      }
      (
        renamedast.Return(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Exit(e) =>
      (
        renamedast.Exit(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Print(e) =>
      (
        renamedast.Print(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.PrintLn(e) =>
      (
        renamedast.PrintLn(renameExpr(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.If(cond, s1, s2) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedThen =
        renameStmt(s1, parentScope ++ currentScope, Map(), isFunc)._1
      val renamedElse =
        renameStmt(s2, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.If(renamedCond, renamedThen, renamedElse), currentScope)

    case ast.While(cond, body) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedBody =
        renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.While(renamedCond, renamedBody), currentScope)

    case ast.Begin(body) =>
      (
        renamedast.Begin(
          renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
        ),
        currentScope
      )

    case ast.Semi(s1, s2) =>
      val (renamedS1, currentScopeS1) =
        renameStmt(s1, parentScope, currentScope, isFunc)
      val (renamedS2, currentScopeS2) =
        renameStmt(s2, parentScope, currentScopeS1, isFunc)
      (
        renamedast.Semi(renamedS1, renamedS2),
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
  ): renamedast.RValue = r match {
    case ast.ArrayLiter(es) =>
      renamedast.ArrayLiter(es.map(e => renameExpr(e, scope)))
    case ast.NewPair(e1, e2) =>
      renamedast.NewPair(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Fst(l) => renamedast.Fst(renameLValue(l, scope))
    case ast.Snd(l) => renamedast.Snd(renameLValue(l, scope))
    case ast.Call(v, args) =>
      if (!functionIds.contains(v.v)) {
        // TODO: Error handling
      }
      renamedast.Call(
        renamedast.Ident(functionIds(v.v)),
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
  ): renamedast.LValue = l match {
    case ast.Fst(l) => renamedast.Fst(renameLValue(l, scope))
    case ast.Snd(l) => renamedast.Snd(renameLValue(l, scope))
    case ast.Ident(v) =>
      if (!scope.contains(v)) {
        // TODO: Error handling
        renamedast.Ident(QualifiedName(v, generateUid(), ?))
      }
      renamedast.Ident(scope(v))
    case ast.ArrayElem(v, es) =>
      renamedast.ArrayElem(
        renamedast.Ident(QualifiedName(v.v, generateUid(), ?)),
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
  ): renamedast.Expr = e match {
    case ast.Not(e)    => renamedast.Not(renameExpr(e, scope))
    case ast.Negate(e) => renamedast.Negate(renameExpr(e, scope))
    case ast.Len(e)    => renamedast.Len(renameExpr(e, scope))
    case ast.Ord(e)    => renamedast.Ord(renameExpr(e, scope))
    case ast.Chr(e)    => renamedast.Chr(renameExpr(e, scope))
    case ast.Mult(e1, e2) =>
      renamedast.Mult(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Div(e1, e2) =>
      renamedast.Div(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Mod(e1, e2) =>
      renamedast.Mod(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Add(e1, e2) =>
      renamedast.Add(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Sub(e1, e2) =>
      renamedast.Sub(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Greater(e1, e2) =>
      renamedast.Greater(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.GreaterEq(e1, e2) =>
      renamedast.GreaterEq(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Smaller(e1, e2) =>
      renamedast.Smaller(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.SmallerEq(e1, e2) =>
      renamedast.SmallerEq(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Equals(e1, e2) =>
      renamedast.Equals(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.NotEquals(e1, e2) =>
      renamedast.NotEquals(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.And(e1, e2) =>
      renamedast.And(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.Or(e1, e2) =>
      renamedast.Or(renameExpr(e1, scope), renameExpr(e2, scope))
    case ast.IntLiter(x)    => renamedast.IntLiter(x)
    case ast.BoolLiter(b)   => renamedast.BoolLiter(b)
    case ast.CharLiter(c)   => renamedast.CharLiter(c)
    case ast.StringLiter(s) => renamedast.StringLiter(s)
    case ast.PairLiter()    => renamedast.PairLiter
    case ast.Ident(v) =>
      if (!scope.contains(v)) {
        // TODO: Error handling
        renamedast.Ident(QualifiedName(v, generateUid(), ?))
      }
      renamedast.Ident(scope(v))
    case ast.ArrayElem(v, es) =>
      val renamedIdent = if (!scope.contains(v.v)) {
        // TODO: Error handling
        renamedast.Ident(QualifiedName(v.v, generateUid(), ?))
      } else {
        renamedast.Ident(scope(v.v))
      }
      renamedast.ArrayElem(renamedIdent, es.map(e => renameExpr(e, scope)))
    case ast.NestedExpr(e) => renamedast.NestedExpr(renameExpr(e, scope))
  }
}
