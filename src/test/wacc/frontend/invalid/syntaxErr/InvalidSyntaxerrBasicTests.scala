package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrBasicTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/basic/"

  it should "frontend analyse badComment.wacc" in {
    frontendStatus(dir + "badComment.wacc") shouldBe 100
  }

  it should "frontend analyse badComment2.wacc" in {
    frontendStatus(dir + "badComment2.wacc") shouldBe 100
  }

  it should "frontend analyse badEscape.wacc" in {
    frontendStatus(dir + "badEscape.wacc") shouldBe 100
  }

  it should "frontend analyse beginNoend.wacc" in {
    frontendStatus(dir + "beginNoend.wacc") shouldBe 100
  }

  it should "frontend analyse bgnErr.wacc" in {
    frontendStatus(dir + "bgnErr.wacc") shouldBe 100
  }

  it should "frontend analyse multipleBegins.wacc" in {
    frontendStatus(dir + "multipleBegins.wacc") shouldBe 100
  }

  it should "frontend analyse noBody.wacc" in {
    frontendStatus(dir + "noBody.wacc") shouldBe 100
  }

  it should "frontend analyse skpErr.wacc" in {
    frontendStatus(dir + "skpErr.wacc") shouldBe 100
  }

  it should "frontend analyse unescapedChar.wacc" in {
    frontendStatus(dir + "unescapedChar.wacc") shouldBe 100
  }

}
