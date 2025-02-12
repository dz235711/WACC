package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "frontend analyse comment.wacc" in {
    frontendStatus(dir + "comment.wacc") shouldBe 0
  }

  it should "frontend analyse commentEoF.wacc" in {
    frontendStatus(dir + "commentEoF.wacc") shouldBe 0
  }

  it should "frontend analyse commentInLine.wacc" in {
    frontendStatus(dir + "commentInLine.wacc") shouldBe 0
  }

  it should "frontend analyse skip.wacc" in {
    frontendStatus(dir + "skip.wacc") shouldBe 0
  }

}
