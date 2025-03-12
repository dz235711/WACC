package wacc

import RenamedAST.{KnownType, QualifiedName, ?}
import scala.collection.mutable
import WaccErrorBuilder.constructSpecialised

type Scope = Map[String, QualifiedName]

class Renamer private {
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
   * @return The renamed program (RenamedAST)
   */
  def renameProgram(p: SyntaxAST.Program, imports: List[SyntaxAST.Program])(using
      ctx: ListContext[WaccError]
  ): RenamedAST.Program = {
    // Get the imported functions and create a list of all functions
    val allFuncs = imports.flatMap(_.fs) ++ p.fs
    // Generate unique identifiers for all functions
    val fids = allFuncs.map(f => {
      val (t, id) = f.decl
      val name = id.v
      val uid = generateUid()
      val fqn = QualifiedName(name, uid, translateType(t))

      // Check for redeclaration of function
      if (functionIds.contains(name)) {
        ctx.add(
          constructSpecialised(
            id.pos,
            name.length,
            s"Illegal function redeclaration $name"
          )
        )
      } else {
        // Add the function to the functionIds map if it is not already declared
        functionIds += (name -> (fqn, f.params.length))
      }

      fqn
    })

    // Rename all functions and the body
    val renamedFuncs = allFuncs.zip(fids).map(renameFunc)
    val renamedBody = renameStmt(p.body, Map(), Map(), false)._1

    // Return the renamed program
    RenamedAST.Program(renamedFuncs, renamedBody)(p.pos)
  }

  /** Translates between a syntactic type and a semantic type.
   *
   * @param synType The syntactic type
   * @return The semantic type
   * */
  private def translateType(synType: SyntaxAST.Type | SyntaxAST.PairElemType): KnownType = synType match {
    case SyntaxAST.IntType()    => KnownType.IntType
    case SyntaxAST.BoolType()   => KnownType.BoolType
    case SyntaxAST.CharType()   => KnownType.CharType
    case SyntaxAST.StringType() => KnownType.StringType
    case SyntaxAST.ArrayType(t) => KnownType.ArrayType(translateType(t))
    case SyntaxAST.ErasedPair() => KnownType.PairType(?, ?)
    case SyntaxAST.PairType(t1, t2) =>
      KnownType.PairType(translateType(t1), translateType(t2))
  }

  /** Rename a function.
   *
   * @param f             The function to rename
   * @param qualifiedName The qualified name of the function
   * @return The renamed function
   */
  private def renameFunc(
      f: SyntaxAST.Func,
      qualifiedName: QualifiedName
  )(using ctx: ListContext[WaccError]): RenamedAST.Func = {
    // Construct a map of the parameters to use as a scope for the body
    val funcScope: Scope = paramsToScope(f.params)
    val renamedBody = renameStmt(f.body, funcScope, Map(), true)._1

    RenamedAST.Func(
      RenamedAST.Ident(qualifiedName)(f.decl._2.pos),
      f.params.map(p => RenamedAST.Ident(funcScope(p._2.v))(p._2.pos)),
      renamedBody
    )(f.pos)
  }

