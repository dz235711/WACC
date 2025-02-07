package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/pairs/"

  it should "pass badLookup01.wacc" in {
    runFrontend(Array(dir + "badLookup01.wacc"))._1 shouldBe 100
  }

  it should "pass badLookup02.wacc" in {
    runFrontend(Array(dir + "badLookup02.wacc"))._1 shouldBe 100
  }

  it should "pass elemOfNonPair.wacc" in {
    runFrontend(Array(dir + "elemOfNonPair.wacc"))._1 shouldBe 100
  }

  it should "pass fstNull.wacc" in {
    runFrontend(Array(dir + "fstNull.wacc"))._1 shouldBe 100
  }

  it should "pass noNesting.wacc" in {
    runFrontend(Array(dir + "noNesting.wacc"))._1 shouldBe 100
  }

  it should "pass sndNull.wacc" in {
    runFrontend(Array(dir + "sndNull.wacc"))._1 shouldBe 100
  }

}
