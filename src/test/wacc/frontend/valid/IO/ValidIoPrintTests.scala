package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/print/"

  it should "frontend analyse hashInProgram.wacc" in {
    frontendStatus(dir + "hashInProgram.wacc") shouldBe 0
  }

  it should "frontend analyse multipleStringsAssignment.wacc" in {
    frontendStatus(dir + "multipleStringsAssignment.wacc") shouldBe 0
  }

  it should "frontend analyse print-backspace.wacc" in {
    frontendStatus(dir + "print-backspace.wacc") shouldBe 0
  }

  it should "frontend analyse print.wacc" in {
    frontendStatus(dir + "print.wacc") shouldBe 0
  }

  it should "frontend analyse printBool.wacc" in {
    frontendStatus(dir + "printBool.wacc") shouldBe 0
  }

  it should "frontend analyse printChar.wacc" in {
    frontendStatus(dir + "printChar.wacc") shouldBe 0
  }

  it should "frontend analyse printCharArray.wacc" in {
    frontendStatus(dir + "printCharArray.wacc") shouldBe 0
  }

  it should "frontend analyse printCharAsString.wacc" in {
    frontendStatus(dir + "printCharAsString.wacc") shouldBe 0
  }

  it should "frontend analyse printEscChar.wacc" in {
    frontendStatus(dir + "printEscChar.wacc") shouldBe 0
  }

  it should "frontend analyse printInt.wacc" in {
    frontendStatus(dir + "printInt.wacc") shouldBe 0
  }

  it should "frontend analyse println.wacc" in {
    frontendStatus(dir + "println.wacc") shouldBe 0
  }

}
