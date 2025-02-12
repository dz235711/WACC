package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "pass fibonacciFullRec.wacc" in {
    frontendStatus(dir + "fibonacciFullRec.wacc") shouldBe 0
  }

  it should "pass fibonacciRecursive.wacc" in {
    frontendStatus(dir + "fibonacciRecursive.wacc") shouldBe 0
  }

  it should "pass fixedPointRealArithmetic.wacc" in {
    frontendStatus(dir + "fixedPointRealArithmetic.wacc") shouldBe 0
  }

  it should "pass functionConditionalReturn.wacc" in {
    frontendStatus(dir + "functionConditionalReturn.wacc") shouldBe 0
  }

  it should "pass mutualRecursion.wacc" in {
    frontendStatus(dir + "mutualRecursion.wacc") shouldBe 0
  }

  it should "pass printInputTriangle.wacc" in {
    frontendStatus(dir + "printInputTriangle.wacc") shouldBe 0
  }

  it should "pass printTriangle.wacc" in {
    frontendStatus(dir + "printTriangle.wacc") shouldBe 0
  }

  it should "pass simpleRecursion.wacc" in {
    frontendStatus(dir + "simpleRecursion.wacc") shouldBe 0
  }

}
