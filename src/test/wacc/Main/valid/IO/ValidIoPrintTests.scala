package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/print/"

  it should "pass hashInProgram.wacc" in pending /*{
    runFrontend(Array(dir+"hashInProgram.wacc"))._1 shouldBe 0
  }*/

  it should "pass multipleStringsAssignment.wacc" in pending /*{
    runFrontend(Array(dir+"multipleStringsAssignment.wacc"))._1 shouldBe 0
  }*/

  it should "pass print-backspace.wacc" in pending /*{
    runFrontend(Array(dir+"print-backspace.wacc"))._1 shouldBe 0
  }*/

  it should "pass print.wacc" in pending /*{
    runFrontend(Array(dir+"print.wacc"))._1 shouldBe 0
  }*/

  it should "pass printBool.wacc" in pending /*{
    runFrontend(Array(dir+"printBool.wacc"))._1 shouldBe 0
  }*/

  it should "pass printChar.wacc" in pending /*{
    runFrontend(Array(dir+"printChar.wacc"))._1 shouldBe 0
  }*/

  it should "pass printCharArray.wacc" in pending /*{
    runFrontend(Array(dir+"printCharArray.wacc"))._1 shouldBe 0
  }*/

  it should "pass printCharAsString.wacc" in pending /*{
    runFrontend(Array(dir+"printCharAsString.wacc"))._1 shouldBe 0
  }*/

  it should "pass printEscChar.wacc" in pending /*{
    runFrontend(Array(dir+"printEscChar.wacc"))._1 shouldBe 0
  }*/

  it should "pass printInt.wacc" in pending /*{
    runFrontend(Array(dir+"printInt.wacc"))._1 shouldBe 0
  }*/

  it should "pass println.wacc" in pending /*{
    runFrontend(Array(dir+"println.wacc"))._1 shouldBe 0
  }*/

}