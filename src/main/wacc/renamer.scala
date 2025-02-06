package wacc

import wacc.renamedast.{KnownType, QualifiedName, ?}
import scala.collection.mutable
import WaccErrorBuilder.constructSpecialised
import wacc.renamedast.Func

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
   * @param errs The list of semantic errors
   * @return (The renamed program (renamedast), the expanded list of semantic errors)
   */
  def rename(
      p: ast.Program,
      errs: List[WaccError]
  ): (renamedast.Program, List[WaccError]) = {
    // Generate unique identifiers for all functions
    var moddedErrs = errs
    val fids = p.fs.map(f => {
      val (t, id) = f.decl
      val name = id.v
      val uid = generateUid()
      val fqn = QualifiedName(name, uid, translateType(t))

      // Check for redeclaration of function
      if (functionIds.contains(name)) {
        moddedErrs = constructSpecialised(
          f.pos,
          name,
          "Illegal function redeclaration"
        ) :: moddedErrs
      } else {
        // Add the function to the functionIds map if it is not already declared
        functionIds += (name -> (fqn, f.params.length))
      }

      fqn
    })

    // Rename all functions and the body
    var renamedFuncs: List[Func] = List()
    for ((f, qfn) <- p.fs.zip(fids)) {
      val (renamedFunc, retErrs) = renameFunc(f, qfn, moddedErrs)
      moddedErrs = retErrs
      renamedFuncs = renamedFuncs :+ renamedFunc
    }
    val (renamedBody, _, retErrs) =
      renameStmt(p.body, Map(), Map(), false, moddedErrs)
    moddedErrs = retErrs

    // Return the renamed program
    (renamedast.Program(renamedFuncs, renamedBody), moddedErrs)
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
      qualifiedName: QualifiedName,
      errs: List[WaccError]
  ): (renamedast.Func, List[WaccError]) = {
    // Construct a map of the parameters to use as a scope for the body
    var moddedErrs = errs
    val (funcScope, retErrs) = paramsToScope(f.params, moddedErrs)
    val (renamedBody, _, retErrs1) =
      renameStmt(f.body, funcScope, Map(), true, retErrs)
    moddedErrs = retErrs1
    (
      renamedast.Func(
        renamedast.Ident(qualifiedName),
        funcScope.values.map(renamedast.Ident.apply).toList,
        renamedBody
      ),
      moddedErrs
    )
  }

  /** Convert a function's parameters to a scope.
   *
   * @param params The parameters of the function
   * @return The scope of the parameters
   */
  private def paramsToScope(
      params: List[ast.Param],
      errs: List[WaccError]
  ): (Scope, List[WaccError]) = 
    params.foldLeft((Map(), errs)) { case ((params, accErrs), param) =>
      val (t, id) = param

      // Check for redeclaration of parameter
      if (params.contains(id.v)) {
        (
          params,
          constructSpecialised(
            id.pos,
            id.v,
            "Illegal parameter redeclaration"
          ) :: accErrs
        )
      } else {
        // Add the parameter to the params map if it is not already declared
        (
          params + (id.v -> QualifiedName(
            id.v,
            generateUid(),
            translateType(t)
          )),
          accErrs
        )
      }
    }
  

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
      isFunc: Boolean,
      errs: List[WaccError]
  ): (renamedast.Stmt, Scope, List[WaccError]) = stmt match {

    case ast.Skip() => (renamedast.Skip, currentScope, errs)

    case ast.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), translateType(t))
      val (renamedR, retErrs) = renameRValue(r, parentScope ++ currentScope, errs)
      var moddedErrs = retErrs 
      val renamedDecl =
        renamedast.Decl(renamedast.Ident(name), renamedR)

      // Check if the variable is already declared in the current scope
      if (currentScope.contains(v.v)) {
        moddedErrs = constructSpecialised(
          v.pos,
          v.v,
          "Illegal variable redeclaration"
        ) :: moddedErrs
        (renamedDecl, currentScope, moddedErrs)
      } else {
        /* Only add the variable to the current scope if it is not already
         * declared */
        (renamedDecl, currentScope + (v.v -> name), moddedErrs)
      }

    case ast.Asgn(l, r) =>
      val (renamedR, retErrs) = renameRValue(r, parentScope ++ currentScope, errs)
      val (renamedL, retErrs1) = renameLValue(l, parentScope ++ currentScope, retErrs)
      (
        renamedast.Asgn(
          renamedL,
          renamedR
        ),
        currentScope,
        retErrs1
      )

    case ast.Read(l) =>
      val (renamedL, retErrs) = renameLValue(l, parentScope ++ currentScope, errs)
      (
      renamedast.Read(renamedL),
      currentScope,
      retErrs
      )

    case ast.Free(e) =>
      val (renamedExpr, retErrs) = renameExpr(e, parentScope ++ currentScope, errs)
      (
      renamedast.Free(renamedExpr),
      currentScope,
      retErrs
      )

    case ret@ast.Return(e) =>
      var moddedErrs = errs
      if (!isFunc) {
      moddedErrs = constructSpecialised(ret.pos, "return", "Return statement outside of function") :: moddedErrs
      }
      val (renamedExpr, retErrs) = renameExpr(e, parentScope ++ currentScope, moddedErrs)
      (
      renamedast.Return(renamedExpr),
      currentScope,
      retErrs
      )

    case ast.Exit(e) =>
      val (renamedExpr, retErrs) = renameExpr(e, parentScope ++ currentScope, errs)
      (
      renamedast.Exit(renamedExpr),
      currentScope,
      retErrs
      )

    case ast.Print(e) =>
      val (renamedExpr, retErrs) = renameExpr(e, parentScope ++ currentScope, errs)
      (
      renamedast.Print(renamedExpr),
      currentScope,
      retErrs
      )

    case ast.PrintLn(e) =>
      val (renamedExpr, retErrs) = renameExpr(e, parentScope ++ currentScope, errs)
      (
      renamedast.PrintLn(renamedExpr),
      currentScope,
      retErrs
      )

    case ast.If(cond, s1, s2) =>
      val (renamedCond, retErrs) = renameExpr(cond, parentScope ++ currentScope, errs)
      val (renamedThen, _, retErrs1) =
        renameStmt(s1, parentScope ++ currentScope, Map(), isFunc, retErrs)
      val (renamedElse, _, retErrs2) =
        renameStmt(s2, parentScope ++ currentScope, Map(), isFunc, retErrs1)
      (renamedast.If(renamedCond, renamedThen, renamedElse), currentScope, retErrs2)

    case ast.While(cond, body) =>
      val (renamedCond, retErrs) = renameExpr(cond, parentScope ++ currentScope, errs)
      val (renamedBody, _, retErrs1) =
        renameStmt(body, parentScope ++ currentScope, Map(), isFunc, retErrs)
      (renamedast.While(renamedCond, renamedBody), currentScope, retErrs1)

    case ast.Begin(body) =>
      val (renamedBody, _, retErrs) = renameStmt(body, parentScope, currentScope, isFunc, errs)
      (
        renamedBody,
        currentScope,
        retErrs
      )

    case ast.Semi(s1, s2) =>
      val (renamedS1, currentScopeS1, retErrs) =
        renameStmt(s1, parentScope, currentScope, isFunc, errs)
      val (renamedS2, currentScopeS2, retErrs1) =
        renameStmt(s2, parentScope, currentScopeS1, isFunc, retErrs)
      (
        renamedast.Semi(renamedS1, renamedS2),
        currentScopeS2,
        retErrs1
      )
  }

  /** Rename an rvalue.
   *
   * @param r     The rvalue to rename
   * @param scope The scope of the rvalue
   * @return The renamed rvalue
   */
  private def renameRValue(
      r: ast.RValue,
      scope: Scope,
      errs: List[WaccError]
  ): (renamedast.RValue, List[WaccError]) =
    r match {
      case ast.ArrayLiter(es) =>
        val (renamedEs, retErrs) = es.foldLeft((List.empty[renamedast.Expr], errs)) {
          case ((acc, accErrs), e) =>
            val (renamedE, newErrs) = renameExpr(e, scope, accErrs)
            (acc :+ renamedE, newErrs)
        }
        (renamedast.ArrayLiter(renamedEs), retErrs)
      case ast.NewPair(e1, e2) =>
        val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
        val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
        (renamedast.NewPair(renamedE1, renamedE2), retErrs2)
      case ast.Fst(l) =>
        val (renamedL, retErrs) = renameLValue(l, scope, errs)
        (renamedast.Fst(renamedL), retErrs)
      case ast.Snd(l) =>
        val (renamedL, retErrs) = renameLValue(l, scope, errs)
        (renamedast.Snd(renamedL), retErrs)
      case ast.Call(v, args) =>
        val (renamedArgs, retErrs) = args.foldLeft((List.empty[renamedast.Expr], errs)) {
          case ((acc, accErrs), arg) =>
            val (renamedArg, newErrs) = renameExpr(arg, scope, accErrs)
            (acc :+ renamedArg, newErrs)
        }
        var moddedErrs = retErrs

        // Check if the function is declared
        val renamedIdent = if (!functionIds.contains(v.v)) {
          moddedErrs = constructSpecialised(
        v.pos,
        v.v,
        "Attempted usage of undefined function"
          ) :: moddedErrs
          renamedast.Ident(QualifiedName(v.v, generateUid(), ?))
        } else {
          val (fqn, argLen) = functionIds(v.v)

          // Check if the number of arguments is correct
          if (args.length != argLen) {
        moddedErrs = constructSpecialised(
          v.pos,
          v.v,
          "Incorrect number of argument passed into function"
        ) :: moddedErrs
          }
          renamedast.Ident(fqn)
        }

        (renamedast.Call(renamedIdent, renamedArgs), moddedErrs)
      case e: ast.Expr => renameExpr(e, scope, errs)
    }

  private def renameIdent(v: ast.Ident, scope: Scope, errs: List[WaccError]): (renamedast.Ident, List[WaccError]) = {
    if (!scope.contains(v.v)) {
      // TODO: Error handling
      (renamedast.Ident(QualifiedName(v.v, generateUid(), ?)), constructSpecialised(
        v.pos,
        v.v,
        "Attempted usage of undefined variable"
      ) :: errs)
    } else {
      (renamedast.Ident(scope(v.v)), errs)
    }
  }

  private def renameArrayElem(
      v: ast.Ident,
      es: List[ast.Expr],
      scope: Scope,
      errs: List[WaccError]
  ): (renamedast.ArrayElem, List[WaccError]) = {
    val (renamedIdent, retErrs) = renameIdent(v, scope, errs)
    val (renamedExprs, finalErrs) = es.foldLeft((List.empty[renamedast.Expr], retErrs)) {
      case ((acc, accErrs), e) =>
        val (renamedE, newErrs) = renameExpr(e, scope, accErrs)
        (acc :+ renamedE, newErrs)
    }
    (renamedast.ArrayElem(renamedIdent, renamedExprs), finalErrs)
  }

  /** Rename an lvalue.
   *
   * @param l     The lvalue to rename
   * @param scope The scope of the lvalue
   * @return The renamed lvalue
   */
  private def renameLValue(l: ast.LValue, scope: Scope, errs: List[WaccError]): (renamedast.LValue, List[WaccError]) =
    l match {
      case ast.Fst(l)           => renameLValue(l, scope, errs) match {
        case (renamedL, errs) => (renamedast.Fst(renamedL), errs)
      }
      case ast.Snd(l)           => renameLValue(l, scope, errs) match {
        case (renamedL, errs) => (renamedast.Snd(renamedL), errs)
      }
      case id@ast.Ident(_)         => renameIdent(id, scope, errs)
      case ast.ArrayElem(v, es) => renameArrayElem(v, es, scope, errs)
    }

  /** Rename an expression.
   *
   * @param e     The expression to rename
   * @param scope The scope of the expression
   * @return The renamed expression
   */
  private def renameExpr(e: ast.Expr, scope: Scope, errs: List[WaccError]): (renamedast.Expr, List[WaccError]) = e match {
    case ast.Not(e) =>
      val (renamedE, retErrs) = renameExpr(e, scope, errs)
      (renamedast.Not(renamedE), retErrs)
    case ast.Negate(e) =>
      val (renamedE, retErrs) = renameExpr(e, scope, errs)
      (renamedast.Negate(renamedE), retErrs)
    case ast.Len(e) =>
      val (renamedE, retErrs) = renameExpr(e, scope, errs)
      (renamedast.Len(renamedE), retErrs)
    case ast.Ord(e) =>
      val (renamedE, retErrs) = renameExpr(e, scope, errs)
      (renamedast.Ord(renamedE), retErrs)
    case ast.Chr(e) =>
      val (renamedE, retErrs) = renameExpr(e, scope, errs)
      (renamedast.Chr(renamedE), retErrs)
    case ast.Mult(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Mult(renamedE1, renamedE2), retErrs2)
    case ast.Div(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Div(renamedE1, renamedE2), retErrs2)
    case ast.Mod(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Mod(renamedE1, renamedE2), retErrs2)
    case ast.Add(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Add(renamedE1, renamedE2), retErrs2)
    case ast.Sub(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Sub(renamedE1, renamedE2), retErrs2)
    case ast.Greater(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Greater(renamedE1, renamedE2), retErrs2)
    case ast.GreaterEq(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.GreaterEq(renamedE1, renamedE2), retErrs2)
    case ast.Smaller(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Smaller(renamedE1, renamedE2), retErrs2)
    case ast.SmallerEq(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.SmallerEq(renamedE1, renamedE2), retErrs2)
    case ast.Equals(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Equals(renamedE1, renamedE2), retErrs2)
    case ast.NotEquals(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.NotEquals(renamedE1, renamedE2), retErrs2)
    case ast.And(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.And(renamedE1, renamedE2), retErrs2)
    case ast.Or(e1, e2) =>
      val (renamedE1, retErrs1) = renameExpr(e1, scope, errs)
      val (renamedE2, retErrs2) = renameExpr(e2, scope, retErrs1)
      (renamedast.Or(renamedE1, renamedE2), retErrs2)
    case ast.IntLiter(x) =>
      (renamedast.IntLiter(x), errs)
    case ast.BoolLiter(b) =>
      (renamedast.BoolLiter(b), errs)
    case ast.CharLiter(c) =>
      (renamedast.CharLiter(c), errs)
    case ast.StringLiter(s) =>
      (renamedast.StringLiter(s), errs)
    case ast.PairLiter() =>
      (renamedast.PairLiter, errs)
    case id@ast.Ident(_) =>
      renameIdent(id, scope, errs)
    case ast.ArrayElem(v, es) =>
      renameArrayElem(v, es, scope, errs)
  }
}
