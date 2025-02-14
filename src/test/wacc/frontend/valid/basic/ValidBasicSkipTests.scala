package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "frontend analyse comment.wacc" taggedAs Frontend in {
    frontendStatus(dir + "comment.wacc") shouldBe 0
  }

  it should "frontend analyse commentEoF.wacc" taggedAs Frontend in {
    frontendStatus(dir + "commentEoF.wacc") shouldBe 0
  }

  it should "frontend analyse commentInLine.wacc" taggedAs Frontend in {
    frontendStatus(dir + "commentInLine.wacc") shouldBe 0
  }

  it should "frontend analyse skip.wacc" taggedAs Frontend in {
    frontendStatus(dir + "skip.wacc") shouldBe 0
  }

}
