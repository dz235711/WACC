package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/if/"

  it should "frontend analyse ifNoelse.wacc" in {
    frontendStatus(dir + "ifNoelse.wacc") shouldBe 100
  }

  it should "frontend analyse ifNofi.wacc" in {
    frontendStatus(dir + "ifNofi.wacc") shouldBe 100
  }

  it should "frontend analyse ifNothen.wacc" in {
    frontendStatus(dir + "ifNothen.wacc") shouldBe 100
  }

  it should "frontend analyse ifiErr.wacc" in {
    frontendStatus(dir + "ifiErr.wacc") shouldBe 100
  }

}
