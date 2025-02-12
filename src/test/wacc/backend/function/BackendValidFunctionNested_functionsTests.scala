package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "correctly execute fibonacciFullRec.wacc" in pending /*{
    fullExec(dir + fibonacciFullRec.wacc, "") shouldBe Some("This program calculates the nth fibonacci number recursively.\nPlease enter n (should not be too large): The input n is 30\nThe nth fibonacci number is 832040\n")
  }*/

  it should "correctly execute fibonacciRecursive.wacc" in pending /*{
    fullExec(dir + fibonacciRecursive.wacc, "") shouldBe Some("The first 20 fibonacci numbers are:\n0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181...\n")
  }*/

  it should "correctly execute fixedPointRealArithmetic.wacc" in pending /*{
    fullExec(dir + fixedPointRealArithmetic.wacc, "") shouldBe Some("Using fixed-point real: 10 / 3 * 3 = 10\n")
  }*/

  it should "correctly execute functionConditionalReturn.wacc" in pending /*{
    fullExec(dir + functionConditionalReturn.wacc, "") shouldBe Some("true\n")
  }*/

  it should "correctly execute mutualRecursion.wacc" in pending /*{
    fullExec(dir + mutualRecursion.wacc, "") shouldBe Some("r1: sending 8\nr2: received 8\nr1: sending 7\nr2: received 7\nr1: sending 6\nr2: received 6\nr1: sending 5\nr2: received 5\nr1: sending 4\nr2: received 4\nr1: sending 3\nr2: received 3\nr1: sending 2\nr2: received 2\nr1: sending 1\nr2: received 1\n")
  }*/

  it should "correctly execute printInputTriangle.wacc" in pending /*{
    fullExec(dir + printInputTriangle.wacc, "") shouldBe Some("Please enter the size of the triangle to print:\n-------------\n------------\n-----------\n----------\n---------\n--------\n-------\n------\n-----\n----\n---\n--\n-\n")
  }*/

  it should "correctly execute printTriangle.wacc" in pending /*{
    fullExec(dir + printTriangle.wacc, "") shouldBe Some("--------\n-------\n------\n-----\n----\n---\n--\n-\n")
  }*/

  it should "correctly execute simpleRecursion.wacc" in pending /*{
    fullExec(dir + simpleRecursion.wacc, "") shouldBe Some("")
  }*/

}