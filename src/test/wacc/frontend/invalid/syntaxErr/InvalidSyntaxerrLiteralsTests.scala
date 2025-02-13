package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrLiteralsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/literals/"

  it should "frontend analyse charLiteralSingle.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "charLiteralSingle.wacc") shouldBe 100
  }

  it should "frontend analyse stringLiteralNoNewlines.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "stringLiteralNoNewlines.wacc") shouldBe 100
  }

  it should "frontend analyse stringLiteralOnlyAscii.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "stringLiteralOnlyAscii.wacc") shouldBe 100
  }

}
