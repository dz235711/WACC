package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "pass andExpr.wacc" in {
    runFrontend(Array(dir + "andExpr.wacc"))._1 shouldBe 0
  }

  it should "pass andOverOrExpr.wacc" in {
    runFrontend(Array(dir + "andOverOrExpr.wacc"))._1 shouldBe 0
  }

  it should "pass boolCalc.wacc" in {
    runFrontend(Array(dir + "boolCalc.wacc"))._1 shouldBe 0
  }

  it should "pass boolExpr1.wacc" in {
    runFrontend(Array(dir + "boolExpr1.wacc"))._1 shouldBe 0
  }

  it should "pass charComparisonExpr.wacc" in {
    runFrontend(Array(dir + "charComparisonExpr.wacc"))._1 shouldBe 0
  }

  it should "pass divExpr.wacc" in {
    runFrontend(Array(dir + "divExpr.wacc"))._1 shouldBe 0
  }

  it should "pass equalsExpr.wacc" in {
    runFrontend(Array(dir + "equalsExpr.wacc"))._1 shouldBe 0
  }

  it should "pass equalsOverAnd.wacc" in {
    runFrontend(Array(dir + "equalsOverAnd.wacc"))._1 shouldBe 0
  }

  it should "pass equalsOverBool.wacc" in {
    runFrontend(Array(dir + "equalsOverBool.wacc"))._1 shouldBe 0
  }

  it should "pass equalsOverOr.wacc" in {
    runFrontend(Array(dir + "equalsOverOr.wacc"))._1 shouldBe 0
  }

  it should "pass greaterEqExpr.wacc" in {
    runFrontend(Array(dir + "greaterEqExpr.wacc"))._1 shouldBe 0
  }

  it should "pass greaterExpr.wacc" in {
    runFrontend(Array(dir + "greaterExpr.wacc"))._1 shouldBe 0
  }

  it should "pass intCalc.wacc" in {
    runFrontend(Array(dir + "intCalc.wacc"))._1 shouldBe 0
  }

  it should "pass intExpr1.wacc" in {
    runFrontend(Array(dir + "intExpr1.wacc"))._1 shouldBe 0
  }

  it should "pass lessCharExpr.wacc" in {
    runFrontend(Array(dir + "lessCharExpr.wacc"))._1 shouldBe 0
  }

  it should "pass lessEqExpr.wacc" in {
    runFrontend(Array(dir + "lessEqExpr.wacc"))._1 shouldBe 0
  }

  it should "pass lessExpr.wacc" in {
    runFrontend(Array(dir + "lessExpr.wacc"))._1 shouldBe 0
  }

  it should "pass longExpr.wacc" in {
    runFrontend(Array(dir + "longExpr.wacc"))._1 shouldBe 0
  }

  it should "pass longExpr2.wacc" in {
    runFrontend(Array(dir + "longExpr2.wacc"))._1 shouldBe 0
  }

  it should "pass longExpr3.wacc" in {
    runFrontend(Array(dir + "longExpr3.wacc"))._1 shouldBe 0
  }

  it should "pass longSplitExpr.wacc" in {
    runFrontend(Array(dir + "longSplitExpr.wacc"))._1 shouldBe 0
  }

  it should "pass longSplitExpr2.wacc" in {
    runFrontend(Array(dir + "longSplitExpr2.wacc"))._1 shouldBe 0
  }

  it should "pass minusExpr.wacc" in {
    runFrontend(Array(dir + "minusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass minusMinusExpr.wacc" in {
    runFrontend(Array(dir + "minusMinusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass minusNoWhitespaceExpr.wacc" in {
    runFrontend(Array(dir + "minusNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }

  it should "pass minusPlusExpr.wacc" in {
    runFrontend(Array(dir + "minusPlusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass modExpr.wacc" in {
    runFrontend(Array(dir + "modExpr.wacc"))._1 shouldBe 0
  }

  it should "pass multExpr.wacc" in {
    runFrontend(Array(dir + "multExpr.wacc"))._1 shouldBe 0
  }

  it should "pass multNoWhitespaceExpr.wacc" in {
    runFrontend(Array(dir + "multNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }

  it should "pass negBothDiv.wacc" in {
    runFrontend(Array(dir + "negBothDiv.wacc"))._1 shouldBe 0
  }

  it should "pass negBothMod.wacc" in {
    runFrontend(Array(dir + "negBothMod.wacc"))._1 shouldBe 0
  }

  it should "pass negDividendDiv.wacc" in {
    runFrontend(Array(dir + "negDividendDiv.wacc"))._1 shouldBe 0
  }

  it should "pass negDividendMod.wacc" in {
    runFrontend(Array(dir + "negDividendMod.wacc"))._1 shouldBe 0
  }

  it should "pass negDivisorDiv.wacc" in {
    runFrontend(Array(dir + "negDivisorDiv.wacc"))._1 shouldBe 0
  }

  it should "pass negDivisorMod.wacc" in {
    runFrontend(Array(dir + "negDivisorMod.wacc"))._1 shouldBe 0
  }

  it should "pass negExpr.wacc" in {
    runFrontend(Array(dir + "negExpr.wacc"))._1 shouldBe 0
  }

  it should "pass notExpr.wacc" in {
    runFrontend(Array(dir + "notExpr.wacc"))._1 shouldBe 0
  }

  it should "pass notequalsExpr.wacc" in {
    runFrontend(Array(dir + "notequalsExpr.wacc"))._1 shouldBe 0
  }

  it should "pass orExpr.wacc" in {
    runFrontend(Array(dir + "orExpr.wacc"))._1 shouldBe 0
  }

  it should "pass ordAndchrExpr.wacc" in {
    runFrontend(Array(dir + "ordAndchrExpr.wacc"))._1 shouldBe 0
  }

  it should "pass plusExpr.wacc" in {
    runFrontend(Array(dir + "plusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass plusMinusExpr.wacc" in {
    runFrontend(Array(dir + "plusMinusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass plusNoWhitespaceExpr.wacc" in {
    runFrontend(Array(dir + "plusNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }

  it should "pass plusPlusExpr.wacc" in {
    runFrontend(Array(dir + "plusPlusExpr.wacc"))._1 shouldBe 0
  }

  it should "pass sequentialCount.wacc" in {
    runFrontend(Array(dir + "sequentialCount.wacc"))._1 shouldBe 0
  }

  it should "pass stringEqualsExpr.wacc" in {
    runFrontend(Array(dir + "stringEqualsExpr.wacc"))._1 shouldBe 0
  }

}
