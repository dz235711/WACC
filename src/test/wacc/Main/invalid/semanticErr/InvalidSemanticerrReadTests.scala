package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/read/"

  it should "pass readIntoBadFst.wacc" in {
    frontendStatus(dir + "readIntoBadFst.wacc") shouldBe 200
  }

  it should "pass readIntoBadSnd.wacc" in {
    frontendStatus(dir + "readIntoBadSnd.wacc") shouldBe 200
  }

  it should "pass readTypeErr01.wacc" in {
    frontendStatus(dir + "readTypeErr01.wacc") shouldBe 200
  }

}
