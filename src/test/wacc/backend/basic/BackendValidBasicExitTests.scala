package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidBasicExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/exit/"

  it should "execute exit-1.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exit-1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exitBasic.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exitBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exitBasic2.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exitBasic2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exitWrap.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exitWrap.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

}
