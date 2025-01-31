package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrBasicTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/basic/"

  it should "pass badComment.wacc" in {
    runFrontend(Array(dir+"badComment.wacc"))._1 shouldBe 100
  }

  it should "pass badComment2.wacc" in {
    runFrontend(Array(dir+"badComment2.wacc"))._1 shouldBe 100
  }

  it should "pass badEscape.wacc" in {
    runFrontend(Array(dir+"badEscape.wacc"))._1 shouldBe 100
  }

  it should "pass beginNoend.wacc" in {
    runFrontend(Array(dir+"beginNoend.wacc"))._1 shouldBe 100
  }

  it should "pass bgnErr.wacc" in {
    runFrontend(Array(dir+"bgnErr.wacc"))._1 shouldBe 100
  }

  it should "pass multipleBegins.wacc" in {
    runFrontend(Array(dir+"multipleBegins.wacc"))._1 shouldBe 100
  }

  it should "pass noBody.wacc" in {
    runFrontend(Array(dir+"noBody.wacc"))._1 shouldBe 100
  }

  it should "pass skpErr.wacc" in {
    runFrontend(Array(dir+"skpErr.wacc"))._1 shouldBe 100
  }

  it should "pass unescapedChar.wacc" in {
    runFrontend(Array(dir+"unescapedChar.wacc"))._1 shouldBe 100
  }

}