  /** Convert a function's parameters to a scope.
   *
   * @param params The parameters of the function
   * @return The scope of the parameters
   */
  private def paramsToScope(
      params: List[SyntaxAST.Param]
  )(using ctx: ListContext[WaccError]): Scope =
    params.foldLeft(Map())((params, param) => {
      val (t, id) = param

      // Check for redeclaration of parameter
      if (params.contains(id.v)) {
        ctx.add(
          constructSpecialised(
            id.pos,
            id.v.length,
            s"Illegal parameter redeclaration ${id.v}"
          )
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
  private def renameStmt(
      stmt: SyntaxAST.Stmt,
      parentScope: Scope,
      currentScope: Scope,
      isFunc: Boolean
  )(using ctx: ListContext[WaccError]): (RenamedAST.Stmt, Scope) = stmt match {

    case SyntaxAST.Skip() => (RenamedAST.Skip()(stmt.pos), currentScope)

    case SyntaxAST.Decl(t, v, r) =>
      val name = QualifiedName(v.v, generateUid(), translateType(t))
      val renamedR = renameRValue(r, parentScope ++ currentScope)
      val renamedDecl =
        RenamedAST.Decl(RenamedAST.Ident(name)(v.pos), renamedR)(stmt.pos)

      // Check if the variable is already declared in the current scope
      if (currentScope.contains(v.v)) {
        ctx.add(
          constructSpecialised(
            v.pos,
            v.v.length,
            s"Illegal variable redeclaration ${v.v}"
          )
        )
        (renamedDecl, currentScope)
      } else {
        /* Only add the variable to the current scope if it is not already declared */
        (renamedDecl, currentScope + (v.v -> name))
      }

    case SyntaxAST.Asgn(l, r) =>
      (
        RenamedAST.Asgn(
          renameLValue(l, parentScope ++ currentScope),
          renameRValue(r, parentScope ++ currentScope)
        )(stmt.pos),
        currentScope
      )

    case SyntaxAST.Read(l) =>
      (
        RenamedAST.Read(renameLValue(l, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.Free(e) =>
      (
        RenamedAST.Free(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.Return(e) =>
      if (!isFunc) {
        ctx.add(
          constructSpecialised(
            stmt.pos,
            6, // Length of "return"
            "Return statement outside of function"
          )
        )
      }
      (
        RenamedAST.Return(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.Exit(e) =>
      (
        RenamedAST.Exit(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.Print(e) =>
      (
        RenamedAST.Print(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.PrintLn(e) =>
      (
        RenamedAST.PrintLn(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.If(cond, s1, s2) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedThen =
        renameStmt(s1, parentScope ++ currentScope, Map(), isFunc)._1
      val renamedElse =
        renameStmt(s2, parentScope ++ currentScope, Map(), isFunc)._1
      (RenamedAST.If(renamedCond, renamedThen, renamedElse)(stmt.pos), currentScope)

    case SyntaxAST.While(cond, body) =>
      val renamedCond = renameExpr(cond, parentScope ++ currentScope)
      val renamedBody =
        renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
      (RenamedAST.While(renamedCond, renamedBody)(stmt.pos), currentScope)

    case SyntaxAST.Begin(body) =>
      (
        RenamedAST.Begin(
          renameStmt(body, parentScope ++ currentScope, Map(), isFunc)._1
        )(stmt.pos),
        currentScope
      )

    case SyntaxAST.Semi(s1, s2) =>
      val (renamedS1, currentScopeS1) =
        renameStmt(s1, parentScope, currentScope, isFunc)
      val (renamedS2, currentScopeS2) =
        renameStmt(s2, parentScope, currentScopeS1, isFunc)
      (
        RenamedAST.Semi(renamedS1, renamedS2)(stmt.pos),
        currentScopeS2
      )

    case SyntaxAST.Throw(e) =>
      (
        RenamedAST.Throw(renameExpr(e, parentScope ++ currentScope))(stmt.pos),
        currentScope
      )

    case SyntaxAST.TryCatchFinally(tryBody, catchIdent, catchBody, finallyBody) =>
      val identName = QualifiedName(catchIdent.v, generateUid(), KnownType.IntType)
      val renamedTry =
        renameStmt(tryBody, parentScope ++ currentScope, Map(), isFunc)._1
      val renamedCatch =
        renameStmt(catchBody, parentScope ++ currentScope + (catchIdent.v -> identName), Map(), isFunc)._1
      val renamedFinally =
        renameStmt(finallyBody, parentScope ++ currentScope, Map(), isFunc)._1
      (
        RenamedAST.TryCatchFinally(
          renamedTry,
          RenamedAST.Ident(identName)(catchIdent.pos),
          renamedCatch,
          renamedFinally
        )(stmt.pos),
        currentScope
      )
  }

  /** Rename an rvalue.
   *
   * @param r     The rvalue to rename
   * @param scope The scope of the rvalue
   * @return The renamed rvalue
   */
  private def renameRValue(r: SyntaxAST.RValue, scope: Scope)(using
      ctx: ListContext[WaccError]
  ): RenamedAST.RValue =
    r match {
      case SyntaxAST.ArrayLiter(es) =>
        RenamedAST.ArrayLiter(es.map(renameExpr(_, scope)))(r.pos)
      case SyntaxAST.NewPair(e1, e2) =>
        RenamedAST.NewPair(renameExpr(e1, scope), renameExpr(e2, scope))(r.pos)
      case SyntaxAST.Fst(l) => RenamedAST.Fst(renameLValue(l, scope))(r.pos)
      case SyntaxAST.Snd(l) => RenamedAST.Snd(renameLValue(l, scope))(r.pos)
      case SyntaxAST.Call(v, args) =>
        val renamedArgs = args.map(renameExpr(_, scope))

        // Check if the function is declared
        val renamedIdent = if (!functionIds.contains(v.v)) {
          ctx.add(
            constructSpecialised(
              v.pos,
              v.v.length,
              s"Attempted usage of undefined function ${v.v}"
            )
          )
          RenamedAST.Ident(QualifiedName(v.v, generateUid(), ?))
        } else {
          val (fqn, argLen) = functionIds(v.v)

          // Check if the number of arguments is correct
          if (args.length != argLen) {
            ctx.add(
              constructSpecialised(
                (v.pos._1, v.pos._2 + v.v.length),
                1,
                s"Incorrect number of arguments for function ${v.v}"
              )
            )
          }
          RenamedAST.Ident(fqn)
        }

        RenamedAST.Call(renamedIdent(v.pos), renamedArgs)(r.pos)
      case e: SyntaxAST.Expr => renameExpr(e, scope)
    }

  private def renameIdent(v: SyntaxAST.Ident, scope: Scope)(using
      ctx: ListContext[WaccError]
  ): RenamedAST.Ident = {
    if (!scope.contains(v.v)) {
      ctx.add(
        constructSpecialised(
          v.pos,
          v.v.length,
          s"Attempted usage of undefined variable ${v.v}"
        )
      )
      RenamedAST.Ident(QualifiedName(v.v, generateUid(), ?))(v.pos)
    } else {
      RenamedAST.Ident(scope(v.v))(v.pos)
    }
  }

  private def renameArrayElem(
      arrElem: SyntaxAST.ArrayElem,
      scope: Scope
  )(using ctx: ListContext[WaccError]): RenamedAST.ArrayElem = {
    val renamedIdent = renameIdent(arrElem.ident, scope)
    RenamedAST.ArrayElem(renamedIdent, arrElem.es.map(renameExpr(_, scope)))(arrElem.pos)
  }

  /** Rename an lvalue.
   *
   * @param l     The lvalue to rename
   * @param scope The scope of the lvalue
   * @return The renamed lvalue
   */
  private def renameLValue(l: SyntaxAST.LValue, scope: Scope)(using
      ctx: ListContext[WaccError]
  ): RenamedAST.LValue =
    l match {
      case SyntaxAST.Fst(lNested) => RenamedAST.Fst(renameLValue(lNested, scope))(l.pos)
      case SyntaxAST.Snd(lNested) => RenamedAST.Snd(renameLValue(lNested, scope))(l.pos)
      case l: SyntaxAST.Ident     => renameIdent(l, scope)
      case l: SyntaxAST.ArrayElem => renameArrayElem(l, scope)
    }

  /** Rename an expression.
   *
   * @param e     The expression to rename
   * @param scope The scope of the expression
   * @return The renamed expression
   */
  private def renameExpr(e: SyntaxAST.Expr, scope: Scope)(using ctx: ListContext[WaccError]): RenamedAST.Expr =
    e match {
      case SyntaxAST.Not(eNested)    => RenamedAST.Not(renameExpr(eNested, scope))(e.pos)
      case SyntaxAST.Negate(eNested) => RenamedAST.Negate(renameExpr(eNested, scope))(e.pos)
      case SyntaxAST.Len(eNested)    => RenamedAST.Len(renameExpr(eNested, scope))(e.pos)
      case SyntaxAST.Ord(eNested)    => RenamedAST.Ord(renameExpr(eNested, scope))(e.pos)
      case SyntaxAST.Chr(eNested)    => RenamedAST.Chr(renameExpr(eNested, scope))(e.pos)
      case SyntaxAST.Mult(e1, e2) =>
        RenamedAST.Mult(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Div(e1, e2) =>
        RenamedAST.Div(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Mod(e1, e2) =>
        RenamedAST.Mod(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Add(e1, e2) =>
        RenamedAST.Add(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Sub(e1, e2) =>
        RenamedAST.Sub(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Greater(e1, e2) =>
        RenamedAST.Greater(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.GreaterEq(e1, e2) =>
        RenamedAST.GreaterEq(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Smaller(e1, e2) =>
        RenamedAST.Smaller(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.SmallerEq(e1, e2) =>
        RenamedAST.SmallerEq(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Equals(e1, e2) =>
        RenamedAST.Equals(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.NotEquals(e1, e2) =>
        RenamedAST.NotEquals(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.And(e1, e2) =>
        RenamedAST.And(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.Or(e1, e2) =>
        RenamedAST.Or(renameExpr(e1, scope), renameExpr(e2, scope))(e.pos)
      case SyntaxAST.IntLiter(x)    => RenamedAST.IntLiter(x)(e.pos)
      case SyntaxAST.BoolLiter(b)   => RenamedAST.BoolLiter(b)(e.pos)
      case SyntaxAST.CharLiter(c)   => RenamedAST.CharLiter(c)(e.pos)
      case SyntaxAST.StringLiter(s) => RenamedAST.StringLiter(s)(e.pos)
      case SyntaxAST.PairLiter()    => RenamedAST.PairLiter()(e.pos)
      case e: SyntaxAST.Ident       => renameIdent(e, scope)
      case e: SyntaxAST.ArrayElem   => renameArrayElem(e, scope)
    }
}

object Renamer extends Renamer {
  def rename(p: SyntaxAST.Program, imports: List[SyntaxAST.Program])(using
      ctx: ListContext[WaccError]
  ): RenamedAST.Program =
    val renamer = new Renamer()
    renamer.renameProgram(p, imports)
}
