package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "execute andExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "andExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute andOverOrExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "andOverOrExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute boolCalc.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "boolCalc.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute boolExpr1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "boolExpr1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute charComparisonExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "charComparisonExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute divExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "divExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute equalsExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "equalsExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute equalsOverAnd.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "equalsOverAnd.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute equalsOverBool.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "equalsOverBool.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute equalsOverOr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "equalsOverOr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute greaterEqExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "greaterEqExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute greaterExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "greaterExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intCalc.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intCalc.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute intExpr1.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "intExpr1.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute lessCharExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "lessCharExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute lessEqExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "lessEqExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute lessExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "lessExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longExpr2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longExpr2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longExpr3.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longExpr3.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longSplitExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longSplitExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute longSplitExpr2.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "longSplitExpr2.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute minusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "minusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute minusMinusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "minusMinusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute minusNoWhitespaceExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "minusNoWhitespaceExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute minusPlusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "minusPlusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute modExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "modExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute multExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "multExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute multNoWhitespaceExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "multNoWhitespaceExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negBothDiv.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negBothDiv.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negBothMod.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negBothMod.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negDividendDiv.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negDividendDiv.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negDividendMod.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negDividendMod.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negDivisorDiv.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negDivisorDiv.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negDivisorMod.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negDivisorMod.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute negExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "negExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute notExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "notExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute notequalsExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "notequalsExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute orExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "orExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute ordAndchrExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "ordAndchrExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute plusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "plusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute plusMinusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "plusMinusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute plusNoWhitespaceExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "plusNoWhitespaceExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute plusPlusExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "plusPlusExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute sequentialCount.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "sequentialCount.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

  it should "execute stringEqualsExpr.wacc" taggedAs Backend in pending /*{
    val programTester = new ProgramTester(dir + "stringEqualsExpr.wacc")
    val (exitStatus, output) = programTester.run(programTester.testInput)

    exitStatus shouldBe programTester.expectedExitStatus
    output shouldBe programTester.expectedOutput
  }*/

}
