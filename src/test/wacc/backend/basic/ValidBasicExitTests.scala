package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidBasicExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/exit/"

  it should "pass exit-1.wacc" in {
    frontendStatus(dir + "exit-1.wacc") shouldBe 0
  }

  it should "pass exitBasic.wacc" in {
    frontendStatus(dir + "exitBasic.wacc") shouldBe 0
  }

  it should "pass exitBasic2.wacc" in {
    frontendStatus(dir + "exitBasic2.wacc") shouldBe 0
  }

  it should "pass exitWrap.wacc" in {
    frontendStatus(dir + "exitWrap.wacc") shouldBe 0
  }

}
