package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath
import scala.collection.mutable.Map as MMap

class IfTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/if/")

  it should "choose the first branch when condition is true" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> "in true"
      )
    )

    InterpreterTester(dir / "ifTrue.in").getResult()._1 shouldBe resultScope
  }

  it should "choose the second branch when condition is false" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> "in false"
      )
    )

    InterpreterTester(dir / "ifFalse.in").getResult()._1 shouldBe resultScope
  }
}
