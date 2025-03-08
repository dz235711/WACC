package wacc

case class AccessFreedValueException(message: String) extends Exception(message)
case class FreedNonFreeableValueException(message: String) extends Exception(message)
case class NullDereferencedException(message: String) extends Exception(message)

case class VariableNotFoundException(message: String) extends Exception(message)
case class FunctionNotFoundException(message: String) extends Exception(message)

case class TypeMismatchException(message: String) extends Exception(message)

case class BadInputException(message: String) extends Exception(message)
