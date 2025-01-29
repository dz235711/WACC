package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/function/"

  it should "pass badlyNamed.wacc" in {
    runFrontend(Array(dir+"badlyNamed.wacc"))._1 shouldBe 100
  }

  it should "pass badlyPlaced.wacc" in {
    runFrontend(Array(dir+"badlyPlaced.wacc"))._1 shouldBe 100
  }

  it should "pass funcExpr.wacc" in {
    runFrontend(Array(dir+"funcExpr.wacc"))._1 shouldBe 100
  }

  it should "pass funcExpr2.wacc" in {
    runFrontend(Array(dir+"funcExpr2.wacc"))._1 shouldBe 100
  }

  it should "pass functionConditionalNoReturn.wacc" in pending /*{
    runFrontend(Array(dir+"functionConditionalNoReturn.wacc"))._1 shouldBe 100
  }*/

  it should "pass functionEndingNotReturn.wacc" in pending /*{
    runFrontend(Array(dir+"functionEndingNotReturn.wacc"))._1 shouldBe 100
  }*/

  it should "pass functionLateDefine.wacc" in {
    runFrontend(Array(dir+"functionLateDefine.wacc"))._1 shouldBe 100
  }

  it should "pass functionMissingCall.wacc" in {
    runFrontend(Array(dir+"functionMissingCall.wacc"))._1 shouldBe 100
  }

  it should "pass functionMissingPType.wacc" in {
    runFrontend(Array(dir+"functionMissingPType.wacc"))._1 shouldBe 100
  }

  it should "pass functionMissingParam.wacc" in {
    runFrontend(Array(dir+"functionMissingParam.wacc"))._1 shouldBe 100
  }

  it should "pass functionMissingType.wacc" in {
    runFrontend(Array(dir+"functionMissingType.wacc"))._1 shouldBe 100
  }

  it should "pass functionNoReturn.wacc" in pending /*{
    runFrontend(Array(dir+"functionNoReturn.wacc"))._1 shouldBe 100
  }*/

  it should "pass functionReturnInLoop.wacc" in pending /*{
    runFrontend(Array(dir+"functionReturnInLoop.wacc"))._1 shouldBe 100
  }*/

  it should "pass functionScopeDef.wacc" in {
    runFrontend(Array(dir+"functionScopeDef.wacc"))._1 shouldBe 100
  }

  it should "pass mutualRecursionNoReturn.wacc" in pending /*{
    runFrontend(Array(dir+"mutualRecursionNoReturn.wacc"))._1 shouldBe 100
  }*/

  it should "pass noBodyAfterFuncs.wacc" in {
    runFrontend(Array(dir+"noBodyAfterFuncs.wacc"))._1 shouldBe 100
  }

  it should "pass thisIsNotC.wacc" in {
    runFrontend(Array(dir+"thisIsNotC.wacc"))._1 shouldBe 100
  }

}