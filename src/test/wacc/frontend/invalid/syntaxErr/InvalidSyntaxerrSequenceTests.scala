package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/sequence/"

  it should "frontend analyse doubleSeq.wacc" taggedAs Frontend in {
    frontendStatus(dir + "doubleSeq.wacc") shouldBe 100
  }

  it should "frontend analyse emptySeq.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptySeq.wacc") shouldBe 100
  }

  it should "frontend analyse endSeq.wacc" taggedAs Frontend in {
    frontendStatus(dir + "endSeq.wacc") shouldBe 100
  }

  it should "frontend analyse extraSeq.wacc" taggedAs Frontend in {
    frontendStatus(dir + "extraSeq.wacc") shouldBe 100
  }

  it should "frontend analyse missingSeq.wacc" taggedAs Frontend in {
    frontendStatus(dir + "missingSeq.wacc") shouldBe 100
  }

}
