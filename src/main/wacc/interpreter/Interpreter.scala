package wacc

import wacc.TypedAST._

import scala.compiletime.uninitialized
import scala.collection.mutable.Map as MMap
import java.rmi.UnexpectedException

type Id = Int
type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | ArrayValue

type VariableScope = MapContext[Id, Value]
type FunctionScope = MapContext[Id, (List[Ident], Stmt)]

case class PairValue(fst: Value, snd: Value)
case class ArrayValue(es: List[Value])

final class Interpreter {

  // CONSTANTS

  /** Exit message */
  private val ExitString = "Exiting interpreter..."

  /** Message for system call exit error */
  private val ExitErrorString = "Exit expression must evaluate to an integer"

  // VARIABLES

  /** The return value of a function, initialised once it is called */
  private var returnValue: Value = uninitialized

  // TODO: Change documentation after scope inheritance is implemented.
  /** Interprets a program within a new scope.
    *
    * @param program The program to be interpreted
    */
  def interpret(program: Program): Unit = {
    val globalScope = new MapContext[Id, Value]()
    ???
  }

  /** Interprets a function, adding it to the scope of functions
    *
    * @param func The function to be interpreted
    * @return The function scope with `func` added.
    */
  def interpretFunction(func: Func)(using scope: VariableScope)(using funcScope: FunctionScope): FunctionScope =
    funcScope.add(func.v.id, (func.params, func.body))

  /** Interprets a statement and returns the scope, regardless of if the scope has changed or not.
    *
    * @param stmt The statement to be interpreted
    * @return The scope after interpreting the statement
    */
  def interpretStmt(stmt: Stmt)(using scope: VariableScope)(using funcScope: FunctionScope): VariableScope =
    stmt match {
      case Skip       => scope
      case Decl(v, r) => scope.add(v.id, interpretRValue(r)) // x = call f()
      case Asgn(l, r) => scope.add(interpretLValue(l), interpretRValue(r))
      case Read(l)    => ??? // TODO: Prompt user for input
      case Free(e)    => ??? // TODO: Keep track of freed identifiers
      case Return(e) => {
        // Set the class-wide return value
        returnValue = interpretExpr(e)
        scope
      }
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

  /** Interprets an RValue into an evaluated value.
    *
    * @param r The RValue to interpret
    * @result The value of the evaluated RValue
    */
  def interpretRValue(r: RValue)(using scope: VariableScope)(using funcScope: FunctionScope): Value = r match {
    case ArrayLiter(es, _)  => ???
    case NewPair(e1, e2, _) => ???
    case Fst(l, _)          => ???
    case Snd(l, _)          => ???
    case Call(v, args, _) =>
      ???
      // Fetch parameters and body of function
      val (params, body) = funcScope.get(v.id) match {
        case Some(value) => value
        case None        => throw new UnexpectedException(s"Function ${v.id} not found")
      }

      // Evaluate arguments and put them into a new scope for the function
      val evaluatedArgs = args.map(interpretExpr(_))
      val newMap = MMap.newBuilder[Id, Value]
      for (evaluatedParam <- params.map(_.id).zip(evaluatedArgs)) {
        newMap += evaluatedParam
      }
      val newScope: VariableScope = MapContext(newMap.result()) // TODO: figure out what the fuck is happening here

      // Call the function. The result will be stored in returnValue as per the Return case in interpretStmt
      interpretStmt(body)(using newScope)

      returnValue
    case e: Expr => interpretExpr(e)
  }

  /** Interprets an LValue into its corresponding id.
    *
    * @param l The LValue to interpret
    * @return The id of the LValue
    */
  def interpretLValue(l: LValue)(using scope: VariableScope)(using funcScope: FunctionScope): Id = ???

  /** Interprets an expression into an evaluated value.
    *
    * @param e Expression to intrepret
    * @return The value of the evaluated expression
    */
  def interpretExpr(e: Expr)(using scope: VariableScope)(using funcScope: FunctionScope): Value = ???
}
