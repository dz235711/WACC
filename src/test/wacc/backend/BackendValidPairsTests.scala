package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "execute checkRefPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "checkRefPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute createPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "createPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute createPair02.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "createPair02.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute createPair03.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "createPair03.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute createRefPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "createRefPair.wacc")
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

  it should "execute linkedList.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "linkedList.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute nestedPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "nestedPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute nestedPairLeftAssign.wacc" taggedAs Debug in pending /* {
    val programTester = new ProgramTester(dir + "nestedPairLeftAssign.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  } */

  it should "execute nestedPairRightExtract.wacc" taggedAs Debug in pending /*{
    val programTester = new ProgramTester(dir + "nestedPairRightExtract.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  } */

  it should "execute null.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "null.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute pairExchangeArrayOk.wacc" taggedAs Debug in {
    val programTester = new ProgramTester(dir + "pairExchangeArrayOk.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute pairarray.wacc" taggedAs Debug in pending /* {
    val programTester = new ProgramTester(dir + "pairarray.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  } */

  it should "execute printNull.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printNull.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printNullPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printNullPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute printPairOfNulls.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "printPairOfNulls.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute readPair.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "readPair.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute writeFst.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "writeFst.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

  it should "execute writeSnd.wacc" taggedAs Backend in {
    val programTester = new ProgramTester(dir + "writeSnd.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus.orElse(exitStatus)
    output shouldBe programTester.expectedOutput
  }

}
