package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrMultipleTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/multiple/"

  it should "pass funcMess.wacc" in {
    frontendStatus(dir + "funcMess.wacc") shouldBe 200
  }

  it should "pass ifAndWhileErrs.wacc" in {
    frontendStatus(dir + "ifAndWhileErrs.wacc") shouldBe 200
  }

  it should "pass messyExpr.wacc" in {
    frontendStatus(dir + "messyExpr.wacc") shouldBe 200
  }

  it should "pass multiCaseSensitivity.wacc" in {
    frontendStatus(dir + "multiCaseSensitivity.wacc") shouldBe 200
  }

  it should "pass multiTypeErrs.wacc" in {
    frontendStatus(dir + "multiTypeErrs.wacc") shouldBe 200
  }

  it should "pass obfuscatingReturnsWithWhile.wacc" in {
    frontendStatus(dir + "obfuscatingReturnsWithWhile.wacc") shouldBe 200
  }

}
