package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/print/"

  it should "pass printTypeErr01.wacc" in {
    runFrontend(Array(dir + "printTypeErr01.wacc"))._1 shouldBe 200
  }

}
