package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/expressions/"

  it should "pass boolOpTypeErr.wacc" in {
    frontendStatus(dir + "boolOpTypeErr.wacc") shouldBe 200
  }

  it should "pass exprTypeErr.wacc" in {
    frontendStatus(dir + "exprTypeErr.wacc") shouldBe 200
  }

  it should "pass intOpTypeErr.wacc" in {
    frontendStatus(dir + "intOpTypeErr.wacc") shouldBe 200
  }

  it should "pass lessPairExpr.wacc" in {
    frontendStatus(dir + "lessPairExpr.wacc") shouldBe 200
  }

  it should "pass mixedOpTypeErr.wacc" in {
    frontendStatus(dir + "mixedOpTypeErr.wacc") shouldBe 200
  }

  it should "pass moreArrExpr.wacc" in {
    frontendStatus(dir + "moreArrExpr.wacc") shouldBe 200
  }

  it should "pass stringElemErr.wacc" in {
    frontendStatus(dir + "stringElemErr.wacc") shouldBe 200
  }

}
