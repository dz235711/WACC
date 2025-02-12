package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/while/"

  it should "pass fibonacciFullIt.wacc" in {
    frontendStatus(dir + "fibonacciFullIt.wacc") shouldBe 0
  }

  it should "pass fibonacciIterative.wacc" in {
    frontendStatus(dir + "fibonacciIterative.wacc") shouldBe 0
  }

  it should "pass loopCharCondition.wacc" in {
    frontendStatus(dir + "loopCharCondition.wacc") shouldBe 0
  }

  it should "pass loopIntCondition.wacc" in {
    frontendStatus(dir + "loopIntCondition.wacc") shouldBe 0
  }

  it should "pass max.wacc" in {
    frontendStatus(dir + "max.wacc") shouldBe 0
  }

  it should "pass min.wacc" in {
    frontendStatus(dir + "min.wacc") shouldBe 0
  }

  it should "pass rmStyleAdd.wacc" in {
    frontendStatus(dir + "rmStyleAdd.wacc") shouldBe 0
  }

  it should "pass rmStyleAddIO.wacc" in {
    frontendStatus(dir + "rmStyleAddIO.wacc") shouldBe 0
  }

  it should "pass whileBasic.wacc" in {
    frontendStatus(dir + "whileBasic.wacc") shouldBe 0
  }

  it should "pass whileBoolFlip.wacc" in {
    frontendStatus(dir + "whileBoolFlip.wacc") shouldBe 0
  }

  it should "pass whileCount.wacc" in {
    frontendStatus(dir + "whileCount.wacc") shouldBe 0
  }

  it should "pass whileFalse.wacc" in {
    frontendStatus(dir + "whileFalse.wacc") shouldBe 0
  }

}
