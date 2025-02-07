package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "pass fibonacciFullRec.wacc" in {
    runFrontend(Array(dir + "fibonacciFullRec.wacc"))._1 shouldBe 0
  }

  it should "pass fibonacciRecursive.wacc" in {
    runFrontend(Array(dir + "fibonacciRecursive.wacc"))._1 shouldBe 0
  }

  it should "pass fixedPointRealArithmetic.wacc" in {
    runFrontend(Array(dir + "fixedPointRealArithmetic.wacc"))._1 shouldBe 0
  }

  it should "pass functionConditionalReturn.wacc" in {
    runFrontend(Array(dir + "functionConditionalReturn.wacc"))._1 shouldBe 0
  }

  it should "pass mutualRecursion.wacc" in {
    runFrontend(Array(dir + "mutualRecursion.wacc"))._1 shouldBe 0
  }

  it should "pass printInputTriangle.wacc" in {
    runFrontend(Array(dir + "printInputTriangle.wacc"))._1 shouldBe 0
  }

  it should "pass printTriangle.wacc" in {
    runFrontend(Array(dir + "printTriangle.wacc"))._1 shouldBe 0
  }

  it should "pass simpleRecursion.wacc" in {
    runFrontend(Array(dir + "simpleRecursion.wacc"))._1 shouldBe 0
  }

}
