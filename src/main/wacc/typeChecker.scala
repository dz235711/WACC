package wacc

import wacc.renamedast.{SemType, ?}
import wacc.renamedast.KnownType.*

enum Constraint {
  case Is(refTy: SemType)
  case IsReadable
  case IsComparable
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
      case _ => None
    }

  def checkProg(p: renamedast.Program): TypedAST.Program = ???

  private def checkFunc(func: renamedast.Func): TypedAST.Func = ???

  /** Checks a statement and returns a typed statement.
   *
   * @param stmt The statement to check
   * @return The typed statement
   */
  private def checkStmt(stmt: renamedast.Stmt): TypedAST.Stmt = stmt match {
    case renamedast.Skip => TypedAST.Skip
    case renamedast.Decl(v, r) =>
      val (ty, rTyped) = checkRVal(r, IsReadable)
      val (_, vTyped) = checkIdent(v, Is(ty.getOrElse(?)))
      TypedAST.Decl(vTyped, rTyped)
    case renamedast.Asgn(l, r) =>
      val (ty, lTyped) = checkLVal(l, IsReadable)
      val (_, rTyped) = checkRVal(r, Is(ty.getOrElse(?)))
      TypedAST.Asgn(lTyped, rTyped)
    case renamedast.Read(l) => TypedAST.Read(checkLVal(l, IsReadable)._2)
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
