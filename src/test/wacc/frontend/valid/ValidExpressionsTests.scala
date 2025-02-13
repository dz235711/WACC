package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "frontend analyse andExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "andExpr.wacc") shouldBe 0
  }

  it should "frontend analyse andOverOrExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "andOverOrExpr.wacc") shouldBe 0
  }

  it should "frontend analyse boolCalc.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "boolCalc.wacc") shouldBe 0
  }

  it should "frontend analyse boolExpr1.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "boolExpr1.wacc") shouldBe 0
  }

  it should "frontend analyse charComparisonExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "charComparisonExpr.wacc") shouldBe 0
  }

  it should "frontend analyse divExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "divExpr.wacc") shouldBe 0
  }

  it should "frontend analyse equalsExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "equalsExpr.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverAnd.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "equalsOverAnd.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverBool.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "equalsOverBool.wacc") shouldBe 0
  }

  it should "frontend analyse equalsOverOr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "equalsOverOr.wacc") shouldBe 0
  }

  it should "frontend analyse greaterEqExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "greaterEqExpr.wacc") shouldBe 0
  }

  it should "frontend analyse greaterExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "greaterExpr.wacc") shouldBe 0
  }

  it should "frontend analyse intCalc.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "intCalc.wacc") shouldBe 0
  }

  it should "frontend analyse intExpr1.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "intExpr1.wacc") shouldBe 0
  }

  it should "frontend analyse lessCharExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "lessCharExpr.wacc") shouldBe 0
  }

  it should "frontend analyse lessEqExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "lessEqExpr.wacc") shouldBe 0
  }

  it should "frontend analyse lessExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "lessExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "longExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "longExpr2.wacc") shouldBe 0
  }

  it should "frontend analyse longExpr3.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "longExpr3.wacc") shouldBe 0
  }

  it should "frontend analyse longSplitExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "longSplitExpr.wacc") shouldBe 0
  }

  it should "frontend analyse longSplitExpr2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "longSplitExpr2.wacc") shouldBe 0
  }

  it should "frontend analyse minusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "minusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusMinusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "minusMinusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusNoWhitespaceExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "minusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse minusPlusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "minusPlusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse modExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "modExpr.wacc") shouldBe 0
  }

  it should "frontend analyse multExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "multExpr.wacc") shouldBe 0
  }

  it should "frontend analyse multNoWhitespaceExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "multNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse negBothDiv.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negBothDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negBothMod.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negBothMod.wacc") shouldBe 0
  }

  it should "frontend analyse negDividendDiv.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negDividendDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negDividendMod.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negDividendMod.wacc") shouldBe 0
  }

  it should "frontend analyse negDivisorDiv.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negDivisorDiv.wacc") shouldBe 0
  }

  it should "frontend analyse negDivisorMod.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negDivisorMod.wacc") shouldBe 0
  }

  it should "frontend analyse negExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "negExpr.wacc") shouldBe 0
  }

  it should "frontend analyse notExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "notExpr.wacc") shouldBe 0
  }

  it should "frontend analyse notequalsExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "notequalsExpr.wacc") shouldBe 0
  }

  it should "frontend analyse orExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "orExpr.wacc") shouldBe 0
  }

  it should "frontend analyse ordAndchrExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ordAndchrExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "plusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusMinusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "plusMinusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusNoWhitespaceExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "plusNoWhitespaceExpr.wacc") shouldBe 0
  }

  it should "frontend analyse plusPlusExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "plusPlusExpr.wacc") shouldBe 0
  }

  it should "frontend analyse sequentialCount.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "sequentialCount.wacc") shouldBe 0
  }

  it should "frontend analyse stringEqualsExpr.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "stringEqualsExpr.wacc") shouldBe 0
  }

}
