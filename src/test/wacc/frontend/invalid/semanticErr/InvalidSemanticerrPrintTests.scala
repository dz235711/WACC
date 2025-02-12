package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/print/"

  it should "frontend analyse printTypeErr01.wacc" in {
    frontendStatus(dir + "printTypeErr01.wacc") shouldBe 200
  }

}
