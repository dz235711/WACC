package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "frontend analyse argScopeCanBeShadowed.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "argScopeCanBeShadowed.wacc") shouldBe 0
  }

  it should "frontend analyse asciiTable.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "asciiTable.wacc") shouldBe 0
  }

  it should "frontend analyse functionDeclaration.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse functionDoubleReturn.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionDoubleReturn.wacc") shouldBe 0
  }

  it should "frontend analyse functionIfReturns.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionIfReturns.wacc") shouldBe 0
  }

  it should "frontend analyse functionManyArguments.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionManyArguments.wacc") shouldBe 0
  }

  it should "frontend analyse functionMultiReturns.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionMultiReturns.wacc") shouldBe 0
  }

  it should "frontend analyse functionReturnPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionReturnPair.wacc") shouldBe 0
  }

  it should "frontend analyse functionSimple.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionSimple.wacc") shouldBe 0
  }

  it should "frontend analyse functionSimpleLoop.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionSimpleLoop.wacc") shouldBe 0
  }

  it should "frontend analyse functionUpdateParameter.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionUpdateParameter.wacc") shouldBe 0
  }

  it should "frontend analyse incFunction.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "incFunction.wacc") shouldBe 0
  }

  it should "frontend analyse lotsOfLocals.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "lotsOfLocals.wacc") shouldBe 0
  }

  it should "frontend analyse manyArgumentsChar.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "manyArgumentsChar.wacc") shouldBe 0
  }

  it should "frontend analyse manyArgumentsInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "manyArgumentsInt.wacc") shouldBe 0
  }

  it should "frontend analyse negFunction.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negFunction.wacc") shouldBe 0
  }

  it should "frontend analyse punning.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "punning.wacc") shouldBe 0
  }

  it should "frontend analyse sameArgName.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "sameArgName.wacc") shouldBe 0
  }

  it should "frontend analyse sameArgName2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "sameArgName2.wacc") shouldBe 0
  }

  it should "frontend analyse sameNameAsVar.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "sameNameAsVar.wacc") shouldBe 0
  }

  it should "frontend analyse usesArgumentWhilstMakingArgument.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "usesArgumentWhilstMakingArgument.wacc") shouldBe 0
  }

}
