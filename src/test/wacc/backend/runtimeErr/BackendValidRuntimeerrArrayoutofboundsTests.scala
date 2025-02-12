package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrArrayoutofboundsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/arrayOutOfBounds/"

  it should "correctly execute arrayNegBounds.wacc" in pending /*{
    fullExec(dir + arrayNegBounds.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute arrayOutOfBounds.wacc" in pending /*{
    fullExec(dir + arrayOutOfBounds.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute arrayOutOfBoundsWrite.wacc" in pending /*{
    fullExec(dir + arrayOutOfBoundsWrite.wacc, "") shouldBe Some("#runtime_error#")
  }*/

}