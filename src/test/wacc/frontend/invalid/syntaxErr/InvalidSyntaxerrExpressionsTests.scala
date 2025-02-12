package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/expressions/"

  it should "frontend analyse missingOperand1.wacc" in {
    frontendStatus(dir + "missingOperand1.wacc") shouldBe 100
  }

  it should "frontend analyse missingOperand2.wacc" in {
    frontendStatus(dir + "missingOperand2.wacc") shouldBe 100
  }

  it should "frontend analyse printlnConcat.wacc" in {
    frontendStatus(dir + "printlnConcat.wacc") shouldBe 100
  }

}
