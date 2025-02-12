package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "frontend analyse if1.wacc" in {
    frontendStatus(dir + "if1.wacc") shouldBe 0
  }

  it should "frontend analyse if2.wacc" in {
    frontendStatus(dir + "if2.wacc") shouldBe 0
  }

  it should "frontend analyse if3.wacc" in {
    frontendStatus(dir + "if3.wacc") shouldBe 0
  }

  it should "frontend analyse if4.wacc" in {
    frontendStatus(dir + "if4.wacc") shouldBe 0
  }

  it should "frontend analyse if5.wacc" in {
    frontendStatus(dir + "if5.wacc") shouldBe 0
  }

  it should "frontend analyse if6.wacc" in {
    frontendStatus(dir + "if6.wacc") shouldBe 0
  }

  it should "frontend analyse ifBasic.wacc" in {
    frontendStatus(dir + "ifBasic.wacc") shouldBe 0
  }

  it should "frontend analyse ifFalse.wacc" in {
    frontendStatus(dir + "ifFalse.wacc") shouldBe 0
  }

  it should "frontend analyse ifTrue.wacc" in {
    frontendStatus(dir + "ifTrue.wacc") shouldBe 0
  }

  it should "frontend analyse whitespace.wacc" in {
    frontendStatus(dir + "whitespace.wacc") shouldBe 0
  }

}
