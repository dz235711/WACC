package wacc

import wacc.TypedAST.{Ident, Stmt}

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

    def enterFinally() =
      enteredFinally = true
  }

  case class InterpreterExitException(exitCode: Int) extends Exception("Exited WACC Interpreter.")
  case class InterpreterWACCException(exceptionCode: Int) extends Exception(s"WACC Exception: $exceptionCode.")

  case class AccessFreedValueException(message: String = "Cannot dereference freed pair") extends Exception(message)
  case class FreedNonFreeableValueException(message: String = "Cannot free non-freeable value")
      extends Exception(message)
  case class NullDereferencedException(message: String = "Cannot dereference null pointer") extends Exception(message)

  case class VariableNotFoundException(message: String) extends Exception(message)
  case class FunctionNotFoundException(message: String) extends Exception(message)

  case class TypeMismatchException(message: String) extends Exception(message)

  case class BadInputException(message: String) extends Exception(message)
}
