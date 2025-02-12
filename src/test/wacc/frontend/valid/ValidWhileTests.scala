package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/while/"

  it should "frontend analyse fibonacciFullIt.wacc" in {
    frontendStatus(dir + "fibonacciFullIt.wacc") shouldBe 0
  }

  it should "frontend analyse fibonacciIterative.wacc" in {
    frontendStatus(dir + "fibonacciIterative.wacc") shouldBe 0
  }

  it should "frontend analyse loopCharCondition.wacc" in {
    frontendStatus(dir + "loopCharCondition.wacc") shouldBe 0
  }

  it should "frontend analyse loopIntCondition.wacc" in {
    frontendStatus(dir + "loopIntCondition.wacc") shouldBe 0
  }

  it should "frontend analyse max.wacc" in {
    frontendStatus(dir + "max.wacc") shouldBe 0
  }

  it should "frontend analyse min.wacc" in {
    frontendStatus(dir + "min.wacc") shouldBe 0
  }

  it should "frontend analyse rmStyleAdd.wacc" in {
    frontendStatus(dir + "rmStyleAdd.wacc") shouldBe 0
  }

  it should "frontend analyse rmStyleAddIO.wacc" in {
    frontendStatus(dir + "rmStyleAddIO.wacc") shouldBe 0
  }

  it should "frontend analyse whileBasic.wacc" in {
    frontendStatus(dir + "whileBasic.wacc") shouldBe 0
  }

  it should "frontend analyse whileBoolFlip.wacc" in {
    frontendStatus(dir + "whileBoolFlip.wacc") shouldBe 0
  }

  it should "frontend analyse whileCount.wacc" in {
    frontendStatus(dir + "whileCount.wacc") shouldBe 0
  }

  it should "frontend analyse whileFalse.wacc" in {
    frontendStatus(dir + "whileFalse.wacc") shouldBe 0
  }

}
