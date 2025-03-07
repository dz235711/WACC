package wacc

import wacc.RenamedAST.KnownType
import wacc.TypedAST._

import scala.compiletime.uninitialized
import scala.collection.mutable.Map as MMap
import scala.collection.mutable.ListBuffer
import java.rmi.UnexpectedException

type Id = Int
type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | UninitalizedPair | ArrayValue

type VariableScope = MapContext[Id, Value]
type FunctionScope = MapContext[Id, (List[Ident], Stmt)]

case class PairValue(var fst: Value, var snd: Value) {
  override def toString: String = s"($fst, $snd)"
}
class UninitalizedPair private ()
object UninitalizedPair {
  val instance = UninitalizedPair()
}
case class ArrayValue(es: ListBuffer[Value]) {
  override def toString: String = es.mkString("[", ", ", "]")
}

final class Interpreter {

  // CONSTANTS

  /** String representation of null pointer */
  private val NullPointerString = "(nil)"

  /** Exit message */
  private val ExitString = "Exiting interpreter..."

  /** System call exit error message */
  private val ExitErrorString = "Exit expression must evaluate to an integer"

  /** Read error message */
  private val ReadErrorString = "Read must be called on object of type int or char"

  /** Condition error message */
  private val ConditionErrorString = "Condition must evaluate to a boolean"

  /** Null dereference error message */
  private val NullDereferenceErrorString = "Cannot dereference null pointer"

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
      case Decl(v, r) => handleAssignment(v, r)
      case Asgn(l, r) => handleAssignment(l, r)
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
        value match {
          // WACC prints the pointers of arrays and pairs, but the closest we can get in Scala is the hashcode
          case pointer: (ArrayValue | PairValue) => print(pointer.hashCode)
          case nullPointer: UninitalizedPair     => print(NullPointerString)
          case _                                 => print(value)
        }
        print(value.toString())
        scope
      case PrintLn(e) =>
        interpretStmt(Print(e))
        println()
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
    case ArrayLiter(es, _)    => ArrayValue(es.map(interpretExpr).to(ListBuffer))
    case NewPair(e1, e2, _)   => PairValue(interpretExpr(e1), interpretExpr(e2))
    case pairVal: (Fst | Snd) => getLValue(pairVal)
    case Call(v, args, _)     =>
      // Fetch parameters and body of function
      val (params, body) = funcScope.get(v.id).getOrElse(throw new UnexpectedException(getFuncErrorString(v.id)))

