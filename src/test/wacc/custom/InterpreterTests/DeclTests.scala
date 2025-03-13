package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import scala.collection.mutable.Map as MMap
import os.RelPath
import scala.collection.mutable.ListBuffer

class DeclTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/decl/")

  "bool declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> true
      )
    )

    InterpreterTester(dir / "declBool.in").getResult()._1 shouldBe resultScope
  }

  "int declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 0
      )
    )

    InterpreterTester(dir / "declInt.in").getResult()._1 shouldBe resultScope
  }

  "char declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 'a'
      )
    )

    InterpreterTester(dir / "declChar.in").getResult()._1 shouldBe resultScope
  }

  "string declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> "Hello, world!"
      )
    )

    InterpreterTester(dir / "declString.in").getResult()._1 shouldBe resultScope
  }

  "array declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> ArrayValue(ListBuffer('c', 'a', 't', 's'))
      )
    )

    InterpreterTester(dir / "declArray.in").getResult()._1 shouldBe resultScope
  }

  "pair declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> PairValue(true, '7')
      )
    )

    InterpreterTester(dir / "declPair.in").getResult()._1 shouldBe resultScope
  }

  "string using char array declarations" should "be in scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> ArrayValue(ListBuffer('I', 'a', 'm'))
      )
    )

    InterpreterTester(dir / "declStringUsingCharArray.in").getResult()._1 shouldBe resultScope
  }
}
