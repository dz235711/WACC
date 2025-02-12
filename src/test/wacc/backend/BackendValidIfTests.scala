package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "correctly execute if1.wacc" in pending /*{
    fullExec(dir + if1.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute if2.wacc" in pending /*{
    fullExec(dir + if2.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute if3.wacc" in pending /*{
    fullExec(dir + if3.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute if4.wacc" in pending /*{
    fullExec(dir + if4.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute if5.wacc" in pending /*{
    fullExec(dir + if5.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute if6.wacc" in pending /*{
    fullExec(dir + if6.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute ifBasic.wacc" in pending /*{
    fullExec(dir + ifBasic.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute ifFalse.wacc" in pending /*{
    fullExec(dir + ifFalse.wacc, "") shouldBe Some("here\n")
  }*/

  it should "correctly execute ifTrue.wacc" in pending /*{
    fullExec(dir + ifTrue.wacc, "") shouldBe Some("here\n")
  }*/

  it should "correctly execute whitespace.wacc" in pending /*{
    fullExec(dir + whitespace.wacc, "") shouldBe Some("1\n")
  }*/

}