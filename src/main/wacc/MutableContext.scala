package wacc

import scala.collection.mutable.ListBuffer

class MutableContext[T](xs: ListBuffer[T] = new ListBuffer[T]) {

  /** Adds an object to the context list
    *
    * @param x The object to add
    */
  def add(x: T): MutableContext[T] = {
    xs += x
    this
  }

  /** Returns the list of objects in the context
    */
  def get: List[T] = xs.toList

}
