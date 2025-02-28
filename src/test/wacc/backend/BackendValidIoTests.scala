package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/"

  it should "execute IOLoop.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "IOLoop.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute IOSequence.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "IOSequence.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
