package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "pass divZero.wacc" in pending /*{
    runFrontend(Array(dir+"divZero.wacc"))._1 shouldBe 0
  }*/

  it should "pass divideByZero.wacc" in pending /*{
    runFrontend(Array(dir+"divideByZero.wacc"))._1 shouldBe 0
  }*/

  it should "pass modByZero.wacc" in pending /*{
    runFrontend(Array(dir+"modByZero.wacc"))._1 shouldBe 0
  }*/

}