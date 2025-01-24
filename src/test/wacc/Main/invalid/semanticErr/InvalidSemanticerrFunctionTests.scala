package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/function/"

  it should "pass callUndefFunction.wacc" in pending /*{
    runFrontend(Array(dir+"callUndefFunction.wacc"))._1 shouldBe 200
  }*/

  it should "pass doubleArgDef.wacc" in pending /*{
    runFrontend(Array(dir+"doubleArgDef.wacc"))._1 shouldBe 200
  }*/

  it should "pass funcVarAccess.wacc" in pending /*{
    runFrontend(Array(dir+"funcVarAccess.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionAssign.wacc" in pending /*{
    runFrontend(Array(dir+"functionAssign.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionBadArgUse.wacc" in pending /*{
    runFrontend(Array(dir+"functionBadArgUse.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionBadCall.wacc" in pending /*{
    runFrontend(Array(dir+"functionBadCall.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionBadParam.wacc" in pending /*{
    runFrontend(Array(dir+"functionBadParam.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionBadReturn.wacc" in pending /*{
    runFrontend(Array(dir+"functionBadReturn.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionOverArgs.wacc" in pending /*{
    runFrontend(Array(dir+"functionOverArgs.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionRedefine.wacc" in pending /*{
    runFrontend(Array(dir+"functionRedefine.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionSwapArgs.wacc" in pending /*{
    runFrontend(Array(dir+"functionSwapArgs.wacc"))._1 shouldBe 200
  }*/

  it should "pass functionUnderArgs.wacc" in pending /*{
    runFrontend(Array(dir+"functionUnderArgs.wacc"))._1 shouldBe 200
  }*/

  it should "pass invalidReturnsBranched.wacc" in pending /*{
    runFrontend(Array(dir+"invalidReturnsBranched.wacc"))._1 shouldBe 200
  }*/

  it should "pass mismatchingReturns.wacc" in pending /*{
    runFrontend(Array(dir+"mismatchingReturns.wacc"))._1 shouldBe 200
  }*/

}