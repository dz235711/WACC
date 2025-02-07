package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "pass argScopeCanBeShadowed.wacc" in {
    runFrontend(Array(dir + "argScopeCanBeShadowed.wacc"))._1 shouldBe 0
  }

  it should "pass asciiTable.wacc" in {
    runFrontend(Array(dir + "asciiTable.wacc"))._1 shouldBe 0
  }

  it should "pass functionDeclaration.wacc" in {
    runFrontend(Array(dir + "functionDeclaration.wacc"))._1 shouldBe 0
  }

  it should "pass functionDoubleReturn.wacc" in {
    runFrontend(Array(dir + "functionDoubleReturn.wacc"))._1 shouldBe 0
  }

  it should "pass functionIfReturns.wacc" in {
    runFrontend(Array(dir + "functionIfReturns.wacc"))._1 shouldBe 0
  }

  it should "pass functionManyArguments.wacc" in {
    runFrontend(Array(dir + "functionManyArguments.wacc"))._1 shouldBe 0
  }

  it should "pass functionMultiReturns.wacc" in {
    runFrontend(Array(dir + "functionMultiReturns.wacc"))._1 shouldBe 0
  }

  it should "pass functionReturnPair.wacc" in {
    runFrontend(Array(dir + "functionReturnPair.wacc"))._1 shouldBe 0
  }

  it should "pass functionSimple.wacc" in {
    runFrontend(Array(dir + "functionSimple.wacc"))._1 shouldBe 0
  }

  it should "pass functionSimpleLoop.wacc" in {
    runFrontend(Array(dir + "functionSimpleLoop.wacc"))._1 shouldBe 0
  }

  it should "pass functionUpdateParameter.wacc" in {
    runFrontend(Array(dir + "functionUpdateParameter.wacc"))._1 shouldBe 0
  }

  it should "pass incFunction.wacc" in {
    runFrontend(Array(dir + "incFunction.wacc"))._1 shouldBe 0
  }

  it should "pass lotsOfLocals.wacc" in {
    runFrontend(Array(dir + "lotsOfLocals.wacc"))._1 shouldBe 0
  }

  it should "pass manyArgumentsChar.wacc" in {
    runFrontend(Array(dir + "manyArgumentsChar.wacc"))._1 shouldBe 0
  }

  it should "pass manyArgumentsInt.wacc" in {
    runFrontend(Array(dir + "manyArgumentsInt.wacc"))._1 shouldBe 0
  }

  it should "pass negFunction.wacc" in {
    runFrontend(Array(dir + "negFunction.wacc"))._1 shouldBe 0
  }

  it should "pass punning.wacc" in {
    runFrontend(Array(dir + "punning.wacc"))._1 shouldBe 0
  }

  it should "pass sameArgName.wacc" in {
    runFrontend(Array(dir + "sameArgName.wacc"))._1 shouldBe 0
  }

  it should "pass sameArgName2.wacc" in {
    runFrontend(Array(dir + "sameArgName2.wacc"))._1 shouldBe 0
  }

  it should "pass sameNameAsVar.wacc" in {
    runFrontend(Array(dir + "sameNameAsVar.wacc"))._1 shouldBe 0
  }

  it should "pass usesArgumentWhilstMakingArgument.wacc" in {
    runFrontend(Array(dir + "usesArgumentWhilstMakingArgument.wacc"))._1 shouldBe 0
  }

}
