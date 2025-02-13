package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/print/"

  it should "frontend analyse printlnCharArry.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printlnCharArry.wacc") shouldBe 100
  }

}
