package wacc

import wacc.renamedast.{KnownType, QualifiedName, ?}
import scala.collection.mutable

type Scope = Map[String, QualifiedName]

class renamer {
  private var uid: Int = 0
  // Map of function names to their qualified names and number of parameters
  private val functionIds: mutable.Map[String, (QualifiedName, Int)] =
    mutable.Map()

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
      if (functionIds.contains(name)) {
        // TODO: Error handling
      } else {
        // Add the function to the functionIds map if it is not already declared
        functionIds += (name -> (fqn, f.params.length))
      }

      fqn
    })

    // Rename all functions and the body
    val renamedFuncs = p.fs.zip(fids).map(renameFunc)
    val renamedBody = renameStmt(p.body, Map(), Map(), false)._1

    // Return the renamed program
    renamedast.Program(renamedFuncs, renamedBody)(p.pos)
  }

  /** Translates between a syntactic type and a semantic type.
   *
   * @param synType The syntactic type
   * @return The semantic type
   * */
  private def translateType(synType: ast.Type | ast.PairElemType): KnownType = {
    val knownType = synType match {
      case ast.IntType()    => renamedast.IntType()
      case ast.BoolType()   => renamedast.BoolType()
      case ast.CharType()   => renamedast.CharType()
      case ast.StringType() => renamedast.StringType()
      case ast.ArrayType(t) => renamedast.ArrayType(translateType(t))
      case ast.ErasedPair() => renamedast.PairType(?, ?)
      case ast.PairType(t1, t2) =>
        renamedast.PairType(translateType(t1), translateType(t2))
    }
    knownType(synType.pos)
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
    val funcScope: Scope = paramsToScope(f.params)
    val renamedBody = renameStmt(f.body, funcScope, Map(), true)._1

    renamedast.Func(
      renamedast.Ident(qualifiedName),
      f.params.map(p => renamedast.Ident(funcScope(p._2.v))),
      renamedBody
    )(f.pos)
  }

  /** Convert a function's parameters to a scope.
   *
   * @param params The parameters of the function
   * @return The scope of the parameters
   */
  private def paramsToScope(params: List[ast.Param]): Scope =
    params.foldLeft(Map())((params, param) => {
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

  /** Rename a statement.
   *
   * @param stmt        The statement to rename
   * @param parentScope The scope of the parent statement
   * @param isFunc      Whether the statement is in a function context
   * @return The renamed statement and the new scope
   */
  private def renameStmt(
      stmt: ast.Stmt,
      parentScope: Scope,
      currentScope: Scope,
      isFunc: Boolean
  ): (renamedast.Stmt, Scope) = stmt match {

    case ast.Skip() => (renamedast.Skip()(stmt.pos), currentScope)

    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), translateType(t))
      val renamedR = renameRValue(r, parentScope ++ currentScope)
      val renamedDecl =
        renamedast.Decl(renamedast.Ident(name)(v.pos), renamedR)(stmt.pos)

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
        )(stmt.pos),
        currentScope
      )

    case ast.Read(l) =>
      (
        renamedast.Read(renameLValue(l, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.Free(e) =>
      (
        renamedast.Free(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.Return(e) =>
      if (!isFunc) {
        // TODO: Error handling
      }
      (
        renamedast.Return(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.Exit(e) =>
      (
        renamedast.Exit(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.Print(e) =>
      (
        renamedast.Print(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.PrintLn(e) =>
      (
        renamedast.PrintLn(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case ast.If(cond, s1, s2) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedThen =
        renameStmt(s1, parentScope ++ currentScope, Map(), isFunc)._1
      val renamedElse =
        renameStmt(s2, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.If(renamedCond, renamedThen, renamedElse)(stmt.pos), currentScope)

    case ast.While(cond, body) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedBody =
        renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.While(renamedCond, renamedBody)(stmt.pos), currentScope)

    case ast.Begin(body) =>
      (
        renamedast.Begin(
          renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
        )(stmt.pos),
        currentScope
      )

    case ast.Semi(s1, s2) =>
      val (renamedS1, currentScopeS1) =
        renameStmt(s1, parentScope, currentScope, isFunc)
      val (renamedS2, currentScopeS2) =
        renameStmt(s2, parentScope, currentScopeS1, isFunc)
      (
        renamedast.Semi(renamedS1, renamedS2)(stmt.pos),
        currentScopeS2
      )
  }

  /** Rename an rvalue.
   *
   * @param r     The rvalue to rename
   * @param scope The scope of the rvalue
   * @return The renamed rvalue
   */
  private def renameRValue(r: ast.RValue, scope: Scope): renamedast.RValue =
    r match {
      case ast.ArrayLiter(es) =>
        renamedast.ArrayLiter(es.map(renameExpr(_, scope)))(r.pos)
      case ast.NewPair(e1, e2) =>
        renamedast.NewPair(renameExpr(e1, scope), renameExpr(e2, scope))(r.pos)
      case ast.Fst(l) => renamedast.Fst(renameLValue(l, scope))(r.pos)
      case ast.Snd(l) => renamedast.Snd(renameLValue(l, scope))(r.pos)
      case ast.Call(v, args) =>
        val renamedArgs = args.map(renameExpr(_, scope))

        // Check if the function is declared
        val renamedIdent = if (!functionIds.contains(v.v)) {
          // TODO: Error handling
          renamedast.Ident(QualifiedName(v.v, generateUid(), ?))
        } else {
          val (fqn, argLen) = functionIds(v.v)

          // Check if the number of arguments is correct
          if (args.length != argLen) {
            // TODO: Error handling
          }
          renamedast.Ident(fqn)
        }

        renamedast.Call(renamedIdent(v.pos), renamedArgs)(r.pos)
      case e: ast.Expr => renameExpr(e, scope)
    }

  private def renameIdent(v: ast.Ident, scope: Scope): renamedast.Ident = {
    if (!scope.contains(v.v)) {
      // TODO: Error handling
      renamedast.Ident(QualifiedName(v.v, generateUid(), ?))(v.pos)
    } else {
      renamedast.Ident(scope(v.v))(v.pos)
    }
  }

  private def renameArrayElem(
      arrElem: ast.ArrayElem,
      scope: Scope
  ): renamedast.ArrayElem = {
    val renamedIdent = renameIdent(arrElem.ident, scope)
    renamedast.ArrayElem(renamedIdent, arrElem.es.map(renameExpr(_, scope)))(arrElem.pos)
  }

  /** Rename an lvalue.
   *
   * @param l     The lvalue to rename
   * @param scope The scope of the lvalue
   * @return The renamed lvalue
   */
  private def renameLValue(l: ast.LValue, scope: Scope): renamedast.LValue =
    l match {
      case ast.Fst(lNested)       => renamedast.Fst(renameLValue(lNested, scope))(l.pos)
      case ast.Snd(lNested)       => renamedast.Snd(renameLValue(lNested, scope))(l.pos)
      case l: ast.Ident     => renameIdent(l, scope)
      case l: ast.ArrayElem => renameArrayElem(l, scope)
    }

  /** Rename an expression.
   *
   * @param e     The expression to rename
   * @param scope The scope of the expression
   * @return The renamed expression
   */
  private def renameExpr(e: ast.Expr, scope: Scope): renamedast.Expr = e match {
    case ast.Not(eNested)    => renamedast.Not(renameExpr(eNested, scope))(e.pos)
    case ast.Negate(eNested) => renamedast.Negate(renameExpr(eNested, scope))(e.pos)
    case ast.Len(eNested)    => renamedast.Len(renameExpr(eNested, scope))(e.pos)
    case ast.Ord(eNested)    => renamedast.Ord(renameExpr(eNested, scope))(e.pos)
    case ast.Chr(eNested)    => renamedast.Chr(renameExpr(eNested, scope))(e.pos)
    case ast.Mult(e1, e2) =>
      renamedast.Mult(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Div(e1, e2) =>
      renamedast.Div(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Mod(e1, e2) =>
      renamedast.Mod(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Add(e1, e2) =>
      renamedast.Add(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Sub(e1, e2) =>
      renamedast.Sub(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Greater(e1, e2) =>
      renamedast.Greater(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.GreaterEq(e1, e2) =>
      renamedast.GreaterEq(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Smaller(e1, e2) =>
      renamedast.Smaller(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.SmallerEq(e1, e2) =>
      renamedast.SmallerEq(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Equals(e1, e2) =>
      renamedast.Equals(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.NotEquals(e1, e2) =>
      renamedast.NotEquals(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.And(e1, e2) =>
      renamedast.And(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.Or(e1, e2) =>
      renamedast.Or(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
    case ast.IntLiter(x)    => renamedast.IntLiter(x)(e.pos)
    case ast.BoolLiter(b)   => renamedast.BoolLiter(b)(e.pos)
    case ast.CharLiter(c)   => renamedast.CharLiter(c)(e.pos)
    case ast.StringLiter(s) => renamedast.StringLiter(s)(e.pos)
    case ast.PairLiter()    => renamedast.PairLiter()(e.pos)
    case e: ast.Ident       => renameIdent(e, scope)
    case e: ast.ArrayElem   => renameArrayElem(e, scope)
    case ast.NestedExpr(eNested)  => renamedast.NestedExpr(renameExpr(eNested, scope))(e.pos)
  }
}
