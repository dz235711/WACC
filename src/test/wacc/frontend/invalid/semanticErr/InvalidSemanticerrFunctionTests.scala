package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/function/"

  it should "frontend analyse callUndefFunction.wacc" in {
    frontendStatus(dir + "callUndefFunction.wacc") shouldBe 200
  }

  it should "frontend analyse doubleArgDef.wacc" in {
    frontendStatus(dir + "doubleArgDef.wacc") shouldBe 200
  }

  it should "frontend analyse funcVarAccess.wacc" in {
    frontendStatus(dir + "funcVarAccess.wacc") shouldBe 200
  }

  it should "frontend analyse functionAssign.wacc" in {
    frontendStatus(dir + "functionAssign.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadArgUse.wacc" in {
    frontendStatus(dir + "functionBadArgUse.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadCall.wacc" in {
    frontendStatus(dir + "functionBadCall.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadParam.wacc" in {
    frontendStatus(dir + "functionBadParam.wacc") shouldBe 200
  }

  it should "frontend analyse functionBadReturn.wacc" in {
    frontendStatus(dir + "functionBadReturn.wacc") shouldBe 200
  }

  it should "frontend analyse functionOverArgs.wacc" in {
    frontendStatus(dir + "functionOverArgs.wacc") shouldBe 200
  }

  it should "frontend analyse functionRedefine.wacc" in {
    frontendStatus(dir + "functionRedefine.wacc") shouldBe 200
  }

  it should "frontend analyse functionSwapArgs.wacc" in {
    frontendStatus(dir + "functionSwapArgs.wacc") shouldBe 200
  }

  it should "frontend analyse functionUnderArgs.wacc" in {
    frontendStatus(dir + "functionUnderArgs.wacc") shouldBe 200
  }

  it should "frontend analyse invalidReturnsBranched.wacc" in {
    frontendStatus(dir + "invalidReturnsBranched.wacc") shouldBe 200
  }

  it should "frontend analyse mismatchingReturns.wacc" in {
    frontendStatus(dir + "mismatchingReturns.wacc") shouldBe 200
  }

}
