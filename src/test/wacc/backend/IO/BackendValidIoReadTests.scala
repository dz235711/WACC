package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/read/"

  it should "execute echoBigInt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoBigInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute echoBigNegInt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoBigNegInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute echoChar.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute echoInt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute echoNegInt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoNegInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute echoPuncChar.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "echoPuncChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute read.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "read.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute readAtEof.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "readAtEof.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.getOrElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
