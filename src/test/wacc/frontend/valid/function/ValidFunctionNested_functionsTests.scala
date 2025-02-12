package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "frontend analyse fibonacciFullRec.wacc" in {
    frontendStatus(dir + "fibonacciFullRec.wacc") shouldBe 0
  }

  it should "frontend analyse fibonacciRecursive.wacc" in {
    frontendStatus(dir + "fibonacciRecursive.wacc") shouldBe 0
  }

  it should "frontend analyse fixedPointRealArithmetic.wacc" in {
    frontendStatus(dir + "fixedPointRealArithmetic.wacc") shouldBe 0
  }

  it should "frontend analyse functionConditionalReturn.wacc" in {
    frontendStatus(dir + "functionConditionalReturn.wacc") shouldBe 0
  }

  it should "frontend analyse mutualRecursion.wacc" in {
    frontendStatus(dir + "mutualRecursion.wacc") shouldBe 0
  }

  it should "frontend analyse printInputTriangle.wacc" in {
    frontendStatus(dir + "printInputTriangle.wacc") shouldBe 0
  }

  it should "frontend analyse printTriangle.wacc" in {
    frontendStatus(dir + "printTriangle.wacc") shouldBe 0
  }

  it should "frontend analyse simpleRecursion.wacc" in {
    frontendStatus(dir + "simpleRecursion.wacc") shouldBe 0
  }

}
