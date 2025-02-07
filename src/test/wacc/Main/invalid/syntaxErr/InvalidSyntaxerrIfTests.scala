package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/if/"

  it should "pass ifNoelse.wacc" in {
    runFrontend(Array(dir + "ifNoelse.wacc"))._1 shouldBe 100
  }

  it should "pass ifNofi.wacc" in {
    runFrontend(Array(dir + "ifNofi.wacc"))._1 shouldBe 100
  }

  it should "pass ifNothen.wacc" in {
    runFrontend(Array(dir + "ifNothen.wacc"))._1 shouldBe 100
  }

  it should "pass ifiErr.wacc" in {
    runFrontend(Array(dir + "ifiErr.wacc"))._1 shouldBe 100
  }

}
