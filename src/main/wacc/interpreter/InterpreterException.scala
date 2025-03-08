package wacc

case class AccessFreedValueException(message: String = "Cannot dereference freed pair") extends Exception(message)
case class FreedNonFreeableValueException(message: String = "Cannot free non-freeable value") extends Exception(message)
case class NullDereferencedException(message: String = "Cannot dereference null pointer") extends Exception(message)

case class VariableNotFoundException(message: String) extends Exception(message)
case class FunctionNotFoundException(message: String) extends Exception(message)

case class TypeMismatchException(message: String) extends Exception(message)

case class BadInputException(message: String) extends Exception(message)
