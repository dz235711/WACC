package wacc

import wacc.renamedast.{KnownType, QualifiedName, ?}
import scala.collection.mutable
import WaccErrorBuilder.constructSpecialised
import scala.collection.mutable.ListBuffer

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
  def rename(errs: ListBuffer[WaccError])(p: ast.Program): renamedast.Program = {
    // Generate unique identifiers for all functions
    val fids = p.fs.map(f => {
      val (t, id) = f.decl
      val name = id.v
      val uid = generateUid()
      val fqn = QualifiedName(name, uid, translateType(t))

      // Check for redeclaration of function
      if (functionIds.contains(name)) {
        errs += constructSpecialised(
          id.pos,
          name,
          "Illegal function redeclaration"
        )
      } else {
        // Add the function to the functionIds map if it is not already declared
        functionIds += (name -> (fqn, f.params.length))
      }

      fqn
    })

    // Rename all functions and the body
    val renamedFuncs = p.fs.zip(fids).map(renameFunc(errs))
    val renamedBody = renameStmt(errs)(p.body, Map(), Map(), false)._1

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
  private def renameFunc(errs: ListBuffer[WaccError])(
      f: ast.Func,
      qualifiedName: QualifiedName,
  ): renamedast.Func = {
    // Construct a map of the parameters to use as a scope for the body
    val funcScope: Scope = paramsToScope(errs)(f.params)
    val renamedBody = renameStmt(errs)(f.body, funcScope, Map(), true)._1

    renamedast.Func(
      renamedast.Ident(qualifiedName),
      funcScope.values.map(renamedast.Ident.apply).toList,
      renamedBody
    )
  }

  /** Convert a function's parameters to a scope.
   *
   * @param params The parameters of the function
   * @return The scope of the parameters
   */
  private def paramsToScope(errs: ListBuffer[WaccError])(params: List[ast.Param]): Scope =
    params.foldLeft(Map())((params, param) => {
      val (t, id) = param

      // Check for redeclaration of parameter
      if (params.contains(id.v)) {
        errs += constructSpecialised(
          id.pos,
          id.v,
          "Illegal function redeclaration"
        )
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
  private def renameStmt(errs: ListBuffer[WaccError])(
      stmt: ast.Stmt,
      parentScope: Scope,
      currentScope: Scope,
      isFunc: Boolean,
  ): (renamedast.Stmt, Scope) = stmt match {

    case ast.Skip() => (renamedast.Skip, currentScope)

    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), translateType(t))
      val renamedR = renameRValue(errs)(r, parentScope ++ currentScope)
      val renamedDecl =
        renamedast.Decl(renamedast.Ident(name), renamedR)

      // Check if the variable is already declared in the current scope
      if (currentScope.contains(v.v)) {
        errs += constructSpecialised(
          v.pos,
          v.v,
          "Illegal function redeclaration"
        )
        (renamedDecl, currentScope)
      } else {
        /* Only add the variable to the current scope if it is not already
         * declared */
        (renamedDecl, currentScope + (v.v -> name))
      }

    case ast.Asgn(l, r) =>
      (
        renamedast.Asgn(
          renameLValue(errs)(l, parentScope ++ currentScope),
          renameRValue(errs)(r, parentScope ++ currentScope)
        ),
        currentScope
      )

    case ast.Read(l) =>
      (
        renamedast.Read(renameLValue(errs)(l, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Free(e) =>
      (
        renamedast.Free(renameExpr(errs)(e, parentScope ++ currentScope)),
        currentScope
      )

    case ret@ast.Return(e) =>
      if (!isFunc) {
        errs += constructSpecialised(
          ret.pos,
          "return",
          "Return statement outside of function"
        )
      }
      (
        renamedast.Return(renameExpr(errs)(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Exit(e) =>
      (
        renamedast.Exit(renameExpr(errs)(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.Print(e) =>
      (
        renamedast.Print(renameExpr(errs)(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.PrintLn(e) =>
      (
        renamedast.PrintLn(renameExpr(errs)(e, parentScope ++ currentScope)),
        currentScope
      )

    case ast.If(cond, s1, s2) =>
      val renamedCond = renameExpr(errs)(cond, parentScope ++ currentScope)
      val renamedThen =
        renameStmt(errs)(s1, parentScope ++ currentScope, Map(), isFunc)._1
      val renamedElse =
        renameStmt(errs)(s2, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.If(renamedCond, renamedThen, renamedElse), currentScope)

    case ast.While(cond, body) =>
      val renamedCond = renameExpr(errs)(cond, parentScope ++ currentScope)
      val renamedBody =
        renameStmt(errs)(body, parentScope ++ currentScope, Map(), isFunc)._1
      (renamedast.While(renamedCond, renamedBody), currentScope)

    case ast.Begin(body) =>
      (
        renamedast.Begin(
          renameStmt(errs)(body, parentScope ++ currentScope, Map(), isFunc)._1
        ),
        currentScope
      )

    case ast.Semi(s1, s2) =>
      val (renamedS1, currentScopeS1) =
        renameStmt(errs)(s1, parentScope, currentScope, isFunc)
      val (renamedS2, currentScopeS2) =
        renameStmt(errs)(s2, parentScope, currentScopeS1, isFunc)
      (
        renamedast.Semi(renamedS1, renamedS2),
        currentScopeS2
      )
  }

  /** Rename an rvalue.
   *
   * @param r     The rvalue to rename
   * @param scope The scope of the rvalue
   * @return The renamed rvalue
   */
  private def renameRValue(errs: ListBuffer[WaccError])(r: ast.RValue, scope: Scope): renamedast.RValue =
    r match {
      case ast.ArrayLiter(es) =>
        renamedast.ArrayLiter(es.map(renameExpr(errs)(_, scope)))
      case ast.NewPair(e1, e2) =>
        renamedast.NewPair(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
      case ast.Fst(l) => renamedast.Fst(renameLValue(errs)(l, scope))
      case ast.Snd(l) => renamedast.Snd(renameLValue(errs)(l, scope))
      case ast.Call(v, args) =>
        val renamedArgs = args.map(renameExpr(errs)(_, scope))

        // Check if the function is declared
        val renamedIdent = if (!functionIds.contains(v.v)) {
          errs += constructSpecialised(
            v.pos,
            v.v,
            "Attempted usage of undefined function"
          )
          renamedast.Ident(QualifiedName(v.v, generateUid(), ?))
        } else {
          val (fqn, argLen) = functionIds(v.v)

          // Check if the number of arguments is correct
          if (args.length != argLen) {
            errs += constructSpecialised(
              v.pos,
              v.v,
              "Incorrect number of arguments"
            )
          }
          renamedast.Ident(fqn)
        }

        renamedast.Call(renamedIdent, renamedArgs)
      case e: ast.Expr => renameExpr(errs)(e, scope)
    }

  private def renameIdent(errs: ListBuffer[WaccError])(v: ast.Ident, scope: Scope): renamedast.Ident = {
    if (!scope.contains(v.v)) {
      errs += constructSpecialised(
        v.pos,
        v.v,
        "Attempted usage of undefined variable"
      )
      renamedast.Ident(QualifiedName(v.v, generateUid(), ?))
    } else {
      renamedast.Ident(scope(v.v))
    }
  }

  private def renameArrayElem(errs: ListBuffer[WaccError])(
      v: ast.Ident,
      es: List[ast.Expr],
      scope: Scope,
  ): renamedast.ArrayElem = {
    val renamedIdent = renameIdent(errs)(v, scope)
    renamedast.ArrayElem(renamedIdent, es.map(renameExpr(errs)(_, scope)))
  }

  /** Rename an lvalue.
   *
   * @param l     The lvalue to rename
   * @param scope The scope of the lvalue
   * @return The renamed lvalue
   */
  private def renameLValue(errs: ListBuffer[WaccError])(l: ast.LValue, scope: Scope): renamedast.LValue =
    l match {
      case ast.Fst(l)           => renamedast.Fst(renameLValue(errs)(l, scope))
      case ast.Snd(l)           => renamedast.Snd(renameLValue(errs)(l, scope))
      case id@ast.Ident(v)         => renameIdent(errs)(id, scope)
      case ast.ArrayElem(v, es) => renameArrayElem(errs)(v, es, scope)
    }

  /** Rename an expression.
   *
   * @param e     The expression to rename
   * @param scope The scope of the expression
   * @return The renamed expression
   */
  private def renameExpr(errs: ListBuffer[WaccError])(e: ast.Expr, scope: Scope): renamedast.Expr = e match {
    case ast.Not(e)    => renamedast.Not(renameExpr(errs)(e, scope))
    case ast.Negate(e) => renamedast.Negate(renameExpr(errs)(e, scope))
    case ast.Len(e)    => renamedast.Len(renameExpr(errs)(e, scope))
    case ast.Ord(e)    => renamedast.Ord(renameExpr(errs)(e, scope))
    case ast.Chr(e)    => renamedast.Chr(renameExpr(errs)(e, scope))
    case ast.Mult(e1, e2) =>
      renamedast.Mult(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Div(e1, e2) =>
      renamedast.Div(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Mod(e1, e2) =>
      renamedast.Mod(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Add(e1, e2) =>
      renamedast.Add(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Sub(e1, e2) =>
      renamedast.Sub(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Greater(e1, e2) =>
      renamedast.Greater(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.GreaterEq(e1, e2) =>
      renamedast.GreaterEq(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Smaller(e1, e2) =>
      renamedast.Smaller(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.SmallerEq(e1, e2) =>
      renamedast.SmallerEq(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Equals(e1, e2) =>
      renamedast.Equals(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.NotEquals(e1, e2) =>
      renamedast.NotEquals(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.And(e1, e2) =>
      renamedast.And(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.Or(e1, e2) =>
      renamedast.Or(renameExpr(errs)(e1, scope), renameExpr(errs)(e2, scope))
    case ast.IntLiter(x)      => renamedast.IntLiter(x)
    case ast.BoolLiter(b)     => renamedast.BoolLiter(b)
    case ast.CharLiter(c)     => renamedast.CharLiter(c)
    case ast.StringLiter(s)   => renamedast.StringLiter(s)
    case ast.PairLiter()      => renamedast.PairLiter
    case id@ast.Ident(v)      => renameIdent(errs)(id, scope)
    case ast.ArrayElem(v, es) => renameArrayElem(errs)(v, es, scope)
  }
}
