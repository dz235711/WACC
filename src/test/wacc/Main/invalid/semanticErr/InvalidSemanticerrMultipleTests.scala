package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrMultipleTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/multiple/"

  it should "pass funcMess.wacc" in pending /*{
    runFrontend(Array(dir+"funcMess.wacc"))._1 shouldBe 200
  }*/

  it should "pass ifAndWhileErrs.wacc" in pending /*{
    runFrontend(Array(dir+"ifAndWhileErrs.wacc"))._1 shouldBe 200
  }*/

  it should "pass messyExpr.wacc" in pending /*{
    runFrontend(Array(dir+"messyExpr.wacc"))._1 shouldBe 200
  }*/

  it should "pass multiCaseSensitivity.wacc" in pending /*{
    runFrontend(Array(dir+"multiCaseSensitivity.wacc"))._1 shouldBe 200
  }*/

  it should "pass multiTypeErrs.wacc" in pending /*{
    runFrontend(Array(dir+"multiTypeErrs.wacc"))._1 shouldBe 200
  }*/

  it should "pass obfuscatingReturnsWithWhile.wacc" in pending /*{
    runFrontend(Array(dir+"obfuscatingReturnsWithWhile.wacc"))._1 shouldBe 200
  }*/

}