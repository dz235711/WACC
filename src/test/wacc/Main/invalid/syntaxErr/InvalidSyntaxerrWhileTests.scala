package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/while/"

  it should "pass donoErr.wacc" in pending /*{
    runFrontend(Array(dir+"donoErr.wacc"))._1 shouldBe 100
  }*/

  it should "pass dooErr.wacc" in pending /*{
    runFrontend(Array(dir+"dooErr.wacc"))._1 shouldBe 100
  }*/

  it should "pass whilErr.wacc" in pending /*{
    runFrontend(Array(dir+"whilErr.wacc"))._1 shouldBe 100
  }*/

  it should "pass whileNodo.wacc" in pending /*{
    runFrontend(Array(dir+"whileNodo.wacc"))._1 shouldBe 100
  }*/

  it should "pass whileNodone.wacc" in pending /*{
    runFrontend(Array(dir+"whileNodone.wacc"))._1 shouldBe 100
  }*/

}