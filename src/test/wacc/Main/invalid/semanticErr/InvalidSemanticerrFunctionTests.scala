package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/function/"

  it should "pass callUndefFunction.wacc" in {
    frontendStatus(dir + "callUndefFunction.wacc") shouldBe 200
  }

  it should "pass doubleArgDef.wacc" in {
    frontendStatus(dir + "doubleArgDef.wacc") shouldBe 200
  }

  it should "pass funcVarAccess.wacc" in {
    frontendStatus(dir + "funcVarAccess.wacc") shouldBe 200
  }

  it should "pass functionAssign.wacc" in {
    frontendStatus(dir + "functionAssign.wacc") shouldBe 200
  }

  it should "pass functionBadArgUse.wacc" in {
    frontendStatus(dir + "functionBadArgUse.wacc") shouldBe 200
  }

  it should "pass functionBadCall.wacc" in {
    frontendStatus(dir + "functionBadCall.wacc") shouldBe 200
  }

  it should "pass functionBadParam.wacc" in {
    frontendStatus(dir + "functionBadParam.wacc") shouldBe 200
  }

  it should "pass functionBadReturn.wacc" in {
    frontendStatus(dir + "functionBadReturn.wacc") shouldBe 200
  }

  it should "pass functionOverArgs.wacc" in {
    frontendStatus(dir + "functionOverArgs.wacc") shouldBe 200
  }

  it should "pass functionRedefine.wacc" in {
    frontendStatus(dir + "functionRedefine.wacc") shouldBe 200
  }

  it should "pass functionSwapArgs.wacc" in {
    frontendStatus(dir + "functionSwapArgs.wacc") shouldBe 200
  }

  it should "pass functionUnderArgs.wacc" in {
    frontendStatus(dir + "functionUnderArgs.wacc") shouldBe 200
  }

  it should "pass invalidReturnsBranched.wacc" in {
    frontendStatus(dir + "invalidReturnsBranched.wacc") shouldBe 200
  }

  it should "pass mismatchingReturns.wacc" in {
    frontendStatus(dir + "mismatchingReturns.wacc") shouldBe 200
  }

}
