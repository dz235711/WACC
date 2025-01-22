package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/while/"

  it should "pass falsErr.wacc" in pending /*{
    runFrontend(Array(dir+"falsErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass truErr.wacc" in pending /*{
    runFrontend(Array(dir+"truErr.wacc"))._1 shouldBe 200
  }*/

  it should "pass whileIntCondition.wacc" in pending /*{
    runFrontend(Array(dir+"whileIntCondition.wacc"))._1 shouldBe 200
  }*/

}