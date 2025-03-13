package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import scala.collection.mutable.Map as MMap
import os.RelPath
import scala.collection.mutable.ListBuffer

class AsgnTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/asgn/")

  "reassignment" should "update the variable" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 42
      )
    )

    InterpreterTester(dir / "reasgn.in").getResult()._1 shouldBe resultScope
  }

  "array element reassignment" should "update the array element" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> ArrayValue(ListBuffer(false, false))
      )
    )

    InterpreterTester(dir / "arrayElemReasgn.in").getResult()._1 shouldBe resultScope
  }

  "arrays passed by reference" should "update in all arrays" taggedAs Repl in {
    val arrVal1 = ArrayValue(ListBuffer(1, 2, 420, 7))

    val resultScope = MapContext(
      MMap(
        1 -> arrVal1,
        2 -> ArrayValue(ListBuffer(arrVal1))
      )
    )

    InterpreterTester(dir / "arrayReferenceReasgn.in").getResult()._1 shouldBe resultScope
  }

  "nested array element reassignment" should "update the nested array element" taggedAs Repl in {
    val arrVal1 = ArrayValue(ListBuffer('a', 'f', 'c'))
    val arrVal2 = ArrayValue(ListBuffer(arrVal1))

    val resultScope = MapContext(
      MMap(
        1 -> arrVal1,
        2 -> arrVal2,
        3 -> ArrayValue(ListBuffer(arrVal2))
      )
    )

    InterpreterTester(dir / "nestedArrayElemReasgn.in").getResult()._1 shouldBe resultScope
  }
}
