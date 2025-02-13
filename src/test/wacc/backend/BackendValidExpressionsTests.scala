package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "execute andExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "andExpr.wacc", "") shouldBe Some("false\ntrue\nfalse\n")
  }*/

  it should "execute andOverOrExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "andOverOrExpr.wacc", "") shouldBe Some("true\nfalse\n")
  }*/

  it should "execute boolCalc.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "boolCalc.wacc", "") shouldBe Some("false\n")
  }*/

  it should "execute boolExpr1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "boolExpr1.wacc", "") shouldBe Some("Correct\n")
  }*/

  it should "execute charComparisonExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "charComparisonExpr.wacc", "") shouldBe Some("false\ntrue\ntrue\ntrue\nfalse\nfalse\n")
  }*/

  it should "execute divExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "divExpr.wacc", "") shouldBe Some("1\n")
  }*/

  it should "execute equalsExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "equalsExpr.wacc", "") shouldBe Some("false\nfalse\ntrue\n")
  }*/

  it should "execute equalsOverAnd.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "equalsOverAnd.wacc", "") shouldBe Some("false\ntrue\n")
  }*/

  it should "execute equalsOverBool.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "equalsOverBool.wacc", "") shouldBe Some("true\nfalse\n")
  }*/

  it should "execute equalsOverOr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "equalsOverOr.wacc", "") shouldBe Some("true\nfalse\n")
  }*/

  it should "execute greaterEqExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "greaterEqExpr.wacc", "") shouldBe Some("false\ntrue\ntrue\n")
  }*/

  it should "execute greaterExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "greaterExpr.wacc", "") shouldBe Some("false\ntrue\n")
  }*/

  it should "execute intCalc.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intCalc.wacc", "") shouldBe Some("72\n")
  }*/

  it should "execute intExpr1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intExpr1.wacc", "") shouldBe Some("Correct\n")
  }*/

  it should "execute lessCharExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "lessCharExpr.wacc", "") shouldBe Some("true\nfalse\n")
  }*/

  it should "execute lessEqExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "lessEqExpr.wacc", "") shouldBe Some("true\nfalse\ntrue\n")
  }*/

  it should "execute lessExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "lessExpr.wacc", "") shouldBe Some("true\nfalse\n")
  }*/

  it should "execute longExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "longExpr.wacc", "") shouldBe Some("")
  }*/

  it should "execute longExpr2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "longExpr2.wacc", "") shouldBe Some("")
  }*/

  it should "execute longExpr3.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "longExpr3.wacc", "") shouldBe Some("")
  }*/

  it should "execute longSplitExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "longSplitExpr.wacc", "") shouldBe Some("")
  }*/

  it should "execute longSplitExpr2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "longSplitExpr2.wacc", "") shouldBe Some("362880\n128\n")
  }*/

  it should "execute minusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "minusExpr.wacc", "") shouldBe Some("5\n")
  }*/

  it should "execute minusMinusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "minusMinusExpr.wacc", "") shouldBe Some("3\n")
  }*/

  it should "execute minusNoWhitespaceExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "minusNoWhitespaceExpr.wacc", "") shouldBe Some("-1\n")
  }*/

  it should "execute minusPlusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "minusPlusExpr.wacc", "") shouldBe Some("-1\n")
  }*/

  it should "execute modExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "modExpr.wacc", "") shouldBe Some("2\n")
  }*/

  it should "execute multExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "multExpr.wacc", "") shouldBe Some("15\n")
  }*/

  it should "execute multNoWhitespaceExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "multNoWhitespaceExpr.wacc", "") shouldBe Some("2\n")
  }*/

  it should "execute negBothDiv.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negBothDiv.wacc", "") shouldBe Some("2\n")
  }*/

  it should "execute negBothMod.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negBothMod.wacc", "") shouldBe Some("-2\n")
  }*/

  it should "execute negDividendDiv.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negDividendDiv.wacc", "") shouldBe Some("-2\n")
  }*/

  it should "execute negDividendMod.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negDividendMod.wacc", "") shouldBe Some("-2\n")
  }*/

  it should "execute negDivisorDiv.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negDivisorDiv.wacc", "") shouldBe Some("-2\n")
  }*/

  it should "execute negDivisorMod.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negDivisorMod.wacc", "") shouldBe Some("2\n")
  }*/

  it should "execute negExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negExpr.wacc", "") shouldBe Some("-42\n")
  }*/

  it should "execute notExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "notExpr.wacc", "") shouldBe Some("false\ntrue\n")
  }*/

  it should "execute notequalsExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "notequalsExpr.wacc", "") shouldBe Some("true\ntrue\nfalse\n")
  }*/

  it should "execute orExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "orExpr.wacc", "") shouldBe Some("true\ntrue\nfalse\n")
  }*/

  it should "execute ordAndchrExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "ordAndchrExpr.wacc", "") shouldBe Some("a is 97\n99 is c\n")
  }*/

  it should "execute plusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "plusExpr.wacc", "") shouldBe Some("35\n")
  }*/

  it should "execute plusMinusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "plusMinusExpr.wacc", "") shouldBe Some("-1\n")
  }*/

  it should "execute plusNoWhitespaceExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "plusNoWhitespaceExpr.wacc", "") shouldBe Some("3\n")
  }*/

  it should "execute plusPlusExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "plusPlusExpr.wacc", "") shouldBe Some("3\n")
  }*/

  it should "execute sequentialCount.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "sequentialCount.wacc", "") shouldBe Some("Can you count to 10?\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")
  }*/

  it should "execute stringEqualsExpr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "stringEqualsExpr.wacc", "") shouldBe Some("true\nfalse\nfalse\n")
  }*/

}
