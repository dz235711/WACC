package wacc

import wacc.renamedast.{?, KnownType, SemType}
import wacc.renamedast.KnownType.*

enum Constraint {
  case Is(refTy: SemType)
  case IsReadable
  case IsComparable
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
      case (Array(ty), Array(refTy)) => ty ~ refTy
      case (Pair(ty1, ty2), Pair(refTy1, refTy2)) =>
        ty1 ~ refTy1 match {
          case Some(newTy1) =>
            ty2 ~ refTy2 match {
              case Some(newTy2) => Some(Pair(newTy1, newTy2))
              case None         => None
            }
          case None => None
        }
      case (ty, refTy) if ty == refTy => Some(ty)
      case _                          => None
    }

  /** Determines whether a type satisfies a constraint. */
  extension (ty: SemType)
    private def satisfies(c: Constraint): Option[SemType] = (ty, c) match {
      case (ty, Is(refTy)) =>
        (ty ~ refTy).orElse {
          // TODO: Error handling
          None
        }
      case (?, _) => Some(?) // Unconstrained types satisfy all constraints
      case (kty @ Array(_), IsArray)        => Some(kty)
      case (kty @ Pair(_, _), IsPair)       => Some(kty)
      case (kty @ (Int | Char), IsReadable) => Some(kty)
      case (_, IsReadable)                  =>
        // TODO: Error handling
        None
      case (kty @ (Int | Char), IsComparable) => Some(kty)
      case (_, IsComparable)                  =>
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
      val (_, rTyped) =
        checkRVal(r, Is(ty.get)) // Identifier in declaration *will* have a type
      TypedAST.Decl(vTyped, rTyped)
    case renamedast.Asgn(l, r) =>
      val (ty, lTyped) = checkLVal(l, Unconstrained)
      val (_, rTyped) = checkRVal(r, Is(ty.getOrElse(?)))

      // Make sure the assignment has a known type
      if (ty.isEmpty) {
        // TODO: Error handling
      }

      TypedAST.Asgn(lTyped, rTyped)
    case renamedast.Read(l)   => TypedAST.Read(checkLVal(l, IsReadable)._2)
    case renamedast.Free(e)   => TypedAST.Free(checkExpr(e, IsFreeable)._2)
    case renamedast.Return(e) => TypedAST.Return(checkExpr(e, retC)._2)
    case renamedast.Exit(e) => TypedAST.Exit(checkExpr(e, Is(KnownType.Int))._2)
    case renamedast.Print(e) => TypedAST.Print(checkExpr(e, Unconstrained)._2)
    case renamedast.PrintLn(e) =>
      TypedAST.PrintLn(checkExpr(e, Unconstrained)._2)
    case renamedast.If(cond, s1, s2) =>
      TypedAST.If(
        checkExpr(cond, Is(KnownType.Bool))._2,
        checkStmt(s1, retC),
        checkStmt(s2, retC)
      )
    case renamedast.While(cond, body) =>
      TypedAST.While(
        checkExpr(cond, Is(KnownType.Bool))._2,
        checkStmt(body, retC)
      )
    case renamedast.Begin(body) => TypedAST.Begin(checkStmt(body, retC))
    case renamedast.Semi(s1, s2) =>
      TypedAST.Semi(checkStmt(s1, retC), checkStmt(s2, retC))
  }

  private def checkExpr(
      expr: renamedast.Expr,
      c: Constraint
  ): (Option[SemType], TypedAST.Expr) = ???

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
