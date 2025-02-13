package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "frontend analyse divZero.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "divZero.wacc") shouldBe 0
  }

  it should "frontend analyse divideByZero.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "divideByZero.wacc") shouldBe 0
  }

  it should "frontend analyse modByZero.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "modByZero.wacc") shouldBe 0
  }

}
