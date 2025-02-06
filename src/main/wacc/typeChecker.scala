package wacc

import wacc.renamedast.{SemType, ?}
import wacc.renamedast.KnownType.*

enum Constraint {
  case Is(refTy: SemType)
  case IsReadable
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
      case (_, IsReadable) =>
        // TODO: Error handling
        None
      case _ => None
    }

  def checkProgram(p: renamedast.Program): TypedAST.Program = ???
}
