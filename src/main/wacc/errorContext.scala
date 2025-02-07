package wacc

import scala.collection.mutable.ListBuffer

class ErrorContext(errs: ListBuffer[WaccError] = new ListBuffer) {

  /** Adds an error to the error context
    *
    * @param err The error to add
    */
  def error(err: WaccError) = errs += err

  /** Returns the list of errors in the error context
    */
  def get: List[WaccError] = errs.toList

}
