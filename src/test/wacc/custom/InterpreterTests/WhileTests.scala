package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath
import scala.collection.mutable.Map as MMap

class WhileTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/while/")

  it should "execute a simple while loop" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 10
      )
    )

    InterpreterTester(dir / "whileSimple.in").getResult()._1 shouldBe resultScope
  }

  it should "not enter a while loop with a false condition" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 99
      )
    )

    InterpreterTester(dir / "whileNotEntered.in").getResult()._1 shouldBe resultScope
  }
}
