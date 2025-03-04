package wacc

import wacc.TypedAST._

import java.rmi.UnexpectedException

type Id = Int
type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | ArrayValue

case class PairValue(fst: Value, snd: Value)
case class ArrayValue(es: List[Value])

final class Interpreter {

  private val ExitString = "Exiting interpreter..."

  private val ExitErrorString = "Exit expression must evaluate to an integer"

  def interpret(program: Program): Unit = {
    val globalScope = new MapContext[Id, Value]()
    ???
  }

  def interpretFunction(func: Func)(using scope: MapContext[Id, Value]): Unit = {
    ???
  }

  def interpretStmt(stmt: Stmt)(using scope: MapContext[Id, Value]): Unit = stmt match {
    case Skip       => ()
    case Decl(v, r) => scope.add(v.id, interpretRValue(r)) // x = call f()
    case Asgn(l, r) => scope.add(interpretLValue(l), interpretRValue(r))
    case Read(l)    => ??? // TODO: Prompt user for input
    case Free(e)    => ??? // TODO: Keep track of freed identifiers
    case Return(e)  => ??? // TODO: When implementing functions
    case Exit(e) =>
      println(ExitString)
      interpretExpr(e) match {
        case i: Int => sys.exit(i)
        case _      => throw new UnexpectedException(ExitErrorString)
      }
    case Print(e)          => ???
    case PrintLn(e)        => ???
    case If(cond, s1, s2)  => ???
    case While(cond, body) => ???
    case Begin(body)       => ???
    case Semi(s1, s2)      => ???
  }

  def interpretRValue(r: RValue)(using scope: MapContext[Id, Value]): Value = ???

  def interpretLValue(l: LValue)(using scope: MapContext[Id, Value]): Id = ???

  def interpretExpr(e: Expr)(using scope: MapContext[Id, Value]): Value = ???
}
