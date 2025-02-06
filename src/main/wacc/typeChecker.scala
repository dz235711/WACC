package wacc

import wacc.renamedast.{?, KnownType, SemType}
import wacc.renamedast.KnownType.*

enum Constraint {
  case Is(refTy: SemType)
  case IsInt
  case IsBool
  case IsChar
  case IsString
  case IsReadable
  case IsOrderable
  case IsFreeable
}
object Constraint {
  val Unconstrained: Constraint = Is(?)
  val IsArray: Constraint = Is(Array(?))
  val IsPair: Constraint = Is(Pair(?, ?))
  /* TODO: Add constraint for invariant types - e.g. the string in string[] is
   * invariant so we can't weaken it to char[], similarly for pairs */
}

sealed class TypeChecker {
  import Constraint.*

  /** Determines whether two types are equal, and if so, what the most specific of them is. */
  extension (ty: SemType)
    private def ~(refTy: SemType): Option[SemType] = (ty, refTy) match {
      case (?, refTy)                => Some(refTy)
      case (ty, ?)                   => Some(ty)
      case (Array(ty), Array(refTy)) => (ty ~ refTy).map(Array.apply)
      case (Pair(ty1, ty2), Pair(refTy1, refTy2)) =>
        for {
          newTy1 <- ty1 ~ refTy1
          newTy2 <- ty2 ~ refTy2
        } yield Pair(newTy1, newTy2)
      case (ty, refTy) if ty == refTy => Some(ty)
      case _                          => None
    }

  /** Determines whether a type satisfies a constraint. */
  extension (ty: SemType)
    private def satisfies(c: Constraint): Option[SemType] = (ty, c) match {
      case (ty, Is(refTy)) =>
        (ty ~ refTy).orElse {
          // TODO: Error handling?
          None
        }
      case (?, _) => Some(?) // Unconstrained types satisfy all constraints
      case (kty @ Array(_), IsArray)        => Some(kty)
      case (kty @ Pair(_, _), IsPair)       => Some(kty)
      case (kty @ (Int | Char), IsReadable) => Some(kty)
      case (_, IsReadable)                  =>
        // TODO: Error handling
        None
      case (kty @ (Int | Char), IsOrderable) => Some(kty)
      case (_, IsOrderable)                  =>
        // TODO: Error handling
        None
      case (kty @ (Array(_) | Pair(_, _)), IsFreeable) => Some(kty)
      case (_, IsFreeable)                             =>
        // TODO: Error handling
        None
      case _ => None
    }

  def checkProg(p: renamedast.Program): TypedAST.Program = ???

  private def checkFunc(func: renamedast.Func): TypedAST.Func = ???

  /** Checks a statement and returns a typed statement.
   *
   * @param stmt The statement to check
   * @param retC The constraint on the return type of the statement
   * @return The typed statement
   */
  private def checkStmt(
      stmt: renamedast.Stmt,
      retC: Constraint
  ): TypedAST.Stmt = stmt match {
    case renamedast.Skip => TypedAST.Skip
    case renamedast.Decl(v, r) =>
      val (ty, vTyped) = checkIdent(v, Unconstrained)
      // Identifier in declaration *will* have a type
      val (_, rTyped) = checkRVal(r, Is(ty.get))
      TypedAST.Decl(vTyped, rTyped)
    case renamedast.Asgn(l, r) =>
      val (lTy, lTyped) = checkLVal(l, Unconstrained)
      val (rTy, rTyped) = checkRVal(r, Is(lTy.getOrElse(?)))

      // Make sure the assignment has a known type
      rTy match {
        case Some(?) => {
          // TODO: Error handling
        }
        case _ => ()
      }

      TypedAST.Asgn(lTyped, rTyped)
    case renamedast.Read(l)   => TypedAST.Read(checkLVal(l, IsReadable)._2)
    case renamedast.Free(e)   => TypedAST.Free(checkExpr(e, IsFreeable)._2)
    case renamedast.Return(e) => TypedAST.Return(checkExpr(e, retC)._2)
    case renamedast.Exit(e)   => TypedAST.Exit(checkExpr(e, IsInt)._2)
    case renamedast.Print(e)  => TypedAST.Print(checkExpr(e, Unconstrained)._2)
    case renamedast.PrintLn(e) =>
      TypedAST.PrintLn(checkExpr(e, Unconstrained)._2)
    case renamedast.If(cond, s1, s2) =>
      TypedAST.If(
        checkExpr(cond, IsBool)._2,
        checkStmt(s1, retC),
        checkStmt(s2, retC)
      )
    case renamedast.While(cond, body) =>
      TypedAST.While(
        checkExpr(cond, IsBool)._2,
        checkStmt(body, retC)
      )
    case renamedast.Begin(body) => TypedAST.Begin(checkStmt(body, retC))
    case renamedast.Semi(s1, s2) =>
      TypedAST.Semi(checkStmt(s1, retC), checkStmt(s2, retC))
  }

