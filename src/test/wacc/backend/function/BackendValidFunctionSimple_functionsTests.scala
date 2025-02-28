package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "execute argScopeCanBeShadowed.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "argScopeCanBeShadowed.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute asciiTable.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "asciiTable.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionDeclaration.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "functionDeclaration.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute functionDoubleReturn.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionDoubleReturn.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionIfReturns.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionIfReturns.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionManyArguments.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionManyArguments.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionMultiReturns.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionMultiReturns.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionReturnPair.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionReturnPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionSimple.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "functionSimple.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute functionSimpleLoop.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "functionSimpleLoop.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute functionUpdateParameter.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "functionUpdateParameter.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute incFunction.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "incFunction.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute lotsOfLocals.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "lotsOfLocals.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute manyArgumentsChar.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "manyArgumentsChar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute manyArgumentsInt.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "manyArgumentsInt.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negFunction.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negFunction.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute punning.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "punning.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute sameArgName.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "sameArgName.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute sameArgName2.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "sameArgName2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute sameNameAsVar.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "sameNameAsVar.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute usesArgumentWhilstMakingArgument.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "usesArgumentWhilstMakingArgument.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }*/

}
