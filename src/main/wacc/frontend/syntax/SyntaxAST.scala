package wacc

sealed class SyntaxAST
object SyntaxAST {
  import parsley.generic.ErrorBridge

  type Param = (Type, Ident)
  private type FuncDecl = (Type, Ident)

  sealed trait PositionalNode {
    val pos: (Int, Int)
  }

  sealed trait Type extends PositionalNode
  sealed trait PairElemType extends PositionalNode
  sealed trait BaseType extends Type, PairElemType

  sealed trait TypeErrorBridge extends ErrorBridge {
    override def labels: List[String] = List("type")

    override def reason: Option[String] = Some(
      "Examples of types include int, bool, char, string, and arrays of these types"
    )
  }
  case class IntType()(val pos: (Int, Int)) extends BaseType, TypeErrorBridge
  case class BoolType()(val pos: (Int, Int)) extends BaseType, TypeErrorBridge
  case class CharType()(val pos: (Int, Int)) extends BaseType, TypeErrorBridge
  case class StringType()(val pos: (Int, Int)) extends BaseType, TypeErrorBridge

  case class ArrayType(t: Type)(val pos: (Int, Int)) extends Type, PairElemType
  case class ErasedPair()(val pos: (Int, Int)) extends PairElemType
  case class PairType(t1: PairElemType, t2: PairElemType)(val pos: (Int, Int)) extends Type

  object IntType extends ParserBridgePos0[IntType]
  object BoolType extends ParserBridgePos0[BoolType]
  object CharType extends ParserBridgePos0[CharType]
  object StringType extends ParserBridgePos0[StringType]

  object ArrayType extends ParserBridgePos1[Type, ArrayType]
  object ErasedPair extends ParserBridgePos0[PairElemType]
  object PairType extends ParserBridgePos2[PairElemType, PairElemType, PairType]

