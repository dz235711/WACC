package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/sequence/"

  it should "execute basicSeq.wacc" in pending /*{
    fullExec(dir + basicSeq.wacc, "") shouldBe Some("")
  }*/

  it should "execute basicSeq2.wacc" in pending /*{
    fullExec(dir + basicSeq2.wacc, "") shouldBe Some("")
  }*/

  it should "execute boolAssignment.wacc" in pending /*{
    fullExec(dir + boolAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "execute charAssignment.wacc" in pending /*{
    fullExec(dir + charAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "execute exitSimple.wacc" in pending /*{
    fullExec(dir + exitSimple.wacc, "") shouldBe Some("")
  }*/

  it should "execute intAssignment.wacc" in pending /*{
    fullExec(dir + intAssignment.wacc, "") shouldBe Some("")
  }*/

  it should "execute intLeadingZeros.wacc" in pending /*{
    fullExec(dir + intLeadingZeros.wacc, "") shouldBe Some("42\n0\n")
  }*/

  it should "execute stringAssignment.wacc" in pending /*{
    fullExec(dir + stringAssignment.wacc, "") shouldBe Some("")
  }*/

}
