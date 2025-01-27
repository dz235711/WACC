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
object ArrayType extends generic.ParserBridge1[Type, ArrayType]
case class PairType(t1: PairElemType, t2: PairElemType) extends Type
object PairType extends generic.ParserBridge2[PairElemType, PairElemType, PairType]
object ErasedPair extends PairElemType, generic.ParserBridge0[PairElemType]

sealed trait Expr extends RValue
case class Not(e: Expr) extends Expr
object Not extends generic.ParserBridge1[Expr, Not]
case class Negate(e: Expr) extends Expr
object Negate extends generic.ParserBridge1[Expr, Negate]
case class Len(e: Expr) extends Expr
object Len extends generic.ParserBridge1[Expr, Len]
case class Ord(e: Expr) extends Expr
object Ord extends generic.ParserBridge1[Expr, Ord]
case class Chr(e: Expr) extends Expr
object Chr extends generic.ParserBridge1[Expr, Chr]

case class Mult(e1: Expr, e2: Expr) extends Expr
object Mult extends generic.ParserBridge2[Expr, Expr, Mult]
case class Div(e1: Expr, e2: Expr) extends Expr
object Div extends generic.ParserBridge2[Expr, Expr, Div]
case class Mod(e1: Expr, e2: Expr) extends Expr
object Mod extends generic.ParserBridge2[Expr, Expr, Mod]
case class Add(e1: Expr, e2: Expr) extends Expr
object Add extends generic.ParserBridge2[Expr, Expr, Add]
case class Sub(e1: Expr, e2: Expr) extends Expr
object Sub extends generic.ParserBridge2[Expr, Expr, Sub]
case class Greater(e1: Expr, e2: Expr) extends Expr
object Greater extends generic.ParserBridge2[Expr, Expr, Greater]
case class GreaterEq(e1: Expr, e2: Expr) extends Expr
object GreaterEq extends generic.ParserBridge2[Expr, Expr, GreaterEq]
case class Smaller(e1: Expr, e2: Expr) extends Expr
object Smaller extends generic.ParserBridge2[Expr, Expr, Smaller]
case class SmallerEq(e1: Expr, e2: Expr) extends Expr
object SmallerEq extends generic.ParserBridge2[Expr, Expr, SmallerEq]
case class Equals(e1: Expr, e2: Expr) extends Expr
object Equals extends generic.ParserBridge2[Expr, Expr, Equals]
case class NotEquals(e1: Expr, e2: Expr) extends Expr
object NotEquals extends generic.ParserBridge2[Expr, Expr, NotEquals]
case class And(e1: Expr, e2: Expr) extends Expr
object And extends generic.ParserBridge2[Expr, Expr, And]
case class Or(e1: Expr, e2: Expr) extends Expr
object Or extends generic.ParserBridge2[Expr, Expr, Or]

case class IntLiter(x: Int) extends Expr
object IntLiter extends generic.ParserBridge1[Int, IntLiter]
case class BoolLiter(b: Boolean) extends Expr
object BoolLiter extends generic.ParserBridge1[Boolean, BoolLiter]
case class CharLiter(c: Char) extends Expr
object CharLiter extends generic.ParserBridge1[Char, CharLiter]
case class StringLiter(s: String) extends Expr
object StringLiter extends generic.ParserBridge1[String, StringLiter]
case object PairLiter extends Expr, generic.ParserBridge0[Expr]
case class Ident(v: String) extends Expr with LValue
object Ident extends generic.ParserBridge1[String, Ident]
case class ArrayElem(v: Ident, es: List[Expr]) extends Expr with LValue
object ArrayElem extends generic.ParserBridge2[Ident, List[Expr], ArrayElem]
case class NestedExpr(e: Expr) extends Expr
object NestedExpr extends generic.ParserBridge1[Expr, NestedExpr]

sealed trait LValue

sealed trait RValue
case class ArrayLiter(es: List[Expr]) extends RValue
object ArrayLiter extends generic.ParserBridge1[List[Expr], ArrayLiter]
case class NewPair(e1: Expr, e2: Expr) extends RValue
object NewPair extends generic.ParserBridge2[Expr, Expr, NewPair]
case class Fst(l: LValue) extends LValue, RValue
object Fst extends generic.ParserBridge1[LValue, Fst]
case class Snd(l: LValue) extends LValue, RValue
object Snd extends generic.ParserBridge1[LValue, Snd]
case class Call(v: Ident, args: List[Expr]) extends RValue
object Call extends generic.ParserBridge2[Ident, List[Expr], Call]

sealed trait Stmt
object Skip extends Stmt with generic.ParserBridge0[Stmt]
case class Decl(t: Type, v: Ident, r: RValue) extends Stmt
object Decl extends generic.ParserBridge3[Type, Ident, RValue, Decl]
case class Asgn(l: LValue, r: RValue) extends Stmt
object Asgn extends generic.ParserBridge2[LValue, RValue, Asgn]
case class Read(l: LValue) extends Stmt
object Read extends generic.ParserBridge1[LValue, Read]
case class Free(e: Expr) extends Stmt
object Free extends generic.ParserBridge1[Expr, Free]
case class Return(e: Expr) extends Stmt
object Return extends generic.ParserBridge1[Expr, Return]
case class Exit(e: Expr) extends Stmt
object Exit extends generic.ParserBridge1[Expr, Exit]
case class Print(e: Expr) extends Stmt
object Print extends generic.ParserBridge1[Expr, Print]
case class PrintLn(e: Expr) extends Stmt
object PrintLn extends generic.ParserBridge1[Expr, PrintLn]
case class If(cond: Expr, s1: Stmt, s2: Stmt) extends Stmt
object If extends generic.ParserBridge3[Expr, Stmt, Stmt, If]
case class While(cond: Expr, body: Stmt) extends Stmt
object While extends generic.ParserBridge2[Expr, Stmt, While]
case class Begin(body: Stmt) extends Stmt
object Begin extends generic.ParserBridge1[Stmt, Begin]
case class Semi(s1: Stmt, s2: Stmt) extends Stmt
object Semi extends generic.ParserBridge2[Stmt, Stmt, Semi]

case class Func(t: Type, v: Ident, params: List[(Type, Ident)], body: Stmt)
object Func extends generic.ParserBridge4[Type, Ident, List[(Type, Ident)], Stmt, Func]

case class Program(fs: List[Func], body: Stmt)
object Program extends generic.ParserBridge2[List[Func], Stmt, Program]
