package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "pass freeNull.wacc" in {
    frontendStatus(dir + "freeNull.wacc") shouldBe 0
  }

  it should "pass readNull1.wacc" in {
    frontendStatus(dir + "readNull1.wacc") shouldBe 0
  }

  it should "pass readNull2.wacc" in {
    frontendStatus(dir + "readNull2.wacc") shouldBe 0
  }

  it should "pass setNull1.wacc" in {
    frontendStatus(dir + "setNull1.wacc") shouldBe 0
  }

  it should "pass setNull2.wacc" in {
    frontendStatus(dir + "setNull2.wacc") shouldBe 0
  }

  it should "pass useNull1.wacc" in {
    frontendStatus(dir + "useNull1.wacc") shouldBe 0
  }

  it should "pass useNull2.wacc" in {
    frontendStatus(dir + "useNull2.wacc") shouldBe 0
  }

}
