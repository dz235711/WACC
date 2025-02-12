package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "frontend analyse andExpr.wacc" in {
    frontendStatus(dir + "andExpr.wacc") shouldBe 0
  }

  it should "frontend analyse andOverOrExpr.wacc" in {
    frontendStatus(dir + "andOverOrExpr.wacc") shouldBe 0
  }

  it should "frontend analyse boolCalc.wacc" in {
    frontendStatus(dir + "boolCalc.wacc") shouldBe 0
  }

  it should "frontend analyse boolExpr1.wacc" in {
    frontendStatus(dir + "boolExpr1.wacc") shouldBe 0
  }

  it should "frontend analyse charComparisonExpr.wacc" in {
    frontendStatus(dir + "charComparisonExpr.wacc") shouldBe 0
  }

  it should "frontend analyse divExpr.wacc" in {
    frontendStatus(dir + "divExpr.wacc") shouldBe 0
  }

  it should "frontend analyse equalsExpr.wacc" in {
    frontendStatus(dir + "equalsExpr.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverAnd.wacc" in {
    frontendStatus(dir + "equalsOverAnd.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverBool.wacc" in {
    frontendStatus(dir + "equalsOverBool.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverOr.wacc" in {
    frontendStatus(dir + "equalsOverOr.wacc") shouldBe 0
  }

  it should "frontend analyse greaterEqExpr.wacc" in {
    frontendStatus(dir + "greaterEqExpr.wacc") shouldBe 0
  }

  it should "frontend analyse greaterExpr.wacc" in {
    frontendStatus(dir + "greaterExpr.wacc") shouldBe 0
  }

  it should "frontend analyse intCalc.wacc" in {
    frontendStatus(dir + "intCalc.wacc") shouldBe 0
  }

  it should "frontend analyse intExpr1.wacc" in {
    frontendStatus(dir + "intExpr1.wacc") shouldBe 0
  }

  it should "frontend analyse lessCharExpr.wacc" in {
    frontendStatus(dir + "lessCharExpr.wacc") shouldBe 0
  }

  it should "frontend analyse lessEqExpr.wacc" in {
    frontendStatus(dir + "lessEqExpr.wacc") shouldBe 0
  }

  it should "frontend analyse lessExpr.wacc" in {
    frontendStatus(dir + "lessExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr.wacc" in {
    frontendStatus(dir + "longExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr2.wacc" in {
    frontendStatus(dir + "longExpr2.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr3.wacc" in {
    frontendStatus(dir + "longExpr3.wacc") shouldBe 0
  }

  it should "frontend analyse longSplitExpr.wacc" in {
    frontendStatus(dir + "longSplitExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longSplitExpr2.wacc" in {
    frontendStatus(dir + "longSplitExpr2.wacc") shouldBe 0
  }

  it should "frontend analyse minusExpr.wacc" in {
    frontendStatus(dir + "minusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusMinusExpr.wacc" in {
    frontendStatus(dir + "minusMinusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "minusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusPlusExpr.wacc" in {
    frontendStatus(dir + "minusPlusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse modExpr.wacc" in {
    frontendStatus(dir + "modExpr.wacc") shouldBe 0
  }

  it should "frontend analyse multExpr.wacc" in {
    frontendStatus(dir + "multExpr.wacc") shouldBe 0
  }

  it should "frontend analyse multNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "multNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse negBothDiv.wacc" in {
    frontendStatus(dir + "negBothDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negBothMod.wacc" in {
    frontendStatus(dir + "negBothMod.wacc") shouldBe 0
  }

  it should "frontend analyse negDividendDiv.wacc" in {
    frontendStatus(dir + "negDividendDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negDividendMod.wacc" in {
    frontendStatus(dir + "negDividendMod.wacc") shouldBe 0
  }

  it should "frontend analyse negDivisorDiv.wacc" in {
    frontendStatus(dir + "negDivisorDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negDivisorMod.wacc" in {
    frontendStatus(dir + "negDivisorMod.wacc") shouldBe 0
  }

  it should "frontend analyse negExpr.wacc" in {
    frontendStatus(dir + "negExpr.wacc") shouldBe 0
  }

  it should "frontend analyse notExpr.wacc" in {
    frontendStatus(dir + "notExpr.wacc") shouldBe 0
  }

  it should "frontend analyse notequalsExpr.wacc" in {
    frontendStatus(dir + "notequalsExpr.wacc") shouldBe 0
  }

  it should "frontend analyse orExpr.wacc" in {
    frontendStatus(dir + "orExpr.wacc") shouldBe 0
  }

  it should "frontend analyse ordAndchrExpr.wacc" in {
    frontendStatus(dir + "ordAndchrExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusExpr.wacc" in {
    frontendStatus(dir + "plusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusMinusExpr.wacc" in {
    frontendStatus(dir + "plusMinusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusNoWhitespaceExpr.wacc" in {
    frontendStatus(dir + "plusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusPlusExpr.wacc" in {
    frontendStatus(dir + "plusPlusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse sequentialCount.wacc" in {
    frontendStatus(dir + "sequentialCount.wacc") shouldBe 0
  }

  it should "frontend analyse stringEqualsExpr.wacc" in {
    frontendStatus(dir + "stringEqualsExpr.wacc") shouldBe 0
  }

}
