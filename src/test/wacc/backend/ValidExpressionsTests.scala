package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "pass andExpr.wacc" in {
    frontendStatus(dir + "andExpr.wacc") shouldBe 0
  }

  it should "pass andOverOrExpr.wacc" in {
    frontendStatus(dir + "andOverOrExpr.wacc") shouldBe 0
  }

  it should "pass boolCalc.wacc" in {
    frontendStatus(dir + "boolCalc.wacc") shouldBe 0
  }

  it should "pass boolExpr1.wacc" in {
    frontendStatus(dir + "boolExpr1.wacc") shouldBe 0
  }

  it should "pass charComparisonExpr.wacc" in {
    frontendStatus(dir + "charComparisonExpr.wacc") shouldBe 0
  }

  it should "pass divExpr.wacc" in {
    frontendStatus(dir + "divExpr.wacc") shouldBe 0
  }

  it should "pass equalsExpr.wacc" in {
    frontendStatus(dir + "equalsExpr.wacc") shouldBe 0
  }

  it should "pass equalsOverAnd.wacc" in {
    frontendStatus(dir + "equalsOverAnd.wacc") shouldBe 0
  }

  it should "pass equalsOverBool.wacc" in {
    frontendStatus(dir + "equalsOverBool.wacc") shouldBe 0
  }

  it should "pass equalsOverOr.wacc" in {
    frontendStatus(dir + "equalsOverOr.wacc") shouldBe 0
  }

  it should "pass greaterEqExpr.wacc" in {
    frontendStatus(dir + "greaterEqExpr.wacc") shouldBe 0
  }

  it should "pass greaterExpr.wacc" in {
    frontendStatus(dir + "greaterExpr.wacc") shouldBe 0
  }

  it should "pass intCalc.wacc" in {
    frontendStatus(dir + "intCalc.wacc") shouldBe 0
  }

  it should "pass intExpr1.wacc" in {
    frontendStatus(dir + "intExpr1.wacc") shouldBe 0
  }

  it should "pass lessCharExpr.wacc" in {
    frontendStatus(dir + "lessCharExpr.wacc") shouldBe 0
  }

  it should "pass lessEqExpr.wacc" in {
    frontendStatus(dir + "lessEqExpr.wacc") shouldBe 0
  }

  it should "pass lessExpr.wacc" in {
    frontendStatus(dir + "lessExpr.wacc") shouldBe 0
  }

  it should "pass longExpr.wacc" in {
    frontendStatus(dir + "longExpr.wacc") shouldBe 0
  }

  it should "pass longExpr2.wacc" in {
    frontendStatus(dir + "longExpr2.wacc") shouldBe 0
  }

  it should "pass longExpr3.wacc" in {
    frontendStatus(dir + "longExpr3.wacc") shouldBe 0
  }

  it should "pass longSplitExpr.wacc" in {
    frontendStatus(dir + "longSplitExpr.wacc") shouldBe 0
  }

  it should "pass longSplitExpr2.wacc" in {
    frontendStatus(dir + "longSplitExpr2.wacc") shouldBe 0
  }

  it should "pass minusExpr.wacc" in {
    frontendStatus(dir + "minusExpr.wacc") shouldBe 0
  }

  it should "pass minusMinusExpr.wacc" in {
    frontendStatus(dir + "minusMinusExpr.wacc") shouldBe 0
  }

  it should "pass minusNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "minusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "pass minusPlusExpr.wacc" in {
    frontendStatus(dir + "minusPlusExpr.wacc") shouldBe 0
  }

  it should "pass modExpr.wacc" in {
    frontendStatus(dir + "modExpr.wacc") shouldBe 0
  }

  it should "pass multExpr.wacc" in {
    frontendStatus(dir + "multExpr.wacc") shouldBe 0
  }

  it should "pass multNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "multNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "pass negBothDiv.wacc" in {
    frontendStatus(dir + "negBothDiv.wacc") shouldBe 0
  }

  it should "pass negBothMod.wacc" in {
    frontendStatus(dir + "negBothMod.wacc") shouldBe 0
  }

  it should "pass negDividendDiv.wacc" in {
    frontendStatus(dir + "negDividendDiv.wacc") shouldBe 0
  }

  it should "pass negDividendMod.wacc" in {
    frontendStatus(dir + "negDividendMod.wacc") shouldBe 0
  }

  it should "pass negDivisorDiv.wacc" in {
    frontendStatus(dir + "negDivisorDiv.wacc") shouldBe 0
  }

  it should "pass negDivisorMod.wacc" in {
    frontendStatus(dir + "negDivisorMod.wacc") shouldBe 0
  }

  it should "pass negExpr.wacc" in {
    frontendStatus(dir + "negExpr.wacc") shouldBe 0
  }

  it should "pass notExpr.wacc" in {
    frontendStatus(dir + "notExpr.wacc") shouldBe 0
  }

  it should "pass notequalsExpr.wacc" in {
    frontendStatus(dir + "notequalsExpr.wacc") shouldBe 0
  }

  it should "pass orExpr.wacc" in {
    frontendStatus(dir + "orExpr.wacc") shouldBe 0
  }

  it should "pass ordAndchrExpr.wacc" in {
    frontendStatus(dir + "ordAndchrExpr.wacc") shouldBe 0
  }

  it should "pass plusExpr.wacc" in {
    frontendStatus(dir + "plusExpr.wacc") shouldBe 0
  }

  it should "pass plusMinusExpr.wacc" in {
    frontendStatus(dir + "plusMinusExpr.wacc") shouldBe 0
  }

  it should "pass plusNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "plusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "pass plusPlusExpr.wacc" in {
    frontendStatus(dir + "plusPlusExpr.wacc") shouldBe 0
  }

  it should "pass sequentialCount.wacc" in {
    frontendStatus(dir + "sequentialCount.wacc") shouldBe 0
  }

  it should "pass stringEqualsExpr.wacc" in {
    frontendStatus(dir + "stringEqualsExpr.wacc") shouldBe 0
  }

}
