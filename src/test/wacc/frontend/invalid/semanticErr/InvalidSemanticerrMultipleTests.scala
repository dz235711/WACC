package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrMultipleTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/multiple/"

  it should "frontend analyse funcMess.wacc" taggedAs Frontend in {
    frontendStatus(dir + "funcMess.wacc") shouldBe 200
  }

  it should "frontend analyse ifAndWhileErrs.wacc" taggedAs Frontend in {
    frontendStatus(dir + "ifAndWhileErrs.wacc") shouldBe 200
  }

  it should "frontend analyse messyExpr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "messyExpr.wacc") shouldBe 200
  }

  it should "frontend analyse multiCaseSensitivity.wacc" taggedAs Frontend in {
    frontendStatus(dir + "multiCaseSensitivity.wacc") shouldBe 200
  }

  it should "frontend analyse multiTypeErrs.wacc" taggedAs Frontend in {
    frontendStatus(dir + "multiTypeErrs.wacc") shouldBe 200
  }

  it should "frontend analyse obfuscatingReturnsWithWhile.wacc" taggedAs Frontend in {
    frontendStatus(dir + "obfuscatingReturnsWithWhile.wacc") shouldBe 200
  }

}
