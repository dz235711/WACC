package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrBadcharTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/badChar/"

  it should "execute negativeChr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negativeChr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute tooBigChr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "tooBigChr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
