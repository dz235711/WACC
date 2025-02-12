package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/while/"

  it should "pass falsErr.wacc" in {
    frontendStatus(dir + "falsErr.wacc") shouldBe 200
  }

  it should "pass truErr.wacc" in {
    frontendStatus(dir + "truErr.wacc") shouldBe 200
  }

  it should "pass whileIntCondition.wacc" in {
    frontendStatus(dir + "whileIntCondition.wacc") shouldBe 200
  }

}
