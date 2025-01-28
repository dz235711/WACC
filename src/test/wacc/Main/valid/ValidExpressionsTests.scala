package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/expressions/"

  it should "pass andExpr.wacc" in pending /*{
    runFrontend(Array(dir+"andExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass andOverOrExpr.wacc" in pending /*{
    runFrontend(Array(dir+"andOverOrExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass boolCalc.wacc" in pending /*{
    runFrontend(Array(dir+"boolCalc.wacc"))._1 shouldBe 0
  }*/

  it should "pass boolExpr1.wacc" in pending /*{
    runFrontend(Array(dir+"boolExpr1.wacc"))._1 shouldBe 0
  }*/

  it should "pass charComparisonExpr.wacc" in pending /*{
    runFrontend(Array(dir+"charComparisonExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass divExpr.wacc" in pending /*{
    runFrontend(Array(dir+"divExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass equalsExpr.wacc" in pending /*{
    runFrontend(Array(dir+"equalsExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass equalsOverAnd.wacc" in pending /*{
    runFrontend(Array(dir+"equalsOverAnd.wacc"))._1 shouldBe 0
  }*/

  it should "pass equalsOverBool.wacc" in pending /*{
    runFrontend(Array(dir+"equalsOverBool.wacc"))._1 shouldBe 0
  }*/

  it should "pass equalsOverOr.wacc" in pending /*{
    runFrontend(Array(dir+"equalsOverOr.wacc"))._1 shouldBe 0
  }*/

  it should "pass greaterEqExpr.wacc" in pending /*{
    runFrontend(Array(dir+"greaterEqExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass greaterExpr.wacc" in pending /*{
    runFrontend(Array(dir+"greaterExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass intCalc.wacc" in pending /*{
    runFrontend(Array(dir+"intCalc.wacc"))._1 shouldBe 0
  }*/

  it should "pass intExpr1.wacc" in pending /*{
    runFrontend(Array(dir+"intExpr1.wacc"))._1 shouldBe 0
  }*/

  it should "pass lessCharExpr.wacc" in pending /*{
    runFrontend(Array(dir+"lessCharExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass lessEqExpr.wacc" in pending /*{
    runFrontend(Array(dir+"lessEqExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass lessExpr.wacc" in pending /*{
    runFrontend(Array(dir+"lessExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass longExpr.wacc" in pending /*{
    runFrontend(Array(dir+"longExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass longExpr2.wacc" in pending /*{
    runFrontend(Array(dir+"longExpr2.wacc"))._1 shouldBe 0
  }*/

  it should "pass longExpr3.wacc" in pending /*{
    runFrontend(Array(dir+"longExpr3.wacc"))._1 shouldBe 0
  }*/

  it should "pass longSplitExpr.wacc" in pending /*{
    runFrontend(Array(dir+"longSplitExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass longSplitExpr2.wacc" in pending /*{
    runFrontend(Array(dir+"longSplitExpr2.wacc"))._1 shouldBe 0
  }*/

  it should "pass minusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"minusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass minusMinusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"minusMinusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass minusNoWhitespaceExpr.wacc" in pending /*{
    runFrontend(Array(dir+"minusNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass minusPlusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"minusPlusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass modExpr.wacc" in pending /*{
    runFrontend(Array(dir+"modExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass multExpr.wacc" in pending /*{
    runFrontend(Array(dir+"multExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass multNoWhitespaceExpr.wacc" in pending /*{
    runFrontend(Array(dir+"multNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass negBothDiv.wacc" in pending /*{
    runFrontend(Array(dir+"negBothDiv.wacc"))._1 shouldBe 0
  }*/

  it should "pass negBothMod.wacc" in pending /*{
    runFrontend(Array(dir+"negBothMod.wacc"))._1 shouldBe 0
  }*/

  it should "pass negDividendDiv.wacc" in pending /*{
    runFrontend(Array(dir+"negDividendDiv.wacc"))._1 shouldBe 0
  }*/

  it should "pass negDividendMod.wacc" in pending /*{
    runFrontend(Array(dir+"negDividendMod.wacc"))._1 shouldBe 0
  }*/

  it should "pass negDivisorDiv.wacc" in pending /*{
    runFrontend(Array(dir+"negDivisorDiv.wacc"))._1 shouldBe 0
  }*/

  it should "pass negDivisorMod.wacc" in pending /*{
    runFrontend(Array(dir+"negDivisorMod.wacc"))._1 shouldBe 0
  }*/

  it should "pass negExpr.wacc" in pending /*{
    runFrontend(Array(dir+"negExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass notExpr.wacc" in pending /*{
    runFrontend(Array(dir+"notExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass notequalsExpr.wacc" in pending /*{
    runFrontend(Array(dir+"notequalsExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass orExpr.wacc" in pending /*{
    runFrontend(Array(dir+"orExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass ordAndchrExpr.wacc" in pending /*{
    runFrontend(Array(dir+"ordAndchrExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass plusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"plusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass plusMinusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"plusMinusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass plusNoWhitespaceExpr.wacc" in pending /*{
    runFrontend(Array(dir+"plusNoWhitespaceExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass plusPlusExpr.wacc" in pending /*{
    runFrontend(Array(dir+"plusPlusExpr.wacc"))._1 shouldBe 0
  }*/

  it should "pass sequentialCount.wacc" in pending /*{
    runFrontend(Array(dir+"sequentialCount.wacc"))._1 shouldBe 0
  }*/

  it should "pass stringEqualsExpr.wacc" in pending /*{
    runFrontend(Array(dir+"stringEqualsExpr.wacc"))._1 shouldBe 0
  }*/

}