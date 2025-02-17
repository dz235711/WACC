package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/IO/"

  it should "frontend analyse readTypeErr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "readTypeErr.wacc") shouldBe 200
  }

}
