package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/pairs/"

  it should "pass badLookup01.wacc" in {
    frontendStatus(dir + "badLookup01.wacc") shouldBe 100
  }

  it should "pass badLookup02.wacc" in {
    frontendStatus(dir + "badLookup02.wacc") shouldBe 100
  }

  it should "pass elemOfNonPair.wacc" in {
    frontendStatus(dir + "elemOfNonPair.wacc") shouldBe 100
  }

  it should "pass fstNull.wacc" in {
    frontendStatus(dir + "fstNull.wacc") shouldBe 100
  }

  it should "pass noNesting.wacc" in {
    frontendStatus(dir + "noNesting.wacc") shouldBe 100
  }

  it should "pass sndNull.wacc" in {
    frontendStatus(dir + "sndNull.wacc") shouldBe 100
  }

}
