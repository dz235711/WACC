package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/while/"

  it should "frontend analyse falsErr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "falsErr.wacc") shouldBe 200
  }

  it should "frontend analyse truErr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "truErr.wacc") shouldBe 200
  }

  it should "frontend analyse whileIntCondition.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "whileIntCondition.wacc") shouldBe 200
  }

}
