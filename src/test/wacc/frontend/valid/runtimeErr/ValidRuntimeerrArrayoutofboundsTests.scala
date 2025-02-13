package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrArrayoutofboundsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/arrayOutOfBounds/"

  it should "frontend analyse arrayNegBounds.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "arrayNegBounds.wacc") shouldBe 0
  }

  it should "frontend analyse arrayOutOfBounds.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "arrayOutOfBounds.wacc") shouldBe 0
  }

  it should "frontend analyse arrayOutOfBoundsWrite.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "arrayOutOfBoundsWrite.wacc") shouldBe 0
  }

}
