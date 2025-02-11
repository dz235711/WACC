package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/print/"

  it should "pass hashInProgram.wacc" in {
    frontendStatus(dir + "hashInProgram.wacc") shouldBe 0
  }

  it should "pass multipleStringsAssignment.wacc" in {
    frontendStatus(dir + "multipleStringsAssignment.wacc") shouldBe 0
  }

  it should "pass print-backspace.wacc" in {
    frontendStatus(dir + "print-backspace.wacc") shouldBe 0
  }

  it should "pass print.wacc" in {
    frontendStatus(dir + "print.wacc") shouldBe 0
  }

  it should "pass printBool.wacc" in {
    frontendStatus(dir + "printBool.wacc") shouldBe 0
  }

  it should "pass printChar.wacc" in {
    frontendStatus(dir + "printChar.wacc") shouldBe 0
  }

  it should "pass printCharArray.wacc" in {
    frontendStatus(dir + "printCharArray.wacc") shouldBe 0
  }

  it should "pass printCharAsString.wacc" in {
    frontendStatus(dir + "printCharAsString.wacc") shouldBe 0
  }

  it should "pass printEscChar.wacc" in {
    frontendStatus(dir + "printEscChar.wacc") shouldBe 0
  }

  it should "pass printInt.wacc" in {
    frontendStatus(dir + "printInt.wacc") shouldBe 0
  }

  it should "pass println.wacc" in {
    frontendStatus(dir + "println.wacc") shouldBe 0
  }

}
