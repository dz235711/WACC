package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "pass argScopeCanBeShadowed.wacc" in {
    frontendStatus(dir + "argScopeCanBeShadowed.wacc") shouldBe 0
  }

  it should "pass asciiTable.wacc" in {
    frontendStatus(dir + "asciiTable.wacc") shouldBe 0
  }

  it should "pass functionDeclaration.wacc" in {
    frontendStatus(dir + "functionDeclaration.wacc") shouldBe 0
  }

  it should "pass functionDoubleReturn.wacc" in {
    frontendStatus(dir + "functionDoubleReturn.wacc") shouldBe 0
  }

  it should "pass functionIfReturns.wacc" in {
    frontendStatus(dir + "functionIfReturns.wacc") shouldBe 0
  }

  it should "pass functionManyArguments.wacc" in {
    frontendStatus(dir + "functionManyArguments.wacc") shouldBe 0
  }

  it should "pass functionMultiReturns.wacc" in {
    frontendStatus(dir + "functionMultiReturns.wacc") shouldBe 0
  }

  it should "pass functionReturnPair.wacc" in {
    frontendStatus(dir + "functionReturnPair.wacc") shouldBe 0
  }

  it should "pass functionSimple.wacc" in {
    frontendStatus(dir + "functionSimple.wacc") shouldBe 0
  }

  it should "pass functionSimpleLoop.wacc" in {
    frontendStatus(dir + "functionSimpleLoop.wacc") shouldBe 0
  }

  it should "pass functionUpdateParameter.wacc" in {
    frontendStatus(dir + "functionUpdateParameter.wacc") shouldBe 0
  }

  it should "pass incFunction.wacc" in {
    frontendStatus(dir + "incFunction.wacc") shouldBe 0
  }

  it should "pass lotsOfLocals.wacc" in {
    frontendStatus(dir + "lotsOfLocals.wacc") shouldBe 0
  }

  it should "pass manyArgumentsChar.wacc" in {
    frontendStatus(dir + "manyArgumentsChar.wacc") shouldBe 0
  }

  it should "pass manyArgumentsInt.wacc" in {
    frontendStatus(dir + "manyArgumentsInt.wacc") shouldBe 0
  }

  it should "pass negFunction.wacc" in {
    frontendStatus(dir + "negFunction.wacc") shouldBe 0
  }

  it should "pass punning.wacc" in {
    frontendStatus(dir + "punning.wacc") shouldBe 0
  }

  it should "pass sameArgName.wacc" in {
    frontendStatus(dir + "sameArgName.wacc") shouldBe 0
  }

  it should "pass sameArgName2.wacc" in {
    frontendStatus(dir + "sameArgName2.wacc") shouldBe 0
  }

  it should "pass sameNameAsVar.wacc" in {
    frontendStatus(dir + "sameNameAsVar.wacc") shouldBe 0
  }

  it should "pass usesArgumentWhilstMakingArgument.wacc" in {
    frontendStatus(dir + "usesArgumentWhilstMakingArgument.wacc") shouldBe 0
  }

}
