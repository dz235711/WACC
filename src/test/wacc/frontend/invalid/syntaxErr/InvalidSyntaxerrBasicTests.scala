package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrBasicTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/basic/"

  it should "frontend analyse badComment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badComment.wacc") shouldBe 100
  }

  it should "frontend analyse badComment2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badComment2.wacc") shouldBe 100
  }

  it should "frontend analyse badEscape.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badEscape.wacc") shouldBe 100
  }

  it should "frontend analyse beginNoend.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "beginNoend.wacc") shouldBe 100
  }

  it should "frontend analyse bgnErr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "bgnErr.wacc") shouldBe 100
  }

  it should "frontend analyse multipleBegins.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "multipleBegins.wacc") shouldBe 100
  }

  it should "frontend analyse noBody.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "noBody.wacc") shouldBe 100
  }

  it should "frontend analyse skpErr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "skpErr.wacc") shouldBe 100
  }

  it should "frontend analyse unescapedChar.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "unescapedChar.wacc") shouldBe 100
  }

}
