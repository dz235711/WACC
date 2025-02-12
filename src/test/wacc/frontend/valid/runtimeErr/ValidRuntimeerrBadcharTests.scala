package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrBadcharTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/badChar/"

  it should "frontend analyse negativeChr.wacc" in {
    frontendStatus(dir + "negativeChr.wacc") shouldBe 0
  }

  it should "frontend analyse tooBigChr.wacc" in {
    frontendStatus(dir + "tooBigChr.wacc") shouldBe 0
  }

}
