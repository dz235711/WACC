package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrArrayoutofboundsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/arrayOutOfBounds/"

  it should "execute arrayNegBounds.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "arrayNegBounds.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute arrayOutOfBounds.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "arrayOutOfBounds.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute arrayOutOfBoundsWrite.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "arrayOutOfBoundsWrite.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
