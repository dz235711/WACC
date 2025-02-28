package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "execute freeNull.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "freeNull.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute readNull1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "readNull1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute readNull2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "readNull2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute setNull1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "setNull1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute setNull2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "setNull2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute useNull1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "useNull1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute useNull2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "useNull2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
