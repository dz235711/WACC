package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/sequence/"

  it should "correctly execute basicSeq.wacc" in pending /*{
    fullExec(dir + basicSeq.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute basicSeq2.wacc" in pending /*{
    fullExec(dir + basicSeq2.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute boolAssignment.wacc" in pending /*{
    fullExec(dir + boolAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute charAssignment.wacc" in pending /*{
    fullExec(dir + charAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute exitSimple.wacc" in pending /*{
    fullExec(dir + exitSimple.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute intAssignment.wacc" in pending /*{
    fullExec(dir + intAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute intLeadingZeros.wacc" in pending /*{
    fullExec(dir + intLeadingZeros.wacc, "") shouldBe Some("42\n0\n")
  }*/

  it should "correctly execute stringAssignment.wacc" in pending /*{
    fullExec(dir + stringAssignment.wacc, "") shouldBe Some("")
  }*/

}