package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/while/"

  it should "pass donoErr.wacc" in {
    frontendStatus(dir + "donoErr.wacc") shouldBe 100
  }

  it should "pass dooErr.wacc" in {
    frontendStatus(dir + "dooErr.wacc") shouldBe 100
  }

  it should "pass whilErr.wacc" in {
    frontendStatus(dir + "whilErr.wacc") shouldBe 100
  }

  it should "pass whileNodo.wacc" in {
    frontendStatus(dir + "whileNodo.wacc") shouldBe 100
  }

  it should "pass whileNodone.wacc" in {
    frontendStatus(dir + "whileNodone.wacc") shouldBe 100
  }

}
