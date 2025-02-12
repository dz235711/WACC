package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "frontend analyse divZero.wacc" in {
    frontendStatus(dir + "divZero.wacc") shouldBe 0
  }

  it should "frontend analyse divideByZero.wacc" in {
    frontendStatus(dir + "divideByZero.wacc") shouldBe 0
  }

  it should "frontend analyse modByZero.wacc" in {
    frontendStatus(dir + "modByZero.wacc") shouldBe 0
  }

}
