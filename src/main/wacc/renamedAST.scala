package wacc:
  package renamedast {
    case class QualifiedName(
      originalName: String,
      UID: Int,
      declType: SemType
    )

    sealed abstract class SemType
    case object ? extends SemType
    enum KnownType extends SemType {
      case Int
      case Bool
      case Char
      case String
      case Array(ty: SemType)
      case Pair(t1: SemType, t2: SemType)
    }

    sealed trait Expr extends RValue
    case class Not(e: Expr) extends Expr
    case class Negate(e: Expr) extends Expr
    case class Len(e: Expr) extends Expr
    case class Ord(e: Expr) extends Expr
    case class Chr(e: Expr) extends Expr

    case class Mult(e1: Expr, e2: Expr) extends Expr
    case class Div(e1: Expr, e2: Expr) extends Expr
    case class Mod(e1: Expr, e2: Expr) extends Expr
    case class Add(e1: Expr, e2: Expr) extends Expr
    case class Sub(e1: Expr, e2: Expr) extends Expr
    case class Greater(e1: Expr, e2: Expr) extends Expr
    case class GreaterEq(e1: Expr, e2: Expr) extends Expr
    case class Smaller(e1: Expr, e2: Expr) extends Expr
    case class SmallerEq(e1: Expr, e2: Expr) extends Expr
    case class Equals(e1: Expr, e2: Expr) extends Expr
    case class NotEquals(e1: Expr, e2: Expr) extends Expr
    case class And(e1: Expr, e2: Expr) extends Expr
    case class Or(e1: Expr, e2: Expr) extends Expr

    case class IntLiter(x: Int) extends Expr
    case class BoolLiter(b: Boolean) extends Expr
    case class CharLiter(c: Char) extends Expr
    case class StringLiter(s: String) extends Expr
    object PairLiter extends Expr
    case class Ident(v: QualifiedName) extends Expr with LValue
    case class ArrayElem(v: Ident, es: List[Expr]) extends Expr with LValue
    case class NestedExpr(e: Expr) extends Expr

    sealed trait LValue
    sealed trait RValue
    case class ArrayLiter(es: List[Expr]) extends RValue
    case class NewPair(e1: Expr, e2: Expr) extends RValue
    case class Fst(l: LValue) extends LValue, RValue
    case class Snd(l: LValue) extends LValue, RValue
    case class Call(v: Ident, args: List[Expr]) extends RValue

    sealed trait Stmt
    object Skip extends Stmt
    case class Decl(v: Ident, r: RValue) extends Stmt
    case class Asgn(l: LValue, r: RValue) extends Stmt
    case class Read(l: LValue) extends Stmt
    case class Free(e: Expr) extends Stmt
    case class Return(e: Expr) extends Stmt
    case class Exit(e: Expr) extends Stmt
    case class Print(e: Expr) extends Stmt
    case class PrintLn(e: Expr) extends Stmt
    case class If(cond: Expr, s1: Stmt, s2: Stmt) extends Stmt
    case class While(cond: Expr, body: Stmt) extends Stmt
    case class Begin(body: Stmt) extends Stmt
    case class Semi(s1: Stmt, s2: Stmt) extends Stmt

    case class Func(v: Ident, params: List[Ident], body: Stmt)

    case class Program(fs: List[Func], body: Stmt)
  }
