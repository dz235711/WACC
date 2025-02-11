package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrFunctionTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/function/"

  it should "pass badlyNamed.wacc" in {
    frontendStatus(dir + "badlyNamed.wacc") shouldBe 100
  }

  it should "pass badlyPlaced.wacc" in {
    frontendStatus(dir + "badlyPlaced.wacc") shouldBe 100
  }

  it should "pass funcExpr.wacc" in {
    frontendStatus(dir + "funcExpr.wacc") shouldBe 100
  }

  it should "pass funcExpr2.wacc" in {
    frontendStatus(dir + "funcExpr2.wacc") shouldBe 100
  }

  it should "pass functionConditionalNoReturn.wacc" in {
    frontendStatus(dir + "functionConditionalNoReturn.wacc") shouldBe 100
  }

  it should "pass functionEndingNotReturn.wacc" in {
    frontendStatus(dir + "functionEndingNotReturn.wacc") shouldBe 100
  }

  it should "pass functionLateDefine.wacc" in {
    frontendStatus(dir + "functionLateDefine.wacc") shouldBe 100
  }

  it should "pass functionMissingCall.wacc" in {
    frontendStatus(dir + "functionMissingCall.wacc") shouldBe 100
  }

  it should "pass functionMissingPType.wacc" in {
    frontendStatus(dir + "functionMissingPType.wacc") shouldBe 100
  }

  it should "pass functionMissingParam.wacc" in {
    frontendStatus(dir + "functionMissingParam.wacc") shouldBe 100
  }

  it should "pass functionMissingType.wacc" in {
    frontendStatus(dir + "functionMissingType.wacc") shouldBe 100
  }

  it should "pass functionNoReturn.wacc" in {
    frontendStatus(dir + "functionNoReturn.wacc") shouldBe 100
  }

  it should "pass functionReturnInLoop.wacc" in {
    frontendStatus(dir + "functionReturnInLoop.wacc") shouldBe 100
  }

  it should "pass functionScopeDef.wacc" in {
    frontendStatus(dir + "functionScopeDef.wacc") shouldBe 100
  }

  it should "pass mutualRecursionNoReturn.wacc" in {
    frontendStatus(dir + "mutualRecursionNoReturn.wacc") shouldBe 100
  }

  it should "pass noBodyAfterFuncs.wacc" in {
    frontendStatus(dir + "noBodyAfterFuncs.wacc") shouldBe 100
  }

  it should "pass thisIsNotC.wacc" in {
    frontendStatus(dir + "thisIsNotC.wacc") shouldBe 100
  }

}
