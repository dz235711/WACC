package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "execute ifNested1.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "ifNested1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute ifNested2.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "ifNested2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute indentationNotImportant.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "indentationNotImportant.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute intsAndKeywords.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "intsAndKeywords.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printAllTypes.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printAllTypes.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scope.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scope.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeBasic.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeIfRedefine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeIfRedefine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeRedefine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeRedefine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeSimpleRedefine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeSimpleRedefine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeVars.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeVars.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeWhileNested.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeWhileNested.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute scopeWhileRedefine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "scopeWhileRedefine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute splitScope.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "splitScope.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

}
