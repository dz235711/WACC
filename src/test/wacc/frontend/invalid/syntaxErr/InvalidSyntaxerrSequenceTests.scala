package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/sequence/"

  it should "pass doubleSeq.wacc" in {
    frontendStatus(dir + "doubleSeq.wacc") shouldBe 100
  }

  it should "pass emptySeq.wacc" in {
    frontendStatus(dir + "emptySeq.wacc") shouldBe 100
  }

  it should "pass endSeq.wacc" in {
    frontendStatus(dir + "endSeq.wacc") shouldBe 100
  }

  it should "pass extraSeq.wacc" in {
    frontendStatus(dir + "extraSeq.wacc") shouldBe 100
  }

  it should "pass missingSeq.wacc" in {
    frontendStatus(dir + "missingSeq.wacc") shouldBe 100
  }

}
