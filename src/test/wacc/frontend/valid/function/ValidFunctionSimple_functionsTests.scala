package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "frontend analyse argScopeCanBeShadowed.wacc" in {
    frontendStatus(dir + "argScopeCanBeShadowed.wacc") shouldBe 0
  }

  it should "frontend analyse asciiTable.wacc" in {
    frontendStatus(dir + "asciiTable.wacc") shouldBe 0
  }

  it should "frontend analyse functionDeclaration.wacc" in {
    frontendStatus(dir + "functionDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse functionDoubleReturn.wacc" in {
    frontendStatus(dir + "functionDoubleReturn.wacc") shouldBe 0
  }

  it should "frontend analyse functionIfReturns.wacc" in {
    frontendStatus(dir + "functionIfReturns.wacc") shouldBe 0
  }

  it should "frontend analyse functionManyArguments.wacc" in {
    frontendStatus(dir + "functionManyArguments.wacc") shouldBe 0
  }

  it should "frontend analyse functionMultiReturns.wacc" in {
    frontendStatus(dir + "functionMultiReturns.wacc") shouldBe 0
  }

  it should "frontend analyse functionReturnPair.wacc" in {
    frontendStatus(dir + "functionReturnPair.wacc") shouldBe 0
  }

  it should "frontend analyse functionSimple.wacc" in {
    frontendStatus(dir + "functionSimple.wacc") shouldBe 0
  }

  it should "frontend analyse functionSimpleLoop.wacc" in {
    frontendStatus(dir + "functionSimpleLoop.wacc") shouldBe 0
  }

  it should "frontend analyse functionUpdateParameter.wacc" in {
    frontendStatus(dir + "functionUpdateParameter.wacc") shouldBe 0
  }

  it should "frontend analyse incFunction.wacc" in {
    frontendStatus(dir + "incFunction.wacc") shouldBe 0
  }

  it should "frontend analyse lotsOfLocals.wacc" in {
    frontendStatus(dir + "lotsOfLocals.wacc") shouldBe 0
  }

  it should "frontend analyse manyArgumentsChar.wacc" in {
    frontendStatus(dir + "manyArgumentsChar.wacc") shouldBe 0
  }

  it should "frontend analyse manyArgumentsInt.wacc" in {
    frontendStatus(dir + "manyArgumentsInt.wacc") shouldBe 0
  }

  it should "frontend analyse negFunction.wacc" in {
    frontendStatus(dir + "negFunction.wacc") shouldBe 0
  }

  it should "frontend analyse punning.wacc" in {
    frontendStatus(dir + "punning.wacc") shouldBe 0
  }

  it should "frontend analyse sameArgName.wacc" in {
    frontendStatus(dir + "sameArgName.wacc") shouldBe 0
  }

  it should "frontend analyse sameArgName2.wacc" in {
    frontendStatus(dir + "sameArgName2.wacc") shouldBe 0
  }

  it should "frontend analyse sameNameAsVar.wacc" in {
    frontendStatus(dir + "sameNameAsVar.wacc") shouldBe 0
  }

  it should "frontend analyse usesArgumentWhilstMakingArgument.wacc" in {
    frontendStatus(dir + "usesArgumentWhilstMakingArgument.wacc") shouldBe 0
  }

}
