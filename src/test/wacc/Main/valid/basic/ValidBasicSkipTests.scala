package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "pass comment.wacc" in {
    runFrontend(Array(dir+"comment.wacc"))._1 shouldBe 0
  }

  it should "pass commentEoF.wacc" in {
    runFrontend(Array(dir+"commentEoF.wacc"))._1 shouldBe 0
  }

  it should "pass commentInLine.wacc" in {
    runFrontend(Array(dir+"commentInLine.wacc"))._1 shouldBe 0
  }

  it should "pass skip.wacc" in {
    runFrontend(Array(dir+"skip.wacc"))._1 shouldBe 0
  }

}