  sealed trait Expr extends RValue, PositionalNode
  case class Not(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Negate(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Len(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Ord(e: Expr)(val pos: (Int, Int)) extends Expr
  case class Chr(e: Expr)(val pos: (Int, Int)) extends Expr

  object Not extends ParserBridgePos1[Expr, Not]
  object Negate extends ParserBridgePos1[Expr, Negate]
  object Len extends ParserBridgePos1[Expr, Len]
  object Ord extends ParserBridgePos1[Expr, Ord]
  object Chr extends ParserBridgePos1[Expr, Chr]

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

  object Mult extends ParserBridgePos2[Expr, Expr, Mult]
  object Div extends ParserBridgePos2[Expr, Expr, Div]
  object Mod extends ParserBridgePos2[Expr, Expr, Mod]
  object Add extends ParserBridgePos2[Expr, Expr, Add]
  object Sub extends ParserBridgePos2[Expr, Expr, Sub]
  object Greater extends ParserBridgePos2[Expr, Expr, Greater]
  object GreaterEq extends ParserBridgePos2[Expr, Expr, GreaterEq]
  object Smaller extends ParserBridgePos2[Expr, Expr, Smaller]
  object SmallerEq extends ParserBridgePos2[Expr, Expr, SmallerEq]
  object Equals extends ParserBridgePos2[Expr, Expr, Equals]
  object NotEquals extends ParserBridgePos2[Expr, Expr, NotEquals]
  object And extends ParserBridgePos2[Expr, Expr, And]
  object Or extends ParserBridgePos2[Expr, Expr, Or]

  case class IntLiter(x: Int)(val pos: (Int, Int)) extends Expr
  case class BoolLiter(b: Boolean)(val pos: (Int, Int)) extends Expr
  case class CharLiter(c: Char)(val pos: (Int, Int)) extends Expr
  case class StringLiter(s: String)(val pos: (Int, Int)) extends Expr
  case class PairLiter()(val pos: (Int, Int)) extends Expr
  case class Ident(v: String)(val pos: (Int, Int)) extends Expr with LValue
  case class ArrayElem(ident: Ident, es: List[Expr])(val pos: (Int, Int)) extends Expr with LValue

  object IntLiter extends ParserBridgePos1[Int, IntLiter]
  object BoolLiter extends ParserBridgePos1[Boolean, BoolLiter]
  object CharLiter extends ParserBridgePos1[Char, CharLiter]
  object StringLiter extends ParserBridgePos1[String, StringLiter]
  object PairLiter extends ParserBridgePos0[PairLiter]
  object Ident extends ParserBridgePos1[String, Ident]
  object ArrayElem extends ParserBridgePos2[Ident, List[Expr], ArrayElem]

  sealed trait LValue extends PositionalNode

  sealed trait RValue extends PositionalNode
  case class ArrayLiter(es: List[Expr])(val pos: (Int, Int)) extends RValue
  case class NewPair(e1: Expr, e2: Expr)(val pos: (Int, Int)) extends RValue
  case class Fst(l: LValue)(val pos: (Int, Int)) extends LValue, RValue
  case class Snd(l: LValue)(val pos: (Int, Int)) extends LValue, RValue
  case class Call(ident: Ident, args: List[Expr])(val pos: (Int, Int)) extends RValue

  object ArrayLiter extends ParserBridgePos1[List[Expr], ArrayLiter]
  object NewPair extends ParserBridgePos2[Expr, Expr, NewPair] {
    override def labels: List[String] = List("pair construction")
  }
  object Fst extends ParserBridgePos1[LValue, Fst] {
    override def labels: List[String] = List("pair element")
  }
  object Snd extends ParserBridgePos1[LValue, Snd] {
    override def labels: List[String] = List("pair element")
  }
  object Call extends ParserBridgePos2[Ident, List[Expr], Call] {
    override def labels: List[String] = List("function call")
  }

  sealed trait StmtErrorBridge extends ErrorBridge {
    override def labels: List[String] = List("statement")

    override def reason: Option[String] = Some(
      "Examples of statements include assignments (int x = 2) or blocks (if x == 2 then x = 3 else x = 4 fi)"
    )
  }

  sealed trait Stmt extends PositionalNode
  case class Skip()(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Decl(t: Type, ident: Ident, r: RValue)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Asgn(l: LValue, r: RValue)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Read(l: LValue)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Free(e: Expr)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Return(e: Expr)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Exit(e: Expr)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Print(e: Expr)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class PrintLn(e: Expr)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class If(cond: Expr, s1: Stmt, s2: Stmt)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class While(cond: Expr, body: Stmt)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Begin(body: Stmt)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Semi(s1: Stmt, s2: Stmt)(val pos: (Int, Int)) extends Stmt, StmtErrorBridge
  case class Throw(e: Expr)(val pos: (Int, Int)) extends Stmt
  case class TryCatchFinally(tryBody: Stmt, catchIdent: Ident, catchBody: Stmt, finallyBody: Stmt)(val pos: (Int, Int))
      extends Stmt

  object Skip extends ParserBridgePos0[Skip]
  object Decl extends ParserBridgePos3[Type, Ident, RValue, Decl]
  object Asgn extends ParserBridgePos2[LValue, RValue, Asgn]
  object Read extends ParserBridgePos1[LValue, Read]
  object Free extends ParserBridgePos1[Expr, Free]
  object Return extends ParserBridgePos1[Expr, Return]
  object Exit extends ParserBridgePos1[Expr, Exit]
  object Print extends ParserBridgePos1[Expr, Print]
  object PrintLn extends ParserBridgePos1[Expr, PrintLn]
  object If extends ParserBridgePos3[Expr, Stmt, Stmt, If]
  object While extends ParserBridgePos2[Expr, Stmt, While]
  object Begin extends ParserBridgePos1[Stmt, Begin]
  object Semi extends ParserBridgePos2[Stmt, Stmt, Semi]
  object Throw extends ParserBridgePos1[Expr, Throw]
  object TryCatchFinally extends ParserBridgePos4[Stmt, Ident, Stmt, Stmt, TryCatchFinally]

  case class Func(decl: FuncDecl, params: List[Param], body: Stmt)(val pos: (Int, Int)) extends PositionalNode
  object Func extends ParserBridgePos3[FuncDecl, List[Param], Stmt, Func] {
    override def labels: List[String] = List("function")
  }

  case class Program(imports: List[Import], fs: List[Func], body: Stmt)(val pos: (Int, Int))
  object Program extends ParserBridgePos3[List[Import], List[Func], Stmt, Program] {
    override def labels: List[String] = List("program")
  }

  case class Import(filename: StringLiter)(val pos: (Int, Int))
  object Import extends ParserBridgePos1[StringLiter, Import] {
    override def labels: List[String] = List("import")
  }
}
