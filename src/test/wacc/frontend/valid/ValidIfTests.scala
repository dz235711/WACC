package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "frontend analyse if1.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if1.wacc") shouldBe 0
  }

  it should "frontend analyse if2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if2.wacc") shouldBe 0
  }

  it should "frontend analyse if3.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if3.wacc") shouldBe 0
  }

  it should "frontend analyse if4.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if4.wacc") shouldBe 0
  }

  it should "frontend analyse if5.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if5.wacc") shouldBe 0
  }

  it should "frontend analyse if6.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "if6.wacc") shouldBe 0
  }

  it should "frontend analyse ifBasic.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ifBasic.wacc") shouldBe 0
  }

  it should "frontend analyse ifFalse.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ifFalse.wacc") shouldBe 0
  }

  it should "frontend analyse ifTrue.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ifTrue.wacc") shouldBe 0
  }

  it should "frontend analyse whitespace.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "whitespace.wacc") shouldBe 0
  }

}
