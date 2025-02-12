package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/function/"

  it should "frontend analyse badlyNamed.wacc" in {
    frontendStatus(dir + "badlyNamed.wacc") shouldBe 100
  }

  it should "frontend analyse badlyPlaced.wacc" in {
    frontendStatus(dir + "badlyPlaced.wacc") shouldBe 100
  }

  it should "frontend analyse funcExpr.wacc" in {
    frontendStatus(dir + "funcExpr.wacc") shouldBe 100
  }

  it should "frontend analyse funcExpr2.wacc" in {
    frontendStatus(dir + "funcExpr2.wacc") shouldBe 100
  }

  it should "frontend analyse functionConditionalNoReturn.wacc" in {
    frontendStatus(dir + "functionConditionalNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionEndingNotReturn.wacc" in {
    frontendStatus(dir + "functionEndingNotReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionLateDefine.wacc" in {
    frontendStatus(dir + "functionLateDefine.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingCall.wacc" in {
    frontendStatus(dir + "functionMissingCall.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingPType.wacc" in {
    frontendStatus(dir + "functionMissingPType.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingParam.wacc" in {
    frontendStatus(dir + "functionMissingParam.wacc") shouldBe 100
  }

  it should "frontend analyse functionMissingType.wacc" in {
    frontendStatus(dir + "functionMissingType.wacc") shouldBe 100
  }

  it should "frontend analyse functionNoReturn.wacc" in {
    frontendStatus(dir + "functionNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse functionReturnInLoop.wacc" in {
    frontendStatus(dir + "functionReturnInLoop.wacc") shouldBe 100
  }

  it should "frontend analyse functionScopeDef.wacc" in {
    frontendStatus(dir + "functionScopeDef.wacc") shouldBe 100
  }

  it should "frontend analyse mutualRecursionNoReturn.wacc" in {
    frontendStatus(dir + "mutualRecursionNoReturn.wacc") shouldBe 100
  }

  it should "frontend analyse noBodyAfterFuncs.wacc" in {
    frontendStatus(dir + "noBodyAfterFuncs.wacc") shouldBe 100
  }

  it should "frontend analyse thisIsNotC.wacc" in {
    frontendStatus(dir + "thisIsNotC.wacc") shouldBe 100
  }

}
