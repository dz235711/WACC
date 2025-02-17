package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/expressions/"

  it should "frontend analyse boolOpTypeErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "boolOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse exprTypeErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "exprTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse intOpTypeErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse lessPairExpr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "lessPairExpr.wacc") shouldBe 200
  }

  it should "frontend analyse mixedOpTypeErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "mixedOpTypeErr.wacc") shouldBe 200
  }

  it should "frontend analyse moreArrExpr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "moreArrExpr.wacc") shouldBe 200
  }

  it should "frontend analyse stringElemErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "stringElemErr.wacc") shouldBe 200
  }

}
