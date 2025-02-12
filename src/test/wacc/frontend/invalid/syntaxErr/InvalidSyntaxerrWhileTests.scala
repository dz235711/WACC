package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/while/"

  it should "frontend analyse donoErr.wacc" in {
    frontendStatus(dir + "donoErr.wacc") shouldBe 100
  }

  it should "frontend analyse dooErr.wacc" in {
    frontendStatus(dir + "dooErr.wacc") shouldBe 100
  }

  it should "frontend analyse whilErr.wacc" in {
    frontendStatus(dir + "whilErr.wacc") shouldBe 100
  }

  it should "frontend analyse whileNodo.wacc" in {
    frontendStatus(dir + "whileNodo.wacc") shouldBe 100
  }

  it should "frontend analyse whileNodone.wacc" in {
    frontendStatus(dir + "whileNodone.wacc") shouldBe 100
  }

}
