package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/sequence/"

  it should "pass basicSeq.wacc" in {
    runFrontend(Array(dir + "basicSeq.wacc"))._1 shouldBe 0
  }

  it should "pass basicSeq2.wacc" in {
    runFrontend(Array(dir + "basicSeq2.wacc"))._1 shouldBe 0
  }

  it should "pass boolAssignment.wacc" in {
    runFrontend(Array(dir + "boolAssignment.wacc"))._1 shouldBe 0
  }

  it should "pass charAssignment.wacc" in {
    runFrontend(Array(dir + "charAssignment.wacc"))._1 shouldBe 0
  }

  it should "pass exitSimple.wacc" in {
    runFrontend(Array(dir + "exitSimple.wacc"))._1 shouldBe 0
  }

  it should "pass intAssignment.wacc" in {
    runFrontend(Array(dir + "intAssignment.wacc"))._1 shouldBe 0
  }

  it should "pass intLeadingZeros.wacc" in {
    runFrontend(Array(dir + "intLeadingZeros.wacc"))._1 shouldBe 0
  }

  it should "pass stringAssignment.wacc" in {
    runFrontend(Array(dir + "stringAssignment.wacc"))._1 shouldBe 0
  }

}
