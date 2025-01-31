package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidWhileTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/while/"

  it should "pass fibonacciFullIt.wacc" in {
    runFrontend(Array(dir+"fibonacciFullIt.wacc"))._1 shouldBe 0
  }

  it should "pass fibonacciIterative.wacc" in {
    runFrontend(Array(dir+"fibonacciIterative.wacc"))._1 shouldBe 0
  }

  it should "pass loopCharCondition.wacc" in {
    runFrontend(Array(dir+"loopCharCondition.wacc"))._1 shouldBe 0
  }

  it should "pass loopIntCondition.wacc" in {
    runFrontend(Array(dir+"loopIntCondition.wacc"))._1 shouldBe 0
  }

  it should "pass max.wacc" in {
    runFrontend(Array(dir+"max.wacc"))._1 shouldBe 0
  }

  it should "pass min.wacc" in {
    runFrontend(Array(dir+"min.wacc"))._1 shouldBe 0
  }

  it should "pass rmStyleAdd.wacc" in {
    runFrontend(Array(dir+"rmStyleAdd.wacc"))._1 shouldBe 0
  }

  it should "pass rmStyleAddIO.wacc" in {
    runFrontend(Array(dir+"rmStyleAddIO.wacc"))._1 shouldBe 0
  }

  it should "pass whileBasic.wacc" in {
    runFrontend(Array(dir+"whileBasic.wacc"))._1 shouldBe 0
  }

  it should "pass whileBoolFlip.wacc" in {
    runFrontend(Array(dir+"whileBoolFlip.wacc"))._1 shouldBe 0
  }

  it should "pass whileCount.wacc" in {
    runFrontend(Array(dir+"whileCount.wacc"))._1 shouldBe 0
  }

  it should "pass whileFalse.wacc" in {
    runFrontend(Array(dir+"whileFalse.wacc"))._1 shouldBe 0
  }

}