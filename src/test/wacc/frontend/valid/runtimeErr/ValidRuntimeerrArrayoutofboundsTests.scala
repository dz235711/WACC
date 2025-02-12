package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrArrayoutofboundsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/arrayOutOfBounds/"

  it should "pass arrayNegBounds.wacc" in {
    frontendStatus(dir + "arrayNegBounds.wacc") shouldBe 0
  }

  it should "pass arrayOutOfBounds.wacc" in {
    frontendStatus(dir + "arrayOutOfBounds.wacc") shouldBe 0
  }

  it should "pass arrayOutOfBoundsWrite.wacc" in {
    frontendStatus(dir + "arrayOutOfBoundsWrite.wacc") shouldBe 0
  }

}
