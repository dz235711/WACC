package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrIntegeroverflowTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/integerOverflow/"

  it should "pass intJustOverflow.wacc" in {
    frontendStatus(dir + "intJustOverflow.wacc") shouldBe 0
  }

  it should "pass intUnderflow.wacc" in {
    frontendStatus(dir + "intUnderflow.wacc") shouldBe 0
  }

  it should "pass intWayOverflow.wacc" in {
    frontendStatus(dir + "intWayOverflow.wacc") shouldBe 0
  }

  it should "pass intmultOverflow.wacc" in {
    frontendStatus(dir + "intmultOverflow.wacc") shouldBe 0
  }

  it should "pass intnegateOverflow.wacc" in {
    frontendStatus(dir + "intnegateOverflow.wacc") shouldBe 0
  }

  it should "pass intnegateOverflow2.wacc" in {
    frontendStatus(dir + "intnegateOverflow2.wacc") shouldBe 0
  }

  it should "pass intnegateOverflow3.wacc" in {
    frontendStatus(dir + "intnegateOverflow3.wacc") shouldBe 0
  }

  it should "pass intnegateOverflow4.wacc" in {
    frontendStatus(dir + "intnegateOverflow4.wacc") shouldBe 0
  }

}
