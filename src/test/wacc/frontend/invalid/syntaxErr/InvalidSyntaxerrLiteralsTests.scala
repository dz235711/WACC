package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrLiteralsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/literals/"

  it should "pass charLiteralSingle.wacc" in {
    frontendStatus(dir + "charLiteralSingle.wacc") shouldBe 100
  }

  it should "pass stringLiteralNoNewlines.wacc" in {
    frontendStatus(dir + "stringLiteralNoNewlines.wacc") shouldBe 100
  }

  it should "pass stringLiteralOnlyAscii.wacc" in {
    frontendStatus(dir + "stringLiteralOnlyAscii.wacc") shouldBe 100
  }

}
