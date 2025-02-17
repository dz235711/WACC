package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/function/"

  it should "frontend analyse badlyNamed.wacc" taggedAs Frontend in {
    frontendStatus(dir + "badlyNamed.wacc") shouldBe 100
  }

  it should "frontend analyse badlyPlaced.wacc" taggedAs Frontend in {
    frontendStatus(dir + "badlyPlaced.wacc") shouldBe 100
  }

  it should "frontend analyse funcExpr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "funcExpr.wacc") shouldBe 100
  }

  it should "frontend analyse funcExpr2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "funcExpr2.wacc") shouldBe 100
  }

  it should "frontend analyse functionConditionalNoReturn.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionConditionalNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionEndingNotReturn.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionEndingNotReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionLateDefine.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionLateDefine.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingCall.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionMissingCall.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingPType.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionMissingPType.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingParam.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionMissingParam.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingType.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionMissingType.wacc") shouldBe 100
  }

  it should "frontend analyse functionNoReturn.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionReturnInLoop.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionReturnInLoop.wacc") shouldBe 100
  }

  it should "frontend analyse functionScopeDef.wacc" taggedAs Frontend in {
    frontendStatus(dir + "functionScopeDef.wacc") shouldBe 100
  }

  it should "frontend analyse mutualRecursionNoReturn.wacc" taggedAs Frontend in {
    frontendStatus(dir + "mutualRecursionNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse noBodyAfterFuncs.wacc" taggedAs Frontend in {
    frontendStatus(dir + "noBodyAfterFuncs.wacc") shouldBe 100
  }

  it should "frontend analyse thisIsNotC.wacc" taggedAs Frontend in {
    frontendStatus(dir + "thisIsNotC.wacc") shouldBe 100
  }

}
