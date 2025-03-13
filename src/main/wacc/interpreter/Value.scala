package wacc

import scala.collection.mutable.ListBuffer

type Id = Int
type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | UninitalizedPair | ArrayValue

trait Freeable {
  var isFreed: Boolean = false
}

case class PairValue(private var fst: Value, private var snd: Value) extends Freeable {
  override def toString: String = s"($fst, $snd)"

  /** Checks if the pair has been freed. If it has, an exception is thrown. */
  private def checkFreed(): Unit = if (isFreed) throw new AccessFreedValueException()

  /** Gets the first value of the pair. */
  def getFst: Value = {
    checkFreed()
    fst
  }

  /** Gets the second value of the pair. */
  def getSnd: Value = {
    checkFreed()
    snd
  }

  /** Sets the first parameter of the pair. 
   *
   * @param value The value to set the first parameter to 
   */
  def setFst(value: Value): Unit = {
    checkFreed()
    fst = value
  }

  /** Sets the second parameter of the pair. 
   *
   * @param value The value to set the second parameter to 
   */
  def setSnd(value: Value): Unit = {
    checkFreed()
    snd = value
  }
}

class UninitalizedPair private ()
object UninitalizedPair {
  val instance = UninitalizedPair()
}

case class ArrayValue(private val es: ListBuffer[Value]) extends Freeable {
  override def toString: String = es.mkString("[", ", ", "]")

  /** Checks if the array has been freed. If it has, an exception is thrown. */
  private def checkFreed(): Unit = if (isFreed) throw new AccessFreedValueException()

  /** Gets the element at the given index. 
   *
   * @param index The index of the value to get
   * @return The element at the given index
   */
  def get(index: Int): Value = {
    checkFreed()
    es(index)
  }

  /** Sets the element at the given index. 
   *
   * @param index The index of the element to set
   * @param value The value to set the index element to
   */
  def set(index: Int, value: Value): Unit = {
    checkFreed()
    es(index) = value
  }

  /** Returns the length of the array. */
  def length: Int = es.length
}
