package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/sequence/"

  it should "frontend analyse basicSeq.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "basicSeq.wacc") shouldBe 0
  }

  it should "frontend analyse basicSeq2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "basicSeq2.wacc") shouldBe 0
  }

  it should "frontend analyse boolAssignment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "boolAssignment.wacc") shouldBe 0
  }

  it should "frontend analyse charAssignment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "charAssignment.wacc") shouldBe 0
  }

  it should "frontend analyse exitSimple.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "exitSimple.wacc") shouldBe 0
  }

  it should "frontend analyse intAssignment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "intAssignment.wacc") shouldBe 0
  }

  it should "frontend analyse intLeadingZeros.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "intLeadingZeros.wacc") shouldBe 0
  }

  it should "frontend analyse stringAssignment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "stringAssignment.wacc") shouldBe 0
  }

}
