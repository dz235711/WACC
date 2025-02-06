package wacc

import wacc.renamedast.{SemType, ?}
import wacc.renamedast.KnownType.*

enum Constraint {
  case Is(refTy: SemType)
  case IsInt
  case IsBool
  case IsChar
  case IsString
  case IsReadable
}
object Constraint {
  val Unconstrained = Is(?)
  val IsArray = Is(Array(?))
  val IsPair = Is(Pair(?, ?))
  /* TODO: Add constraint for invariant types - e.g. the string in string[] is
   * invariant so we can't weaken it to char[], similarly for pairs */
}
