package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "execute comment.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "comment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute commentEoF.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "commentEoF.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute commentInLine.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "commentInLine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute skip.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "skip.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
