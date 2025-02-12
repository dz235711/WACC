package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "correctly execute andExpr.wacc" in pending /*{
    fullExec(dir + andExpr.wacc, "") shouldBe Some("false\ntrue\nfalse\n")
  }*/

  it should "correctly execute andOverOrExpr.wacc" in pending /*{
    fullExec(dir + andOverOrExpr.wacc, "") shouldBe Some("true\nfalse\n")
  }*/

  it should "correctly execute boolCalc.wacc" in pending /*{
    fullExec(dir + boolCalc.wacc, "") shouldBe Some("false\n")
  }*/

  it should "correctly execute boolExpr1.wacc" in pending /*{
    fullExec(dir + boolExpr1.wacc, "") shouldBe Some("Correct\n")
  }*/

  it should "correctly execute charComparisonExpr.wacc" in pending /*{
    fullExec(dir + charComparisonExpr.wacc, "") shouldBe Some("false\ntrue\ntrue\ntrue\nfalse\nfalse\n")
  }*/

  it should "correctly execute divExpr.wacc" in pending /*{
    fullExec(dir + divExpr.wacc, "") shouldBe Some("1\n")
  }*/

  it should "correctly execute equalsExpr.wacc" in pending /*{
    fullExec(dir + equalsExpr.wacc, "") shouldBe Some("false\nfalse\ntrue\n")
  }*/

  it should "correctly execute equalsOverAnd.wacc" in pending /*{
    fullExec(dir + equalsOverAnd.wacc, "") shouldBe Some("false\ntrue\n")
  }*/

  it should "correctly execute equalsOverBool.wacc" in pending /*{
    fullExec(dir + equalsOverBool.wacc, "") shouldBe Some("true\nfalse\n")
  }*/

  it should "correctly execute equalsOverOr.wacc" in pending /*{
    fullExec(dir + equalsOverOr.wacc, "") shouldBe Some("true\nfalse\n")
  }*/

  it should "correctly execute greaterEqExpr.wacc" in pending /*{
    fullExec(dir + greaterEqExpr.wacc, "") shouldBe Some("false\ntrue\ntrue\n")
  }*/

  it should "correctly execute greaterExpr.wacc" in pending /*{
    fullExec(dir + greaterExpr.wacc, "") shouldBe Some("false\ntrue\n")
  }*/

  it should "correctly execute intCalc.wacc" in pending /*{
    fullExec(dir + intCalc.wacc, "") shouldBe Some("72\n")
  }*/

  it should "correctly execute intExpr1.wacc" in pending /*{
    fullExec(dir + intExpr1.wacc, "") shouldBe Some("Correct\n")
  }*/

  it should "correctly execute lessCharExpr.wacc" in pending /*{
    fullExec(dir + lessCharExpr.wacc, "") shouldBe Some("true\nfalse\n")
  }*/

  it should "correctly execute lessEqExpr.wacc" in pending /*{
    fullExec(dir + lessEqExpr.wacc, "") shouldBe Some("true\nfalse\ntrue\n")
  }*/

  it should "correctly execute lessExpr.wacc" in pending /*{
    fullExec(dir + lessExpr.wacc, "") shouldBe Some("true\nfalse\n")
  }*/

  it should "correctly execute longExpr.wacc" in pending /*{
    fullExec(dir + longExpr.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute longExpr2.wacc" in pending /*{
    fullExec(dir + longExpr2.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute longExpr3.wacc" in pending /*{
    fullExec(dir + longExpr3.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute longSplitExpr.wacc" in pending /*{
    fullExec(dir + longSplitExpr.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute longSplitExpr2.wacc" in pending /*{
    fullExec(dir + longSplitExpr2.wacc, "") shouldBe Some("362880\n128\n")
  }*/

  it should "correctly execute minusExpr.wacc" in pending /*{
    fullExec(dir + minusExpr.wacc, "") shouldBe Some("5\n")
  }*/

  it should "correctly execute minusMinusExpr.wacc" in pending /*{
    fullExec(dir + minusMinusExpr.wacc, "") shouldBe Some("3\n")
  }*/

  it should "correctly execute minusNoWhitespaceExpr.wacc" in pending /*{
    fullExec(dir + minusNoWhitespaceExpr.wacc, "") shouldBe Some("-1\n")
  }*/

  it should "correctly execute minusPlusExpr.wacc" in pending /*{
    fullExec(dir + minusPlusExpr.wacc, "") shouldBe Some("-1\n")
  }*/

  it should "correctly execute modExpr.wacc" in pending /*{
    fullExec(dir + modExpr.wacc, "") shouldBe Some("2\n")
  }*/

  it should "correctly execute multExpr.wacc" in pending /*{
    fullExec(dir + multExpr.wacc, "") shouldBe Some("15\n")
  }*/

  it should "correctly execute multNoWhitespaceExpr.wacc" in pending /*{
    fullExec(dir + multNoWhitespaceExpr.wacc, "") shouldBe Some("2\n")
  }*/

  it should "correctly execute negBothDiv.wacc" in pending /*{
    fullExec(dir + negBothDiv.wacc, "") shouldBe Some("2\n")
  }*/

  it should "correctly execute negBothMod.wacc" in pending /*{
    fullExec(dir + negBothMod.wacc, "") shouldBe Some("-2\n")
  }*/

  it should "correctly execute negDividendDiv.wacc" in pending /*{
    fullExec(dir + negDividendDiv.wacc, "") shouldBe Some("-2\n")
  }*/

  it should "correctly execute negDividendMod.wacc" in pending /*{
    fullExec(dir + negDividendMod.wacc, "") shouldBe Some("-2\n")
  }*/

  it should "correctly execute negDivisorDiv.wacc" in pending /*{
    fullExec(dir + negDivisorDiv.wacc, "") shouldBe Some("-2\n")
  }*/

  it should "correctly execute negDivisorMod.wacc" in pending /*{
    fullExec(dir + negDivisorMod.wacc, "") shouldBe Some("2\n")
  }*/

  it should "correctly execute negExpr.wacc" in pending /*{
    fullExec(dir + negExpr.wacc, "") shouldBe Some("-42\n")
  }*/

  it should "correctly execute notExpr.wacc" in pending /*{
    fullExec(dir + notExpr.wacc, "") shouldBe Some("false\ntrue\n")
  }*/

  it should "correctly execute notequalsExpr.wacc" in pending /*{
    fullExec(dir + notequalsExpr.wacc, "") shouldBe Some("true\ntrue\nfalse\n")
  }*/

  it should "correctly execute orExpr.wacc" in pending /*{
    fullExec(dir + orExpr.wacc, "") shouldBe Some("true\ntrue\nfalse\n")
  }*/

  it should "correctly execute ordAndchrExpr.wacc" in pending /*{
    fullExec(dir + ordAndchrExpr.wacc, "") shouldBe Some("a is 97\n99 is c\n")
  }*/

  it should "correctly execute plusExpr.wacc" in pending /*{
    fullExec(dir + plusExpr.wacc, "") shouldBe Some("35\n")
  }*/

  it should "correctly execute plusMinusExpr.wacc" in pending /*{
    fullExec(dir + plusMinusExpr.wacc, "") shouldBe Some("-1\n")
  }*/

  it should "correctly execute plusNoWhitespaceExpr.wacc" in pending /*{
    fullExec(dir + plusNoWhitespaceExpr.wacc, "") shouldBe Some("3\n")
  }*/

  it should "correctly execute plusPlusExpr.wacc" in pending /*{
    fullExec(dir + plusPlusExpr.wacc, "") shouldBe Some("3\n")
  }*/

  it should "correctly execute sequentialCount.wacc" in pending /*{
    fullExec(dir + sequentialCount.wacc, "") shouldBe Some("Can you count to 10?\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")
  }*/

  it should "correctly execute stringEqualsExpr.wacc" in pending /*{
    fullExec(dir + stringEqualsExpr.wacc, "") shouldBe Some("true\nfalse\nfalse\n")
  }*/

}