      // Evaluate arguments and put them into a new scope for the function
      val evaluatedArgs = args.map(interpretExpr)
      val newMap = MMap.newBuilder[Id, Value]
      for (evaluatedParam <- params.map(_.id).zip(evaluatedArgs)) {
        newMap += evaluatedParam
      }
      val newScope: VariableScope = MapContext(newMap.result())

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
  def interpretExpr(e: Expr)(using scope: VariableScope): Value =
    e match {
      case Not(e)    => !(e.asInstanceOf[Boolean])
      case Negate(e) => -(e.asInstanceOf[Int])
      case Len(e)    => (e.asInstanceOf[ArrayValue]).es.length
      case Ord(e)    => (e.asInstanceOf[Char]).toInt // TODO: Check if this is consistent with WACC ord
      case Chr(e)    => (e.asInstanceOf[Int]).toChar // TODO: Check if this is consistent with WACC chr

      case Mult(e1, e2)      => e1.asInstanceOf[Int] * e2.asInstanceOf[Int] // TODO: Is there a way to avoid this cast?
      case Mod(e1, e2)       => e1.asInstanceOf[Int] % e2.asInstanceOf[Int]
      case Add(e1, e2)       => e1.asInstanceOf[Int] + e2.asInstanceOf[Int]
      case Div(e1, e2)       => e1.asInstanceOf[Int] / e2.asInstanceOf[Int]
      case Sub(e1, e2)       => e1.asInstanceOf[Int] - e2.asInstanceOf[Int]
      case Greater(e1, e2)   => binaryCompare(e1, e2, _ > _, _ > _)
      case GreaterEq(e1, e2) => binaryCompare(e1, e2, _ >= _, _ >= _)
      case Smaller(e1, e2)   => binaryCompare(e1, e2, _ < _, _ < _)
      case SmallerEq(e1, e2) => binaryCompare(e1, e2, _ <= _, _ <= _)
      case Equals(e1, e2)    => interpretExpr(e1) == interpretExpr(e2)
      case NotEquals(e1, e2) => interpretExpr(e1) != interpretExpr(e2)
      case And(e1, e2)       => e1.asInstanceOf[Boolean] && e2.asInstanceOf[Boolean]
      case Or(e1, e2)        => e1.asInstanceOf[Boolean] || e2.asInstanceOf[Boolean]

      case IntLiter(x)    => x
      case BoolLiter(b)   => b
      case CharLiter(c)   => c
      case StringLiter(s) => s
      case PairLiter      => UninitalizedPair.instance

      case Ident(id, _) =>
        scope
          .get(id)
          .getOrElse(throw new UnexpectedException(s"Variable with id $id not found")) // TODO: Proper free handling
      case ArrayElem(v, es, _) =>
        // Evaluate the expression indices
        val indices = es.map(interpretExpr(_).asInstanceOf[Int])

        // Index through the array(s)
        var currentValue =
          scope.get(v.id).getOrElse(throw new UnexpectedException(s"Variable with id ${v.id} not found"))
        for (ind <- indices) {
          val ArrayValue(arrayVals) = currentValue: @unchecked // TODO: Idiomatic?
          currentValue = arrayVals(ind)
        }

        currentValue
      case NestedExpr(e, _) => interpretExpr(e)
    }

  def binaryCompare(e1: Expr, e2: Expr, intComparator: (Int, Int) => Boolean, charComparator: (Char, Char) => Boolean)(
      using scope: VariableScope
  ): Boolean =
    val evalExpr2 = interpretExpr(e2)
    interpretExpr(e1) match {
      case i1: Int  => intComparator(i1, evalExpr2.asInstanceOf[Int])
      case c1: Char => charComparator(c1, evalExpr2.asInstanceOf[Char])
      case _        => throw new UnexpectedException("Unexpected type for comparison")
    }

  def handleAssignment(l: LValue, r: RValue)(using
      scope: VariableScope
  )(using funcScope: FunctionScope): VariableScope =
    l match {
      case Ident(id, _) =>
        scope.add(id, interpretRValue(r))
      case ArrayElem(v, es, semType) =>
        val nestedArray =
          interpretExpr(ArrayElem(v, es.init, semType))
            .asInstanceOf[ArrayValue] // Index through to the array that we want to change.
        val lastIndex = interpretExpr(es.last).asInstanceOf[Int]

        val newValue = interpretRValue(r)
        nestedArray.es(lastIndex) = newValue

        scope
      // Fetch the pair to set its values
      case Fst(l, _) =>
        unpackAsPair(getLValue(l)).fst = interpretRValue(r)
        scope
      case Snd(l, _) =>
        unpackAsPair(getLValue(l)).snd = interpretRValue(r)
        scope
    }

  def getLValue(l: LValue)(using scope: VariableScope): Value =
    l match {
      case idArr: (Ident | ArrayElem) => interpretExpr(idArr)
      case Fst(l, _)                  => unpackAsPair(getLValue(l)).fst
      case Snd(l, _)                  => unpackAsPair(getLValue(l)).snd
    }

  def unpackAsPair(value: Value): PairValue =
    value match {
      case pair: PairValue       => pair
      case nil: UninitalizedPair => throw new UnexpectedException(NullDereferenceErrorString)
      case _                     => throw new UnexpectedException("Expected pair, got something else")
    }
}
