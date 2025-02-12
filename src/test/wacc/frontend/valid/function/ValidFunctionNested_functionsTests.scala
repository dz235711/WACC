package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "frontend analyse fibonacciFullRec.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "fibonacciFullRec.wacc") shouldBe 0
  }

  it should "frontend analyse fibonacciRecursive.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "fibonacciRecursive.wacc") shouldBe 0
  }

  it should "frontend analyse fixedPointRealArithmetic.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "fixedPointRealArithmetic.wacc") shouldBe 0
  }

  it should "frontend analyse functionConditionalReturn.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "functionConditionalReturn.wacc") shouldBe 0
  }

  it should "frontend analyse mutualRecursion.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "mutualRecursion.wacc") shouldBe 0
  }

  it should "frontend analyse printInputTriangle.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printInputTriangle.wacc") shouldBe 0
  }

  it should "frontend analyse printTriangle.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printTriangle.wacc") shouldBe 0
  }

  it should "frontend analyse simpleRecursion.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "simpleRecursion.wacc") shouldBe 0
  }

}
