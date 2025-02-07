package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/function/"

  it should "pass callUndefFunction.wacc" in pending /* {
    runFrontend(Array(dir+"callUndefFunction.wacc"))._1 shouldBe 200
  } */

  it should "pass doubleArgDef.wacc" in {
    runFrontend(Array(dir+"doubleArgDef.wacc"))._1 shouldBe 200
  }

  it should "pass funcVarAccess.wacc" in {
    runFrontend(Array(dir+"funcVarAccess.wacc"))._1 shouldBe 200
  }

  it should "pass functionAssign.wacc" in {
    runFrontend(Array(dir+"functionAssign.wacc"))._1 shouldBe 200
  }

  it should "pass functionBadArgUse.wacc" in {
    runFrontend(Array(dir+"functionBadArgUse.wacc"))._1 shouldBe 200
  }

  it should "pass functionBadCall.wacc" in {
    runFrontend(Array(dir+"functionBadCall.wacc"))._1 shouldBe 200
  }

  it should "pass functionBadParam.wacc" in {
    runFrontend(Array(dir+"functionBadParam.wacc"))._1 shouldBe 200
  }

  it should "pass functionBadReturn.wacc" in {
    runFrontend(Array(dir+"functionBadReturn.wacc"))._1 shouldBe 200
  }

  it should "pass functionOverArgs.wacc" in {
    runFrontend(Array(dir+"functionOverArgs.wacc"))._1 shouldBe 200
  }

  it should "pass functionRedefine.wacc" in {
    runFrontend(Array(dir+"functionRedefine.wacc"))._1 shouldBe 200
  }

  it should "pass functionSwapArgs.wacc" in {
    runFrontend(Array(dir+"functionSwapArgs.wacc"))._1 shouldBe 200
  }

  it should "pass functionUnderArgs.wacc" in {
    runFrontend(Array(dir+"functionUnderArgs.wacc"))._1 shouldBe 200
  }

  it should "pass invalidReturnsBranched.wacc" in {
    runFrontend(Array(dir+"invalidReturnsBranched.wacc"))._1 shouldBe 200
  }

  it should "pass mismatchingReturns.wacc" in {
    runFrontend(Array(dir+"mismatchingReturns.wacc"))._1 shouldBe 200
  }

}