package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "pass divZero.wacc" in {
    frontendStatus(dir + "divZero.wacc") shouldBe 0
  }

  it should "pass divideByZero.wacc" in {
    frontendStatus(dir + "divideByZero.wacc") shouldBe 0
  }

  it should "pass modByZero.wacc" in {
    frontendStatus(dir + "modByZero.wacc") shouldBe 0
  }

}
