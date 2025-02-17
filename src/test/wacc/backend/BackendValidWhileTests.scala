package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/while/"

  it should "execute fibonacciFullIt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "fibonacciFullIt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute fibonacciIterative.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "fibonacciIterative.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute loopCharCondition.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "loopCharCondition.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute loopIntCondition.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "loopIntCondition.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute max.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "max.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute min.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "min.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute rmStyleAdd.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "rmStyleAdd.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute rmStyleAddIO.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "rmStyleAddIO.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute whileBasic.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "whileBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute whileBoolFlip.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "whileBoolFlip.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute whileCount.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "whileCount.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute whileFalse.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "whileFalse.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
