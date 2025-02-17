package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrIntegeroverflowTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/integerOverflow/"

  it should "frontend analyse intJustOverflow.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intJustOverflow.wacc") shouldBe 0
  }

  it should "frontend analyse intUnderflow.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intUnderflow.wacc") shouldBe 0
  }

  it should "frontend analyse intWayOverflow.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intWayOverflow.wacc") shouldBe 0
  }

  it should "frontend analyse intmultOverflow.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intmultOverflow.wacc") shouldBe 0
  }

  it should "frontend analyse intnegateOverflow.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intnegateOverflow.wacc") shouldBe 0
  }

  it should "frontend analyse intnegateOverflow2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intnegateOverflow2.wacc") shouldBe 0
  }

  it should "frontend analyse intnegateOverflow3.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intnegateOverflow3.wacc") shouldBe 0
  }

  it should "frontend analyse intnegateOverflow4.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intnegateOverflow4.wacc") shouldBe 0
  }

}
