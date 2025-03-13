package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath

class SkipTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/skip/")

  it should "skip without doing anything" taggedAs Repl in {
    InterpreterTester(dir / "skip.in").getResult() shouldBe (MapContext(), MapContext(), Some(0))
  }
}
