package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "execute _VarNames.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "_VarNames.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute boolDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "boolDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute boolDeclaration2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "boolDeclaration2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute capCharDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "capCharDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute charDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "charDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute charDeclaration2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "charDeclaration2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute emptyStringDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "emptyStringDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longVarNames.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longVarNames.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute manyVariables.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "manyVariables.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negIntDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negIntDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute puncCharDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "puncCharDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute stringCarriageReturn.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "stringCarriageReturn.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute stringDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "stringDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute zeroIntDeclaration.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "zeroIntDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
