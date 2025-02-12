package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/expressions/"

  it should "frontend analyse boolOpTypeErr.wacc" in {
    frontendStatus(dir + "boolOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse exprTypeErr.wacc" in {
    frontendStatus(dir + "exprTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse intOpTypeErr.wacc" in {
    frontendStatus(dir + "intOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse lessPairExpr.wacc" in {
    frontendStatus(dir + "lessPairExpr.wacc") shouldBe 200
  }

  it should "frontend analyse mixedOpTypeErr.wacc" in {
    frontendStatus(dir + "mixedOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse moreArrExpr.wacc" in {
    frontendStatus(dir + "moreArrExpr.wacc") shouldBe 200
  }

  it should "frontend analyse stringElemErr.wacc" in {
    frontendStatus(dir + "stringElemErr.wacc") shouldBe 200
  }

}
