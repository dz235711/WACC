package wacc

import RenamedAST.{SemType, ?}
import RenamedAST.KnownType.*

sealed class TypedAST
object TypedAST {
  sealed trait Type {
    def getType: SemType
  }

  sealed trait IntType extends Type {
    def getType: SemType = IntType
  }
  sealed trait BoolType extends Type {
    def getType: SemType = BoolType
  }
  sealed trait CharType extends Type {
    def getType: SemType = CharType
  }
  sealed trait StringType extends Type {
    def getType: SemType = StringType
  }

  sealed trait Expr extends RValue
  case class Not(e: Expr) extends Expr, BoolType
  case class Negate(e: Expr) extends Expr, IntType
  case class Len(e: Expr) extends Expr, IntType
  case class Ord(e: Expr) extends Expr, IntType
  case class Chr(e: Expr) extends Expr, CharType

  case class Mult(e1: Expr, e2: Expr) extends Expr, IntType
  case class Mod(e1: Expr, e2: Expr) extends Expr, IntType
  case class Add(e1: Expr, e2: Expr) extends Expr, IntType
  case class Div(e1: Expr, e2: Expr) extends Expr, IntType
  case class Sub(e1: Expr, e2: Expr) extends Expr, IntType
  case class Greater(e1: Expr, e2: Expr) extends Expr, BoolType
  case class GreaterEq(e1: Expr, e2: Expr) extends Expr, BoolType
  case class Smaller(e1: Expr, e2: Expr) extends Expr, BoolType
  case class SmallerEq(e1: Expr, e2: Expr) extends Expr, BoolType
  case class Equals(e1: Expr, e2: Expr) extends Expr, BoolType
  case class NotEquals(e1: Expr, e2: Expr) extends Expr, BoolType
  case class And(e1: Expr, e2: Expr) extends Expr, BoolType
  case class Or(e1: Expr, e2: Expr) extends Expr, BoolType

  case class IntLiter(x: Int) extends Expr, IntType
  case class BoolLiter(b: Boolean) extends Expr, BoolType
  case class CharLiter(c: Char) extends Expr, CharType
  case class StringLiter(s: String) extends Expr, StringType
  object PairLiter extends Expr, Type {
    def getType: SemType = PairType(?, ?)
  }
  case class Ident(id: Int, override val getType: SemType) extends Expr, LValue
  case class ArrayElem(v: Ident, es: List[Expr], override val getType: SemType) extends Expr, LValue
  case class NestedExpr(e: Expr, override val getType: SemType) extends Expr, Type

  sealed trait LValue extends Type
  sealed trait RValue extends Type
  case class ArrayLiter(es: List[Expr], override val getType: SemType) extends RValue
  case class NewPair(e1: Expr, e2: Expr, override val getType: SemType) extends RValue
  case class Fst(l: LValue, override val getType: SemType) extends LValue, RValue
  case class Snd(l: LValue, override val getType: SemType) extends LValue, RValue
  case class Call(v: Ident, args: List[Expr], override val getType: SemType) extends RValue

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
  case class Throw(e: Expr) extends Stmt
  case class TryCatchFinally(body: Stmt, catchIdent: Ident, catchBody: Stmt, finallyBody: Stmt) extends Stmt

  case class Func(v: Ident, params: List[Ident], body: Stmt)

  case class Program(fs: List[Func], body: Stmt)
}
