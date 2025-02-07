package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/expressions/"

  it should "pass boolOpTypeErr.wacc" in {
    runFrontend(Array(dir+"boolOpTypeErr.wacc"))._1 shouldBe 200
  }

  it should "pass exprTypeErr.wacc" in {
    runFrontend(Array(dir+"exprTypeErr.wacc"))._1 shouldBe 200
  }

  it should "pass intOpTypeErr.wacc" in {
    runFrontend(Array(dir+"intOpTypeErr.wacc"))._1 shouldBe 200
  }

  it should "pass lessPairExpr.wacc" in {
    runFrontend(Array(dir+"lessPairExpr.wacc"))._1 shouldBe 200
  }

  it should "pass mixedOpTypeErr.wacc" in {
    runFrontend(Array(dir+"mixedOpTypeErr.wacc"))._1 shouldBe 200
  }

  it should "pass moreArrExpr.wacc" in {
    runFrontend(Array(dir+"moreArrExpr.wacc"))._1 shouldBe 200
  }

  it should "pass stringElemErr.wacc" in {
    runFrontend(Array(dir+"stringElemErr.wacc"))._1 shouldBe 200
  }

}