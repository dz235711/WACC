package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ExceptionBackendTests extends AnyFlatSpec {
  val dir = "src/test/examples/custom/exceptions/"

  it should "execute catchAdvanced.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "catchAdvanced.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute catchExceptionCode.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "catchExceptionCode.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute catchFunctionException.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "catchFunctionException.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute catchRecursive.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "catchRecursive.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exceptionBasic.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exceptionBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exceptionCatch.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exceptionCatch.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exceptionCodeShadowing.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exceptionCodeShadowing.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exceptionIsNoOp.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exceptionIsNoOp.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute exceptionNoThrow.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "exceptionNoThrow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute finallyAlwaysRuns.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "finallyAlwaysRuns.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute nestedTry.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "nestedTry.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute prioritiseFinallyReturn.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "prioritiseFinallyReturn.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute uncaughtThrow.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "uncaughtThrow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute catchBadChar.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "catchBadChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute catchDivideByZero.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "catchDivideByZero.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute catchNullPairErr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "catchNullPairErr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute catchOutOfBounds.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "catchOutOfBounds.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute catchOverflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "catchOverflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/
}
