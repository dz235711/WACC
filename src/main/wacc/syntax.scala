package wacc

import parsley.generic

sealed trait Type
sealed trait PairElemType
enum BaseType extends Type, PairElemType {
  case Int
  case Bool
  case Char
  case String
}
case class ArrayType(t: Type) extends Type, PairElemType
case class PairType(t1: PairElemType, t2: PairElemType) extends Type
object ErasedPair extends PairElemType

sealed trait Expr extends RValue
case class Not(e: Expr) extends Expr
object Not extends generic.ParserBridge1[Expr, Expr]
case class Negate(e: Expr) extends Expr
object Negate extends generic.ParserBridge1[Expr, Expr]
case class Len(e: Expr) extends Expr
object Len extends generic.ParserBridge1[Expr, Expr]
case class Ord(e: Expr) extends Expr
object Ord extends generic.ParserBridge1[Expr, Expr]
case class Chr(e: Expr) extends Expr
object Chr extends generic.ParserBridge1[Expr, Expr]

case class Mult(e1: Expr, e2: Expr) extends Expr
object Mult extends generic.ParserBridge2[Expr, Expr, Expr]
case class Div(e1: Expr, e2: Expr) extends Expr
object Div extends generic.ParserBridge2[Expr, Expr, Expr]
case class Mod(e1: Expr, e2: Expr) extends Expr
object Mod extends generic.ParserBridge2[Expr, Expr, Expr]
case class Add(e1: Expr, e2: Expr) extends Expr
object Add extends generic.ParserBridge2[Expr, Expr, Expr]
case class Sub(e1: Expr, e2: Expr) extends Expr
object Sub extends generic.ParserBridge2[Expr, Expr, Expr]
case class Greater(e1: Expr, e2: Expr) extends Expr
object Greater extends generic.ParserBridge2[Expr, Expr, Expr]
case class GreaterEq(e1: Expr, e2: Expr) extends Expr
object GreaterEq extends generic.ParserBridge2[Expr, Expr, Expr]
case class Smaller(e1: Expr, e2: Expr) extends Expr
object Smaller extends generic.ParserBridge2[Expr, Expr, Expr]
case class SmallerEq(e1: Expr, e2: Expr) extends Expr
object SmallerEq extends generic.ParserBridge2[Expr, Expr, Expr]
case class Equals(e1: Expr, e2: Expr) extends Expr
object Equals extends generic.ParserBridge2[Expr, Expr, Expr]
case class NotEquals(e1: Expr, e2: Expr) extends Expr
object NotEquals extends generic.ParserBridge2[Expr, Expr, Expr]
case class And(e1: Expr, e2: Expr) extends Expr
object And extends generic.ParserBridge2[Expr, Expr, Expr]
case class Or(e1: Expr, e2: Expr) extends Expr
object Or extends generic.ParserBridge2[Expr, Expr, Expr]

case class IntLiter(x: Int) extends Expr
object IntLiter extends generic.ParserBridge1[Int, Expr]
case class BoolLiter(b: Boolean) extends Expr
object BoolLiter extends generic.ParserBridge1[Boolean, Expr]
case class CharLiter(c: Char) extends Expr
object CharLiter extends generic.ParserBridge1[Char, Expr]
case class StringLiter(s: String) extends Expr
object StringLiter extends generic.ParserBridge1[String, Expr]
case object PairLiter extends Expr, generic.ParserBridge0[Expr]
case class Ident(v: String) extends Expr with LValue
object Ident extends generic.ParserBridge1[String, Ident]
case class ArrayElem(v: Ident, es: List[Expr]) extends Expr with LValue
object ArrayElem extends generic.ParserBridge2[Ident, List[Expr], ArrayElem]
case class NestedExpr(e: Expr) extends Expr
object NestedExpr extends generic.ParserBridge1[Expr, Expr]

sealed trait LValue

sealed trait RValue
case class ArrayLiter(es: List[Expr]) extends RValue
case class NewPair(e1: Expr, e2: Expr) extends RValue
case class Fst(l: LValue) extends LValue, RValue
case class Snd(l: LValue) extends LValue, RValue
case class Call(v: Ident, args: List[Expr]) extends RValue

sealed trait Stmt
object Skip extends Stmt
case class Decl(t: Type, v: Ident, r: RValue) extends Stmt
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

case class Func(t: Type, v: Ident, params: List[(Type, Ident)], body: Stmt)

case class Program(fs: List[Func], body: Stmt)
