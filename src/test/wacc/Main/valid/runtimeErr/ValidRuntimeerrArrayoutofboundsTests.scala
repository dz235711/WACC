package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrArrayoutofboundsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/arrayOutOfBounds/"

  it should "pass arrayNegBounds.wacc" in {
    runFrontend(Array(dir+"arrayNegBounds.wacc"))._1 shouldBe 0
  }

  it should "pass arrayOutOfBounds.wacc" in {
    runFrontend(Array(dir+"arrayOutOfBounds.wacc"))._1 shouldBe 0
  }

  it should "pass arrayOutOfBoundsWrite.wacc" in {
    runFrontend(Array(dir+"arrayOutOfBoundsWrite.wacc"))._1 shouldBe 0
  }

}