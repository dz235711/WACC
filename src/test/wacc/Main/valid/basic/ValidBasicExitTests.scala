package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidBasicExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/exit/"

  it should "pass exit-1.wacc" in {
    runFrontend(Array(dir+"exit-1.wacc"))._1 shouldBe 0
  }

  it should "pass exitBasic.wacc" in {
    runFrontend(Array(dir+"exitBasic.wacc"))._1 shouldBe 0
  }

  it should "pass exitBasic2.wacc" in {
    runFrontend(Array(dir+"exitBasic2.wacc"))._1 shouldBe 0
  }

  it should "pass exitWrap.wacc" in {
    runFrontend(Array(dir+"exitWrap.wacc"))._1 shouldBe 0
  }

}