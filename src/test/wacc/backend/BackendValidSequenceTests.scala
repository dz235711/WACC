package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/sequence/"

  it should "execute basicSeq.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "basicSeq.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute basicSeq2.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "basicSeq2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute boolAssignment.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "boolAssignment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute charAssignment.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "charAssignment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exitSimple.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "exitSimple.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intAssignment.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intAssignment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intLeadingZeros.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intLeadingZeros.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute stringAssignment.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "stringAssignment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

}
