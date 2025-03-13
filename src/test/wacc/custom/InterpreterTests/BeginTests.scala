package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath
import scala.collection.mutable.Map as MMap

class BeginTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/begin/")

  it should "allow shadowing of variables" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> "parent",
        2 -> 'c'
      )
    )

    InterpreterTester(dir / "beginShadow.in").getResult()._1 shouldBe resultScope
  }

  it should "allow access to variables from parent scope" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 0,
        2 -> '\u0000'
      )
    )

    InterpreterTester(dir / "beginParentAccess.in").getResult()._1 shouldBe resultScope
  }

  it should "not allow access of variables after the scope is exited" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 13
      )
    )

    InterpreterTester(dir / "beginIllegalAccess.in").getResult()._1 shouldBe resultScope
  }
}
