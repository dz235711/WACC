package wacc

import wacc.RenamedAST.KnownType
import wacc.TypedAST._

import scala.compiletime.uninitialized
import scala.collection.mutable.ListBuffer
import os.SubProcess.InputStream
import os.SubProcess.OutputStream
import wacc.AsciiConstants.MIN_CHAR
import wacc.AsciiConstants.MAX_CHAR
import scala.util.{Try, Success, Failure}

type BoolExpr = BoolLiter | Not | Greater | GreaterEq | Smaller | SmallerEq | Equals | NotEquals | And | Or
type IntExpr = IntLiter | Negate | Ord | Len | Mult | Mod | Add | Div | Sub
type CharExpr = Chr | CharLiter

type VariableScope = MapContext[Id, Value]
type FunctionScope = MapContext[Id, (List[Ident], Stmt)]

final class Interpreter(
    private val inputStream: InputStream = InputStream(System.out),
    private val outputStream: OutputStream = OutputStream(System.in)
) {

  // CONSTANTS

  /** Mask to bind the exit code to the least significant 8 bits */
  private val ExitCodeMask = 0xff

  /** String representation of null pointer */
  private val NullPointerString = "(nil)"

  /** Exit message */
  private val ExitString = "Exiting interpreter..."

  /** System call exit error message */
  private val ExitErrorString = "Exit expression must evaluate to an integer"

  /** Condition error message */
  private val ConditionErrorString = "Condition must evaluate to a boolean"

  /** Comparison type mismatch error message */
  private val ComparisonErrorString = "Comparison must be between two values of the same type"

  /** Pair not received error message */
  private val UnpackPairErrorString = "Expected pair, got something else"

  // VARIABLES

  /** The return value of a function, initialised once it is called */
  private var returnValue: Value = uninitialized

  /** The exit value of the interpreter. Only set if exit is called */
  private var exitValue: Option[Int] = None

  // Helper functions

  /** Returns an error message for when a variable is not found in the variable scope
    *
    * @param id The id of the variable that was not found
    * @return The error message
    */
  private def getVariableErrorString(id: Id): String = s"Variable with id $id not found"

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
    * @param inheritedScope An optional, initial scope for variables
    * @param inheritedFunctionScope An optional, initial scope for functions
    * @return The variable and function scopes after interpreting the program
    */
  def interpret(
      program: Program,
      inheritedScope: VariableScope,
      inheritedFunctionScope: FunctionScope
  ): (VariableScope, FunctionScope, Option[Int]) = {
    given scope: VariableScope = inheritedScope
    given functionScope: FunctionScope = inheritedFunctionScope

    program.fs.foreach(interpretFunction)
    Try(interpretStmt(program.body)) match {
      case Success(_)                                  => ()
      case Failure(InterpreterExitException(exitCode)) => exitValue = Some(exitCode)
      case Failure(exception)                          => throw exception
    }

    (scope, functionScope, exitValue)
  }

  /** Interprets a function, adding it to the scope of functions
    *
    * @param func The function to be interpreted
    * @return The function scope with `func` added.
    */
  private def interpretFunction(func: Func)(using funcScope: FunctionScope): FunctionScope =
    funcScope.add(func.v.id, (func.params, func.body))

  /** Interprets a statement and returns the scope, regardless of if the scope has changed or not.
    *
    * @param stmt The statement to be interpreted
    * @return The scope after interpreting the statement
    */
  private def interpretStmt(stmt: Stmt)(using
      scope: VariableScope,
      funcScope: FunctionScope
  ): VariableScope =
    stmt match {
      case Skip       => scope
      case Decl(v, r) => handleAssignment(v, r)
      case Asgn(l, r) => handleAssignment(l, r)
      case Read(l) =>
        Try(
          l.getType match {
            case KnownType.IntType => IntLiter(outputStream.readLine().toInt)
            case KnownType.CharType =>
              val inputString = outputStream.readLine()
              if (inputString.length != 1) throw BadInputException("Read malformed input. Expected char.")
              if ((MIN_CHAR > inputString.head.toInt) || (inputString.head.toInt > MAX_CHAR))
                throw BadInputException("Read malformed input. Char is not ASCII 0-127.")
              CharLiter(inputString.head)
            case _ => throw BadInputException("Read must be called on object of type int or char")
          }
        ) match {
          case Success(readValue)                                      => handleAssignment(l, readValue)
          case Failure(_: (BadInputException | NumberFormatException)) => scope
          case Failure(exception)                                      => throw exception
        }
      case Free(e) =>
        interpretExpr(e) match {
          case freeable: Freeable => freeable.isFreed = true
          case _                  => throw FreedNonFreeableValueException()
        }
        scope
      case Return(e) =>
        // Set the class-wide return value
        returnValue = interpretExpr(e)
        scope
      case Exit(e) =>
        inputStream.write(ExitString)
        interpretExpr(e) match {
          case i: Int => throw InterpreterExitException(i & ExitCodeMask)
          case _      => throw TypeMismatchException(ExitErrorString)
        }
      case Print(e) =>
        val value = interpretExpr(e)
        value match {
          // An array of characters should print as a string.
          case ArrayValue(es) if es.exists(_.getClass == classOf[Character]) => {
            val resString = StringBuilder().addAll(es.asInstanceOf[ListBuffer[Char]]).result()
            inputStream.write(resString)
          }
          // WACC prints the pointers of arrays and pairs, but the closest we can get in Scala is the hashcode
          case pointer: (ArrayValue | PairValue) => inputStream.write(pointer.hashCode)
          case _: UninitalizedPair     => inputStream.write(NullPointerString)
          case _                                 => inputStream.write(value.toString)
        }
        scope
      case PrintLn(e) =>
        interpretStmt(Print(e))
        inputStream.write("\n")
        scope
      case If(cond, s1, s2) =>
        val evaluatedCond = interpretExpr(cond) match {
          case b: Boolean => b
          case _          => throw TypeMismatchException(ConditionErrorString)
        }

        if (evaluatedCond) {
          interpretStmt(s1)
        } else {
          interpretStmt(s2)
        }
      case While(cond, body) =>
        val evaluatedCond = interpretExpr(cond) match {
          case b: Boolean => b
          case _          => throw TypeMismatchException(ConditionErrorString)
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
  private def interpretRValue(r: RValue)(using scope: VariableScope, funcScope: FunctionScope): Value = r match {
    case ArrayLiter(es, _)    => ArrayValue(es.map(interpretExpr).to(ListBuffer))
    case NewPair(e1, e2, _)   => PairValue(interpretExpr(e1), interpretExpr(e2))
    case pairVal: (Fst | Snd) => getLValue(pairVal)
    case Call(v, args, _)     =>
      // Fetch parameters and body of function
      val (params, body) = funcScope.get(v.id).getOrElse(throw FunctionNotFoundException(getFuncErrorString(v.id)))

      // Evaluate arguments and put them into a new scope for the function
      val evaluatedArgs = args.map(interpretExpr)

      val newScope: VariableScope = MapContext()
      for ((id, value) <- params.map(_.id).zip(evaluatedArgs)) {
        newScope.add(id, value)
      }

      // Call the function. The result will be stored in returnValue as per the Return case in interpretStmt
      interpretStmt(body)(using newScope)

      returnValue
    case e: Expr => interpretExpr(e)
  }

  /** Interprets an expression into an evaluated value.
    *
    * @param e Expression to intrepret
    * @return The value of the evaluated expression
    */
  private def interpretExpr(e: Expr)(using scope: VariableScope): Value =
    e match {
      case e: BoolExpr => interpretBool(e)
      case e: IntExpr  => interpretInt(e)
      case e: CharExpr => interpretChar(e)

      case StringLiter(s) => s
      case PairLiter      => UninitalizedPair.instance

      case Ident(id, _) =>
        scope
          .get(id)
          .getOrElse(
            throw VariableNotFoundException(getVariableErrorString(id))
          ) // TODO: Proper free handling
      case ArrayElem(v, es, _) =>
        // Evaluate the expression indices
        val indices = es.map(interpretExpr(_).asInstanceOf[Int])

        // Index through the array(s)
        var currentValue =
          scope.get(v.id).getOrElse(throw VariableNotFoundException(getVariableErrorString(v.id)))
        for (ind <- indices) {
          val arrVal: ArrayValue = currentValue.asInstanceOf[ArrayValue]
          currentValue = arrVal.get(ind)
        }

        currentValue
      case NestedExpr(e, _) => interpretExpr(e)
    }

  /** Interprets a given expression as a boolean. Will throw an exception if the type of the expression is not a boolean.
    *
    * @param e The expression to be evaluated as a boolean
    * @return The boolean as a result of evalutaing `e`
    */
  private def interpretBool(e: Expr)(using scope: VariableScope): Boolean =
    e match {
      case Not(e) => !interpretBool(e)

      case Greater(e1, e2)   => binaryCompare(e1, e2, _ > _, _ > _)
      case GreaterEq(e1, e2) => binaryCompare(e1, e2, _ >= _, _ >= _)
      case Smaller(e1, e2)   => binaryCompare(e1, e2, _ < _, _ < _)
      case SmallerEq(e1, e2) => binaryCompare(e1, e2, _ <= _, _ <= _)
      case Equals(e1, e2)    => interpretExpr(e1) == interpretExpr(e2)
      case NotEquals(e1, e2) => interpretExpr(e1) != interpretExpr(e2)
      case And(e1, e2)       => binaryBoolOp(e1, e2, _ && _)
      case Or(e1, e2)        => binaryBoolOp(e1, e2, _ || _)

      case BoolLiter(b) => b

      case Ident(id, KnownType.BoolType) =>
        scope.get(id) match {
          case Some(b: Boolean) => b
          case Some(_)          => throw TypeMismatchException("Expected bool.")
          case None             => throw VariableNotFoundException(getVariableErrorString(id))
        } // TODO: Duplicated code

      case _ => throw TypeMismatchException("Expected bool.")
    }

  /** Interprets a given expression as an Int. Will throw an exception if the type of the expression is not a Int.
    *
    * @param e The expression to be evaluated as an Int
    * @return The Int as a result of evalutaing `e`
    */
  private def interpretInt(e: Expr)(using scope: VariableScope): Int =
    e match {
      case Negate(e) => -interpretInt(e)
      case Ord(e)    => interpretChar(e).toInt // TODO: Check if this is consistent with WACC ord
      case Len(e)    => interpretExpr(e).asInstanceOf[ArrayValue].length

      case Mult(e1, e2) => binaryArithmetic(e1, e2, _ * _)
      case Mod(e1, e2)  => binaryArithmetic(e1, e2, _ % _)
      case Add(e1, e2)  => binaryArithmetic(e1, e2, _ + _)
      case Div(e1, e2)  => binaryArithmetic(e1, e2, _ / _)
      case Sub(e1, e2)  => binaryArithmetic(e1, e2, _ - _)

      case IntLiter(x) => x

      case Ident(id, KnownType.IntType) =>
        scope.get(id) match {
          case Some(x: Int) => x
          case Some(_)      => throw TypeMismatchException("Expected int.")
          case None         => throw VariableNotFoundException(getVariableErrorString(id))
        }

      case _ => throw TypeMismatchException("Expected int.")
    }

  /** Interprets a given expression as an Char. Will throw an exception if the type of the expression is not a Char.
    *
    * @param e The expression to be evaluated as an Char
    * @return The Char as a result of evalutaing `e`
    */
  private def interpretChar(e: Expr)(using scope: VariableScope): Char =
    e match {
      case Chr(e)       => interpretInt(e).toChar
      case CharLiter(c) => c

      case Ident(id, KnownType.CharType) =>
        scope.get(id) match {
          case Some(c: Char) => c
          case Some(_)       => throw TypeMismatchException("Expected char.")
          case None          => throw VariableNotFoundException(getVariableErrorString(id))
        }

      case _ => throw TypeMismatchException("Expected char.")
    }

  /** Applies a given arithmetic operation on two integer expressions.
    *
    * @param e1 The first integer expression
    * @param e2 The second integer expression
    * @param op The arithmetic operation to apply to the expressions
    * @return The Int as a result of applying the operation on the expressions.
    */
  private def binaryArithmetic(e1: Expr, e2: Expr, op: (Int, Int) => Int)(using scope: VariableScope): Int =
    op(
      interpretInt(e1),
      interpretInt(e2)
    )

  /** Applies a given boolean operation on two boolean expressions.
    * 
    * @param e1 The first boolean expression
    * @param e2 The second boolean expression
    * @param op The boolean operation to apply to the expression
    * @return The boolean as a result of applying the operation on the expressions.
    */
  private def binaryBoolOp(e1: Expr, e2: Expr, op: (Boolean, Boolean) => Boolean)(using scope: VariableScope): Boolean =
    op(
      interpretBool(e1),
      interpretBool(e2)
    )

  /** Compares two expressions, either integers or characters, using the given comparators. 
   *
   * @param e1 The first expression to compare
   * @param e2 The second expression to compare
   * @param intComparator The comparator for integers
   * @param charComparator The comparator for characters
   * @return True if the comparison is true, false otherwise
   */
  private def binaryCompare(
      e1: Expr,
      e2: Expr,
      intComparator: (Int, Int) => Boolean,
      charComparator: (Char, Char) => Boolean
  )(using
      scope: VariableScope
  ): Boolean =
    val evalExpr2 = interpretExpr(e2)
    interpretExpr(e1) match {
      case i1: Int  => intComparator(i1, interpretInt(e2))
      case c1: Char => charComparator(c1, evalExpr2.asInstanceOf[Char])
      case _        => throw TypeMismatchException(ComparisonErrorString)
    }

  /** Handles an assignment statement, updating the variable scope accordingly.
    *
    * @param l The LValue to be assigned
    * @param r The RValue to assign to the LValue
    * @return The variable scope after the assignment
    */
  private def handleAssignment(l: LValue, r: RValue)(using
      scope: VariableScope,
      funcScope: FunctionScope
  ): VariableScope =
    l match {
      case Ident(id, _) =>
        scope.add(id, interpretRValue(r))
      case ArrayElem(v, es, semType) =>
        val nestedArray =
          interpretExpr(ArrayElem(v, es.init, semType))
            .asInstanceOf[ArrayValue] // Index through to the array that we want to change.
        val lastIndex = interpretExpr(es.last).asInstanceOf[Int]

        val newValue = interpretRValue(r)
        nestedArray.set(lastIndex, newValue)

        scope
      // Fetch the pair to set its values
      case Fst(l, _) =>
        unpackAsPair(getLValue(l)).setFst(interpretRValue(r))
        scope
      case Snd(l, _) =>
        unpackAsPair(getLValue(l)).setSnd(interpretRValue(r))
        scope
    }

  /** Gets the value associated with the given LValue.
   * 
   * @param l The LValue to get the associated value of
   * @return The associated value of the LValue
   */
  private def getLValue(l: LValue)(using scope: VariableScope): Value =
    l match {
      case idArr: (Ident | ArrayElem) => interpretExpr(idArr)
      case Fst(l, _)                  => unpackAsPair(getLValue(l)).getFst
      case Snd(l, _)                  => unpackAsPair(getLValue(l)).getSnd
    }

  /** Unpacks a value as a pair, throwing an exception if the value is not a pair.
   * 
   * @param value The value to unpack
   * @return The value as a pair
   */
  private def unpackAsPair(value: Value): PairValue =
    value match {
      case pair: PairValue       => pair
      case _: UninitalizedPair => throw NullDereferencedException()
      case _                     => throw TypeMismatchException(UnpackPairErrorString)
    }
}

object Interpreter {
  def interpret(
      p: Program,
      inheritedScope: VariableScope = MapContext(),
      inheritedFunctionScope: FunctionScope = MapContext()
  )(using
      inputStream: InputStream = InputStream(System.out),
      outputStream: OutputStream = OutputStream(System.in)
  ): (VariableScope, FunctionScope, Option[Int]) =
    val interpreter = new Interpreter(inputStream, outputStream)
    interpreter.interpret(p, inheritedScope, inheritedFunctionScope)
}
