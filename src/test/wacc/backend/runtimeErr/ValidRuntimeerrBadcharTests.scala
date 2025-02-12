package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrBadcharTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/badChar/"

  it should "pass negativeChr.wacc" in {
    frontendStatus(dir + "negativeChr.wacc") shouldBe 0
  }

  it should "pass tooBigChr.wacc" in {
    frontendStatus(dir + "tooBigChr.wacc") shouldBe 0
  }

}
