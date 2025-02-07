package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/IO/"

  it should "pass readTypeErr.wacc" in {
    runFrontend(Array(dir + "readTypeErr.wacc"))._1 shouldBe 200
  }

}
