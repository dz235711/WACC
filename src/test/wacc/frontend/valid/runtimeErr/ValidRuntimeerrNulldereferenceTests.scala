package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "frontend analyse freeNull.wacc" taggedAs Frontend in {
    frontendStatus(dir + "freeNull.wacc") shouldBe 0
  }

  it should "frontend analyse readNull1.wacc" taggedAs Frontend in {
    frontendStatus(dir + "readNull1.wacc") shouldBe 0
  }

  it should "frontend analyse readNull2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "readNull2.wacc") shouldBe 0
  }

  it should "frontend analyse setNull1.wacc" taggedAs Frontend in {
    frontendStatus(dir + "setNull1.wacc") shouldBe 0
  }

  it should "frontend analyse setNull2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "setNull2.wacc") shouldBe 0
  }

  it should "frontend analyse useNull1.wacc" taggedAs Frontend in {
    frontendStatus(dir + "useNull1.wacc") shouldBe 0
  }

  it should "frontend analyse useNull2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "useNull2.wacc") shouldBe 0
  }

}
