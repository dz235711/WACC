package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/print/"

  it should "execute hashInProgram.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "hashInProgram.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute multipleStringsAssignment.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "multipleStringsAssignment.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  } */

  it should "execute print-backspace.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "print-backspace.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute print.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "print.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printBool.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printBool.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printChar.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printCharArray.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printCharArray.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printCharAsString.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printCharAsString.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printEscChar.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printEscChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printInt.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute println.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "println.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

}
