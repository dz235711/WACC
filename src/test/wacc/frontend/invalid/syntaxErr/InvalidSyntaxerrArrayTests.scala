package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/array/"

  it should "frontend analyse arrayExpr.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayExpr.wacc") shouldBe 100
  }

}
