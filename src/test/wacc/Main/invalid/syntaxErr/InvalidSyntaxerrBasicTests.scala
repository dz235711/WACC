package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrBasicTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/basic/"

  it should "pass badComment.wacc" in pending /*{
    runFrontend(Array(dir+"badComment.wacc"))._1 shouldBe 100
  }*/

  it should "pass badComment2.wacc" in pending /*{
    runFrontend(Array(dir+"badComment2.wacc"))._1 shouldBe 100
  }*/

  it should "pass badEscape.wacc" in pending /*{
    runFrontend(Array(dir+"badEscape.wacc"))._1 shouldBe 100
  }*/

  it should "pass beginNoend.wacc" in pending /*{
    runFrontend(Array(dir+"beginNoend.wacc"))._1 shouldBe 100
  }*/

  it should "pass bgnErr.wacc" in pending /*{
    runFrontend(Array(dir+"bgnErr.wacc"))._1 shouldBe 100
  }*/

  it should "pass multipleBegins.wacc" in pending /*{
    runFrontend(Array(dir+"multipleBegins.wacc"))._1 shouldBe 100
  }*/

  it should "pass noBody.wacc" in pending /*{
    runFrontend(Array(dir+"noBody.wacc"))._1 shouldBe 100
  }*/

  it should "pass skpErr.wacc" in pending /*{
    runFrontend(Array(dir+"skpErr.wacc"))._1 shouldBe 100
  }*/

  it should "pass unescapedChar.wacc" in pending /*{
    runFrontend(Array(dir+"unescapedChar.wacc"))._1 shouldBe 100
  }*/

}