package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "execute if1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute if2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute if3.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if3.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute if4.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if4.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute if5.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if5.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute if6.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "if6.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute ifBasic.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "ifBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute ifFalse.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "ifFalse.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute ifTrue.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "ifTrue.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute whitespace.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "whitespace.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
