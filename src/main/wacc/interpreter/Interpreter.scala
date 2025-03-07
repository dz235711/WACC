package wacc

import wacc.RenamedAST.KnownType
import wacc.TypedAST._

import scala.compiletime.uninitialized
import scala.collection.mutable.Map as MMap
import java.rmi.UnexpectedException
import wacc.lexer.int

type Id = Int
type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | ArrayValue

type VariableScope = MapContext[Id, Value]
type FunctionScope = MapContext[Id, (List[Ident], Stmt)]

case class PairValue(fst: Value, snd: Value) {
  override def toString: String = s"($fst, $snd)"
}
case class ArrayValue(es: List[Value]) {
  override def toString: String = es.mkString("[", ", ", "]")
}

final class Interpreter {

  // CONSTANTS

  /** Exit message */
  private val ExitString = "Exiting interpreter..."

  /** System call exit error message */
  private val ExitErrorString = "Exit expression must evaluate to an integer"

  /** Read error message */
  private val ReadErrorString = "Read must be called on object of type int or char"

  /** Condition error message */
  private val ConditionErrorString = "Condition must evaluate to a boolean"

  // VARIABLES

  /** The return value of a function, initialised once it is called */
  private var returnValue: Value = uninitialized

  // Helper functions

  /** Returns an error message for when a function is not found in the function scope
    *
    * @param id The id of the function that was not found
    * @return The error message
    */
  private def getFuncErrorString(id: Id): String = s"Function with id $id not found"

  // TODO: Change documentation after scope inheritance is implemented.
  /** Interprets a program within a new scope.
    *
    * @param program The program to be interpreted
    * @return The variable and function scopes after interpreting the program
    */
  def interpret(program: Program): (VariableScope, FunctionScope) = {
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
      case Decl(v, r) => scope.add(v.id, interpretRValue(r))
      case Asgn(l, r) => scope.add(interpretLValue(l), interpretRValue(r))
      case Read(l) =>
        val readValue = l.getType match {
          case KnownType.IntType  => scala.io.StdIn.readInt()
          case KnownType.CharType => scala.io.StdIn.readChar()
          case _                  => throw new UnexpectedException(ReadErrorString)
        }
        val lId = interpretLValue(l)
        scope.add(lId, readValue)
      case Free(e)   => ???
      case Return(e) =>
        // Set the class-wide return value
        returnValue = interpretExpr(e)
        scope
      case Exit(e) =>
        println(ExitString)
        interpretExpr(e) match {
          case i: Int => sys.exit(i)
          case _      => throw new UnexpectedException(ExitErrorString)
        }
      case Print(e) =>
        val value = interpretExpr(e)
        print(value.toString())
        scope
      case PrintLn(e) =>
        val value = interpretExpr(e)
        println(value.toString())
        scope
      case If(cond, s1, s2) =>
        val evaluatedCond = interpretExpr(cond) match {
          case b: Boolean => b
          case _          => throw new UnexpectedException(ConditionErrorString)
        }

        if (evaluatedCond) {
          interpretStmt(s1)
        } else {
          interpretStmt(s2)
        }
      case While(cond, body) =>
        val evaluatedCond = interpretExpr(cond) match {
          case b: Boolean => b
          case _          => throw new UnexpectedException(ConditionErrorString)
        }

        if (evaluatedCond) {
          val newScope = interpretStmt(body)
          interpretStmt(While(cond, body))(using newScope)
        } else {
          scope
        }
      case Begin(body) => interpretStmt(body)
      case Semi(s1, s2) =>
        val newScope = interpretStmt(s1)
        interpretStmt(s2)(using newScope)
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
    case Call(v, args, _)   =>
      // Fetch parameters and body of function
      val (params, body) = funcScope.get(v.id) match {
        case Some(value) => value
        case None        => throw new UnexpectedException(getFuncErrorString(v.id))
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
  def interpretExpr(e: Expr)(using scope: VariableScope)(using funcScope: FunctionScope): Value =
    e match {
      case Not(e)    => !unpackExprAs[Boolean](e)
      case Negate(e) => -unpackExprAs[Int](e)
      case Len(e)    => unpackExprAs[ArrayValue](e).es.length
      case Ord(e)    => unpackExprAs[Char](e).toInt // TODO: Check if this is consistent with WACC ord
      case Chr(e)    => unpackExprAs[Int](e).toChar // TODO: Check if this is consistent with WACC ord

      case Mult(e1, e2) => unpackExprAs[Int](e1) * unpackExprAs[Int](e2)
      case Mod(e1, e2)  => unpackExprAs[Int](e1) % unpackExprAs[Int](e2)
      case Add(e1, e2)  => unpackExprAs[Int](e1) + unpackExprAs[Int](e2)
      case Div(e1, e2)  => unpackExprAs[Int](e1) / unpackExprAs[Int](e2)
      case Sub(e1, e2)  => unpackExprAs[Int](e1) - unpackExprAs[Int](e2)
      case Greater(e1, e2) =>
        interpretExpr(e1) match {
          case i1: Int  => i1 > unpackExprAs[Int](e2)
          case c1: Char => c1 > unpackExprAs[Char](e2)
          case _        => throw new UnexpectedException("Unexpected type for comparison")
        }
      case GreaterEq(e1, e2) => ???
      case Smaller(e1, e2)   => ???
      case SmallerEq(e1, e2) => ???
      case Equals(e1, e2)    => ???
      case NotEquals(e1, e2) => ???
      case And(e1, e2)       => unpackExprAs[Boolean](e1) && unpackExprAs[Boolean](e2)
      case Or(e1, e2)        => unpackExprAs[Boolean](e1) || unpackExprAs[Boolean](e2)

      case IntLiter(x)    => x
      case BoolLiter(b)   => b
      case CharLiter(c)   => c
      case StringLiter(s) => s
      case PairLiter      => ???

      case Ident(id, _) =>
        scope
          .get(id)
          .getOrElse(throw new UnexpectedException(s"Variable with id $id not found")) // TODO: Proper free handling
      case ArrayElem(v, es, _) => ???
      case NestedExpr(e, _)    => interpretExpr(e)
    }

    def unpackExprAs[T <: Value](e: Expr): T = interpretExpr(e) match {
      case v: T => v
      case _    => throw new UnexpectedException(s"Unexpected type unpacked")
    }
}
