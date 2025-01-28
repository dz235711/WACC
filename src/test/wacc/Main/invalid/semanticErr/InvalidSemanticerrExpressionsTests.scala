package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/expressions/"

  it should "pass boolOpTypeErr.wacc" in pending /*{
    runFrontend(Array(dir+"boolOpTypeErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass exprTypeErr.wacc" in pending /*{
    runFrontend(Array(dir+"exprTypeErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass intOpTypeErr.wacc" in pending /*{
    runFrontend(Array(dir+"intOpTypeErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass lessPairExpr.wacc" in pending /*{
    runFrontend(Array(dir+"lessPairExpr.wacc"))._1 shouldBe 200
  }*/

  it should "pass mixedOpTypeErr.wacc" in pending /*{
    runFrontend(Array(dir+"mixedOpTypeErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass moreArrExpr.wacc" in pending /*{
    runFrontend(Array(dir+"moreArrExpr.wacc"))._1 shouldBe 200
  }*/

  it should "pass stringElemErr.wacc" in pending /*{
    runFrontend(Array(dir+"stringElemErr.wacc"))._1 shouldBe 200
  }*/

}