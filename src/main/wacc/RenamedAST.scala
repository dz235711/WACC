package wacc

sealed class RenamedAST
object RenamedAST {
  case class QualifiedName(
      originalName: String,
      UID: Int,
      declType: SemType
  )
  
  sealed trait PositionalNode {
    val pos: (Int, Int)
  }
  
  sealed abstract class SemType
  case object ? extends SemType
  
  enum KnownType extends SemType {
    case IntType extends KnownType
    case BoolType extends KnownType
    case CharType extends KnownType
    case StringType extends KnownType
    case ArrayType(ty: SemType) extends KnownType
    case PairType(t1: SemType, t2: SemType) extends KnownType
  }
  
  /** Converts SemType to String
    *
    * @param ty the SemType
    * @return the name of the type
    */
  def getTypeName(ty: SemType): String = ty match {
    case KnownType.IntType          => "int"
    case KnownType.BoolType         => "bool"
    case KnownType.CharType         => "char"
    case KnownType.StringType       => "string"
    case KnownType.ArrayType(t)     => s"${getTypeName(t)}[]"
    case KnownType.PairType(t1, t2) => s"pair(${getTypeName(t1)}, ${getTypeName(t2)})"
    case ?                          => "unknown"
  }
  
  sealed trait Expr extends RValue, PositionalNode
  case class Not(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Negate(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Len(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Ord(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Chr(e: Expr)(val pos: (Int, Int)) extends Expr
  
  case class Mult(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Div(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Mod(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Add(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Sub(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Greater(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class GreaterEq(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Smaller(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class SmallerEq(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Equals(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class NotEquals(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class And(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  case class Or(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends Expr
  
  case class IntLiter(x: Int)(val pos: (Int, Int)) extends Expr
  case class BoolLiter(b: Boolean)(val pos: (Int, Int)) extends Expr
  case class CharLiter(c: Char)(val pos: (Int, Int)) extends Expr
  case class StringLiter(s: String)(val pos: (Int, Int)) extends Expr
  case class PairLiter()(val pos: (Int, Int)) extends Expr
  case class Ident(v: QualifiedName)(val pos: (Int, Int)) extends Expr with LValue
  case class ArrayElem(v: Ident, es: List[Expr])(val pos: (Int, Int)) extends Expr with LValue
  case class NestedExpr(e: Expr)(val pos: (Int, Int)) extends Expr
  
  sealed trait LValue extends PositionalNode
  sealed trait RValue extends PositionalNode
  case class ArrayLiter(es: List[Expr])(val pos: (Int, Int)) extends RValue
  case class NewPair(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends RValue
  case class Fst(l: LValue)(val pos: (Int, Int)) extends LValue, RValue
  case class Snd(l: LValue)(val pos: (Int, Int)) extends LValue, RValue
  case class Call(v: Ident, args: List[Expr])(val pos: (Int, Int)) extends RValue
  
  sealed trait Stmt extends PositionalNode
  case class Skip()(val pos: (Int, Int)) extends Stmt
  case class Decl(v: Ident, r: RValue)(val pos: (Int, Int)) extends Stmt
  case class Asgn(l: LValue, r: RValue)(val pos: (Int, Int)) extends Stmt
  case class Read(l: LValue)(val pos: (Int, Int)) extends Stmt
  case class Free(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class Return(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class Exit(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class Print(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class PrintLn(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class If(cond: Expr, s1: Stmt, s2: Stmt)(val pos: (Int, Int)) extends Stmt
  case class While(cond: Expr, body: Stmt)(val pos: (Int, Int)) extends Stmt
  case class Begin(body: Stmt)(val pos: (Int, Int)) extends Stmt
  case class Semi(s1: Stmt, s2: Stmt)(val pos: (Int, Int)) extends Stmt
  
  case class Func(v: Ident, params: List[Ident], body: Stmt)(val pos: (Int, Int))
  
  case class Program(fs: List[Func], body: Stmt)(val pos: (Int, Int))

}