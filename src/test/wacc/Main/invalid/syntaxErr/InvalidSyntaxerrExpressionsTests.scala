package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrExpressionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/expressions/"

  it should "pass missingOperand1.wacc" in pending /*{
    runFrontend(Array(dir+"missingOperand1.wacc"))._1 shouldBe 100
  }*/

  it should "pass missingOperand2.wacc" in pending /*{
    runFrontend(Array(dir+"missingOperand2.wacc"))._1 shouldBe 100
  }*/

  it should "pass printlnConcat.wacc" in pending /*{
    runFrontend(Array(dir+"printlnConcat.wacc"))._1 shouldBe 100
  }*/

}