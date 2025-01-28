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
case class Negate(e: Expr) extends Expr
case class Len(e: Expr) extends Expr
case class Ord(e: Expr) extends Expr
case class Chr(e: Expr) extends Expr

object Not extends generic.ParserBridge1[Expr, Not]
object Negate extends generic.ParserBridge1[Expr, Negate]
object Len extends generic.ParserBridge1[Expr, Len]
object Ord extends generic.ParserBridge1[Expr, Ord]
object Chr extends generic.ParserBridge1[Expr, Chr]

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

object Mult extends generic.ParserBridge2[Expr, Expr, Mult]
object Div extends generic.ParserBridge2[Expr, Expr, Div]
object Mod extends generic.ParserBridge2[Expr, Expr, Mod]
object Add extends generic.ParserBridge2[Expr, Expr, Add]
object Sub extends generic.ParserBridge2[Expr, Expr, Sub]
object Greater extends generic.ParserBridge2[Expr, Expr, Greater]
object GreaterEq extends generic.ParserBridge2[Expr, Expr, GreaterEq]
object Smaller extends generic.ParserBridge2[Expr, Expr, Smaller]
object SmallerEq extends generic.ParserBridge2[Expr, Expr, SmallerEq]
object Equals extends generic.ParserBridge2[Expr, Expr, Equals]
object NotEquals extends generic.ParserBridge2[Expr, Expr, NotEquals]
object And extends generic.ParserBridge2[Expr, Expr, And]
object Or extends generic.ParserBridge2[Expr, Expr, Or]

case class IntLiter(x: Int) extends Expr
case class BoolLiter(b: Boolean) extends Expr
case class CharLiter(c: Char) extends Expr
case class StringLiter(s: String) extends Expr
case object PairLiter extends Expr
case class Ident(v: String) extends Expr with LValue
case class ArrayElem(v: Ident, es: List[Expr]) extends Expr with LValue
case class NestedExpr(e: Expr) extends Expr

object IntLiter extends generic.ParserBridge1[Int, IntLiter]
object BoolLiter extends generic.ParserBridge1[Boolean, BoolLiter]
object CharLiter extends generic.ParserBridge1[Char, CharLiter]
object StringLiter extends generic.ParserBridge1[String, StringLiter]
object Ident extends generic.ParserBridge1[String, Ident]
object ArrayElem extends generic.ParserBridge2[Ident, List[Expr], ArrayElem]
object NestedExpr extends generic.ParserBridge1[Expr, NestedExpr]

sealed trait LValue

sealed trait RValue
case class ArrayLiter(es: List[Expr]) extends RValue
case class NewPair(e1: Expr, e2: Expr) extends RValue
case class Fst(l: LValue) extends LValue, RValue
case class Snd(l: LValue) extends LValue, RValue
case class Call(v: Ident, args: List[Expr]) extends RValue

object ArrayLiter extends generic.ParserBridge1[List[Expr], ArrayLiter]
object NewPair extends generic.ParserBridge2[Expr, Expr, NewPair]
object Fst extends generic.ParserBridge1[LValue, Fst]
object Snd extends generic.ParserBridge1[LValue, Snd]
object Call extends generic.ParserBridge2[Ident, List[Expr], Call]

sealed trait Stmt
case object Skip extends Stmt
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

object Decl extends generic.ParserBridge3[Type, Ident, RValue, Decl]
object Asgn extends generic.ParserBridge2[LValue, RValue, Asgn]
object Read extends generic.ParserBridge1[LValue, Read]
object Free extends generic.ParserBridge1[Expr, Free]
object Return extends generic.ParserBridge1[Expr, Return]
object Exit extends generic.ParserBridge1[Expr, Exit]
object Print extends generic.ParserBridge1[Expr, Print]
object PrintLn extends generic.ParserBridge1[Expr, PrintLn]
object If extends generic.ParserBridge3[Expr, Stmt, Stmt, If]
object While extends generic.ParserBridge2[Expr, Stmt, While]
object Begin extends generic.ParserBridge1[Stmt, Begin]
object Semi extends generic.ParserBridge2[Stmt, Stmt, Semi]

case class Func(t: Type, v: Ident, params: List[(Type, Ident)], body: Stmt)
object Func extends generic.ParserBridge4[Type, Ident, List[(Type, Ident)], Stmt, Func]

case class Program(fs: List[Func], body: Stmt)
object Program extends generic.ParserBridge2[List[Func], Stmt, Program]