  private def checkExpr(
      expr: renamedast.Expr,
      c: Constraint
  ): (Option[SemType], TypedAST.Expr) = expr match {
    case renamedast.Not(e) =>
      Bool.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Bool), TypedAST.Not(checkExpr(e, IsBool)._2))
    case renamedast.Negate(e) =>
      Int.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Int), TypedAST.Negate(checkExpr(e, IsInt)._2))
    case renamedast.Len(e) =>
      Int.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Int), TypedAST.Len(checkExpr(e, IsArray)._2))
    case renamedast.Ord(e) =>
      Int.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Int), TypedAST.Ord(checkExpr(e, IsChar)._2))
    case renamedast.Chr(e) =>
      Char.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Char), TypedAST.Chr(checkExpr(e, IsInt)._2))
    case renamedast.Mult(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mult.apply)
    case renamedast.Div(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Div.apply)
    case renamedast.Mod(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mod.apply)
    case renamedast.Add(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Add.apply)
    case renamedast.Sub(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Sub.apply)
    case renamedast.Greater(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Greater.apply)
    case renamedast.GreaterEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.GreaterEq.apply)
    case renamedast.Smaller(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Smaller.apply)
    case renamedast.SmallerEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.SmallerEq.apply)
    case renamedast.Equals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.Equals.apply)
    case renamedast.NotEquals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.NotEquals.apply)
    case renamedast.And(e1, e2) => checkLogical(e1, e2, c)(TypedAST.And.apply)
    case renamedast.Or(e1, e2)  => checkLogical(e1, e2, c)(TypedAST.Or.apply)
    case renamedast.IntLiter(x) =>
      Int.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Int), TypedAST.IntLiter(x))
    case renamedast.BoolLiter(b) =>
      Bool.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Bool), TypedAST.BoolLiter(b))
    case renamedast.CharLiter(ch) =>
      Char.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Char), TypedAST.CharLiter(ch))
    case renamedast.StringLiter(s) =>
      String.satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(String), TypedAST.StringLiter(s))
    case renamedast.PairLiter =>
      Pair(?, ?).satisfies(c).getOrElse {
        // TODO: Error handling
      }
      (Some(Pair(?, ?)), TypedAST.PairLiter)
    case id @ renamedast.Ident(_) => checkIdent(id, c)
    case renamedast.ArrayElem(v, es) =>
      val esTyped = es.map(checkExpr(_, IsInt)._2)
      val (arrTy, vTyped) = checkIdent(v, IsArray)
      val elemTy = (for
        case Array(elemTy) <- arrTy
        ty <- elemTy.satisfies(c)
      yield ty).getOrElse {
        // TODO: Error handling
        ?
      }
      (Some(elemTy), TypedAST.ArrayElem(vTyped, esTyped, elemTy))
    case renamedast.NestedExpr(e) => checkExpr(e, c)
  }

  /** Checks an arithmetic expression and returns a typed arithmetic expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the return type
   * @param build The function to build the typed arithmetic expression
   * @return The typed arithmetic expression
   */
  private def checkArithmetic(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (e1: TypedAST.Expr, e2: TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    Int.satisfies(c).getOrElse {
      // TODO: Error handling
    }
    (Some(Int), build(checkExpr(e1, IsInt)._2, checkExpr(e2, IsInt)._2))

  /** Checks a comparison expression and returns a typed comparison expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the return type
   * @param build The function to build the typed comparison expression
   * @return The typed comparison expression
   */
  private def checkOrdering(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (e1: TypedAST.Expr, e2: TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    Bool.satisfies(c).getOrElse {
      // TODO: Error handling
    }
    (
      Some(Bool),
      build(checkExpr(e1, IsOrderable)._2, checkExpr(e2, IsOrderable)._2)
    )

  /** Checks an equality expression and returns a typed equality expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the return type
   * @param build The function to build the typed equality expression
   * @return The typed equality expression
   */
  private def checkEquality(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (e1: TypedAST.Expr, e2: TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    Bool.satisfies(c).getOrElse {
      // TODO: Error handling
    }
    (
      Some(Bool),
      build(checkExpr(e1, Unconstrained)._2, checkExpr(e2, Unconstrained)._2)
    )

  /** Checks a logical expression and returns a typed logical expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the return type
   * @param build The function to build the typed logical expression
   * @return The typed logical expression
   */
  private def checkLogical(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (e1: TypedAST.Expr, e2: TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    Bool.satisfies(c).getOrElse {
      // TODO: Error handling
    }
    (Some(Bool), build(checkExpr(e1, IsBool)._2, checkExpr(e2, IsBool)._2))

  private def checkLVal(
      lval: renamedast.LValue,
      c: Constraint
  ): (Option[SemType], TypedAST.LValue) = ???

  private def checkRVal(
      rval: renamedast.RValue,
      c: Constraint
  ): (Option[SemType], TypedAST.RValue) = ???

  private def checkIdent(
      ident: renamedast.Ident,
      c: Constraint
  ): (Option[SemType], TypedAST.Ident) = ???
}
