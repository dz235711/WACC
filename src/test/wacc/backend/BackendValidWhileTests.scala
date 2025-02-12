package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/while/"

  it should "correctly execute fibonacciFullIt.wacc" in pending /*{
    fullExec(dir + fibonacciFullIt.wacc, "") shouldBe Some("This program calculates the nth fibonacci number iteratively.\nPlease enter n (should not be too large): The input n is 30\nThe nth fibonacci number is 832040\n")
  }*/

  it should "correctly execute fibonacciIterative.wacc" in pending /*{
    fullExec(dir + fibonacciIterative.wacc, "") shouldBe Some("The first 20 fibonacci numbers are:\n0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, ...\n")
  }*/

  it should "correctly execute loopCharCondition.wacc" in pending /*{
    fullExec(dir + loopCharCondition.wacc, "") shouldBe Some("Change c\nShould print "Change c" once before.\n")
  }*/

  it should "correctly execute loopIntCondition.wacc" in pending /*{
    fullExec(dir + loopIntCondition.wacc, "") shouldBe Some("Change n\nShould print "Change n" once before.\n")
  }*/

  it should "correctly execute max.wacc" in pending /*{
    fullExec(dir + max.wacc, "") shouldBe Some("max value = 17\n")
  }*/

  it should "correctly execute min.wacc" in pending /*{
    fullExec(dir + min.wacc, "") shouldBe Some("min value = 10\n")
  }*/

  it should "correctly execute rmStyleAdd.wacc" in pending /*{
    fullExec(dir + rmStyleAdd.wacc, "") shouldBe Some("initial value of x: 3\n(+)(+)(+)(+)(+)(+)(+)\nfinal value of x: 10\n")
  }*/

  it should "correctly execute rmStyleAddIO.wacc" in pending /*{
    fullExec(dir + rmStyleAddIO.wacc, "") shouldBe Some("Enter the first number: Enter the second number: Initial value of x: 2\n(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)(+)\nfinal value of x: 42\n")
  }*/

  it should "correctly execute whileBasic.wacc" in pending /*{
    fullExec(dir + whileBasic.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute whileBoolFlip.wacc" in pending /*{
    fullExec(dir + whileBoolFlip.wacc, "") shouldBe Some("flip b!\nend of loop\n")
  }*/

  it should "correctly execute whileCount.wacc" in pending /*{
    fullExec(dir + whileCount.wacc, "") shouldBe Some("Can you count to 10?\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")
  }*/

  it should "correctly execute whileFalse.wacc" in pending /*{
    fullExec(dir + whileFalse.wacc, "") shouldBe Some("end of loop\n")
  }*/

}