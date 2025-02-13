package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/pairs/"

  it should "frontend analyse badLookup01.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badLookup01.wacc") shouldBe 100
  }

  it should "frontend analyse badLookup02.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badLookup02.wacc") shouldBe 100
  }

  it should "frontend analyse elemOfNonPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "elemOfNonPair.wacc") shouldBe 100
  }

  it should "frontend analyse fstNull.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "fstNull.wacc") shouldBe 100
  }

  it should "frontend analyse noNesting.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "noNesting.wacc") shouldBe 100
  }

  it should "frontend analyse sndNull.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "sndNull.wacc") shouldBe 100
  }

}
