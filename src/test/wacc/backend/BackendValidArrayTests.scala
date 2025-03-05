package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "execute array.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "array.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayBasic.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayBasic.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayEmpty.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayEmpty.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayIndexMayBeArrayIndex.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayIndexMayBeArrayIndex.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayLength.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayLength.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayLookup.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayLookup.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayNested.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayNested.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayOnHeap.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayOnHeap.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arrayPrint.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arrayPrint.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute arraySimple.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "arraySimple.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute charArrayInStringArray.wacc" taggedAs Debug in {
    val programTester = new ProgramTester(dir + "charArrayInStringArray.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }
  }

  it should "execute emptyArrayAloneIsFine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "emptyArrayAloneIsFine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute emptyArrayNextLine.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "emptyArrayNextLine.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute emptyArrayPrint.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "emptyArrayPrint.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute emptyArrayReplace.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "emptyArrayReplace.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute emptyArrayScope.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "emptyArrayScope.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute free.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "free.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute lenArrayIndex.wacc" taggedAs Backend in {
  it should "execute lenArrayIndex.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "lenArrayIndex.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }
  }

  it should "execute modifyString.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "modifyString.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printRef.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printRef.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute stringFromArray.wacc" taggedAs Backend in {
  it should "execute stringFromArray.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "stringFromArray.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }
  }

}
