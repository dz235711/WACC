package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrIntegeroverflowTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/integerOverflow/"

  it should "execute intJustOverflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intJustOverflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intUnderflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intUnderflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intWayOverflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intWayOverflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intmultOverflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intmultOverflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intnegateOverflow.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intnegateOverflow.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intnegateOverflow2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intnegateOverflow2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intnegateOverflow3.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intnegateOverflow3.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intnegateOverflow4.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intnegateOverflow4.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
