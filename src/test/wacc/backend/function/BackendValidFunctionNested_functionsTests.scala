package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidFunctionNested_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/nested_functions/"

  it should "execute fibonacciFullRec.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "fibonacciFullRec.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute fibonacciRecursive.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "fibonacciRecursive.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute fixedPointRealArithmetic.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "fixedPointRealArithmetic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionConditionalReturn.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionConditionalReturn.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute mutualRecursion.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "mutualRecursion.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute printInputTriangle.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "printInputTriangle.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute printTriangle.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "printTriangle.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute simpleRecursion.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "simpleRecursion.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
