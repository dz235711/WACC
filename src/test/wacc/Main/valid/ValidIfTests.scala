package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "pass if1.wacc" in {
    runFrontend(Array(dir+"if1.wacc"))._1 shouldBe 0
  }

  it should "pass if2.wacc" in {
    runFrontend(Array(dir+"if2.wacc"))._1 shouldBe 0
  }

  it should "pass if3.wacc" in {
    runFrontend(Array(dir+"if3.wacc"))._1 shouldBe 0
  }

  it should "pass if4.wacc" in {
    runFrontend(Array(dir+"if4.wacc"))._1 shouldBe 0
  }

  it should "pass if5.wacc" in {
    runFrontend(Array(dir+"if5.wacc"))._1 shouldBe 0
  }

  it should "pass if6.wacc" in {
    runFrontend(Array(dir+"if6.wacc"))._1 shouldBe 0
  }

  it should "pass ifBasic.wacc" in {
    runFrontend(Array(dir+"ifBasic.wacc"))._1 shouldBe 0
  }

  it should "pass ifFalse.wacc" in {
    runFrontend(Array(dir+"ifFalse.wacc"))._1 shouldBe 0
  }

  it should "pass ifTrue.wacc" in {
    runFrontend(Array(dir+"ifTrue.wacc"))._1 shouldBe 0
  }

  it should "pass whitespace.wacc" in {
    runFrontend(Array(dir+"whitespace.wacc"))._1 shouldBe 0
  }

}