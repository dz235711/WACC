package wacc

import wacc.TypedAST.{Ident, Stmt}
import wacc.Clib.{NullPairString, DivZeroString, OverflowString, BadCharString}

object InterpreterException {
  case class CatchFinally(catchIdent: Ident, catchStmt: Stmt, finallyStmt: Stmt) {
    private var depthCounter = 0
    private var enteredFinally = false

    def shouldEnterFinally =
      depthCounter == 0 && !enteredFinally

    def increaseDepth() =
      depthCounter += 1

    def decreaseDepth() =
      depthCounter -= 1

    def clearDepth() =
      depthCounter = 0

    def enterFinally() =
      enteredFinally = true
  }

  case class ExitException(exitCode: Int) extends Exception("Exited WACC Interpreter.")
  case class WACCException(exceptionCode: Int) extends Exception(s"WACC Exception: $exceptionCode.")

  case class AccessFreedValueException(message: String = "Cannot dereference freed pair") extends Exception(message)
  case class FreedNonFreeableValueException(message: String = "Cannot free non-freeable value")
      extends Exception(message)
  case class NullDereferencedException(message: String = NullPairString) extends Exception(message)

  case class DivZeroException(message: String = DivZeroString) extends Exception(message)
  case class IntOverflowException(message: String = OverflowString) extends Exception(message)

  case class BadCharException(message: String = BadCharString) extends Exception(message)

  case class VariableNotFoundException(message: String) extends Exception(message)
  case class FunctionNotFoundException(message: String) extends Exception(message)

  case class TypeMismatchException(message: String) extends Exception(message)

  case class BadInputException(message: String) extends Exception(message)
}
