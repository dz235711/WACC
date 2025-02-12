package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "correctly execute divZero.wacc" in pending /*{
    fullExec(dir + divZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute divideByZero.wacc" in pending /*{
    fullExec(dir + divideByZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute modByZero.wacc" in pending /*{
    fullExec(dir + modByZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

}