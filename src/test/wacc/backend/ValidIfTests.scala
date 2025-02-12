package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "pass if1.wacc" in {
    frontendStatus(dir + "if1.wacc") shouldBe 0
  }

  it should "pass if2.wacc" in {
    frontendStatus(dir + "if2.wacc") shouldBe 0
  }

  it should "pass if3.wacc" in {
    frontendStatus(dir + "if3.wacc") shouldBe 0
  }

  it should "pass if4.wacc" in {
    frontendStatus(dir + "if4.wacc") shouldBe 0
  }

  it should "pass if5.wacc" in {
    frontendStatus(dir + "if5.wacc") shouldBe 0
  }

  it should "pass if6.wacc" in {
    frontendStatus(dir + "if6.wacc") shouldBe 0
  }

  it should "pass ifBasic.wacc" in {
    frontendStatus(dir + "ifBasic.wacc") shouldBe 0
  }

  it should "pass ifFalse.wacc" in {
    frontendStatus(dir + "ifFalse.wacc") shouldBe 0
  }

  it should "pass ifTrue.wacc" in {
    frontendStatus(dir + "ifTrue.wacc") shouldBe 0
  }

  it should "pass whitespace.wacc" in {
    frontendStatus(dir + "whitespace.wacc") shouldBe 0
  }

}
