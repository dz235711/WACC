package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/if/"

  it should "execute if1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if1.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute if2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if2.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute if3.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if3.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute if4.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if4.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute if5.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if5.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute if6.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + if6.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute ifBasic.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + ifBasic.wacc, "") shouldBe Some("")
  }*/

  it should "execute ifFalse.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + ifFalse.wacc, "") shouldBe Some("here\n")
  }*/

  it should "execute ifTrue.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + ifTrue.wacc, "") shouldBe Some("here\n")
  }*/

  it should "execute whitespace.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + whitespace.wacc, "") shouldBe Some("1\n")
  }*/

}
