package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/function/"

  it should "frontend analyse callUndefFunction.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "callUndefFunction.wacc") shouldBe 200
  }

  it should "frontend analyse doubleArgDef.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "doubleArgDef.wacc") shouldBe 200
  }

  it should "frontend analyse funcVarAccess.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "funcVarAccess.wacc") shouldBe 200
  }

  it should "frontend analyse functionAssign.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionAssign.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadArgUse.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionBadArgUse.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadCall.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionBadCall.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadParam.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionBadParam.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadReturn.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionBadReturn.wacc") shouldBe 200
  }

  it should "frontend analyse functionOverArgs.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionOverArgs.wacc") shouldBe 200
  }

  it should "frontend analyse functionRedefine.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionRedefine.wacc") shouldBe 200
  }

  it should "frontend analyse functionSwapArgs.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionSwapArgs.wacc") shouldBe 200
  }

  it should "frontend analyse functionUnderArgs.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionUnderArgs.wacc") shouldBe 200
  }

  it should "frontend analyse invalidReturnsBranched.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "invalidReturnsBranched.wacc") shouldBe 200
  }

  it should "frontend analyse mismatchingReturns.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "mismatchingReturns.wacc") shouldBe 200
  }

}
