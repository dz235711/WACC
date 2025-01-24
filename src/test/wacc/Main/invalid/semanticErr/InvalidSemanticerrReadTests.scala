package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/read/"

  it should "pass readIntoBadFst.wacc" in pending /*{
    runFrontend(Array(dir+"readIntoBadFst.wacc"))._1 shouldBe 200
  }*/

  it should "pass readIntoBadSnd.wacc" in pending /*{
    runFrontend(Array(dir+"readIntoBadSnd.wacc"))._1 shouldBe 200
  }*/

  it should "pass readTypeErr01.wacc" in pending /*{
    runFrontend(Array(dir+"readTypeErr01.wacc"))._1 shouldBe 200
  }*/

}