package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrDividebyzeroTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/divideByZero/"

  it should "execute divZero.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + divZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute divideByZero.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + divideByZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute modByZero.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + modByZero.wacc, "") shouldBe Some("#runtime_error#")
  }*/

}
