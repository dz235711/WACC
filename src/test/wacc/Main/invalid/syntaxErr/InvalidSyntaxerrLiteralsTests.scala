package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrLiteralsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/literals/"

  it should "pass charLiteralSingle.wacc" in pending /*{
    runFrontend(Array(dir+"charLiteralSingle.wacc"))._1 shouldBe 100
  }*/

  it should "pass stringLiteralNoNewlines.wacc" in pending /*{
    runFrontend(Array(dir+"stringLiteralNoNewlines.wacc"))._1 shouldBe 100
  }*/

  it should "pass stringLiteralOnlyAscii.wacc" in pending /*{
    runFrontend(Array(dir+"stringLiteralOnlyAscii.wacc"))._1 shouldBe 100
  }*/

}