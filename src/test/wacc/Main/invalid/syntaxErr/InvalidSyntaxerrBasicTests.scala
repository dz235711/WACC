package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrBasicTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/basic/"

  it should "pass badComment.wacc" in {
    frontendStatus(dir + "badComment.wacc") shouldBe 100
  }

  it should "pass badComment2.wacc" in {
    frontendStatus(dir + "badComment2.wacc") shouldBe 100
  }

  it should "pass badEscape.wacc" in {
    frontendStatus(dir + "badEscape.wacc") shouldBe 100
  }

  it should "pass beginNoend.wacc" in {
    frontendStatus(dir + "beginNoend.wacc") shouldBe 100
  }

  it should "pass bgnErr.wacc" in {
    frontendStatus(dir + "bgnErr.wacc") shouldBe 100
  }

  it should "pass multipleBegins.wacc" in {
    frontendStatus(dir + "multipleBegins.wacc") shouldBe 100
  }

  it should "pass noBody.wacc" in {
    frontendStatus(dir + "noBody.wacc") shouldBe 100
  }

  it should "pass skpErr.wacc" in {
    frontendStatus(dir + "skpErr.wacc") shouldBe 100
  }

  it should "pass unescapedChar.wacc" in {
    frontendStatus(dir + "unescapedChar.wacc") shouldBe 100
  }

}
