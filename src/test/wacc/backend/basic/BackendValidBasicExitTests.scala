package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidBasicExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/exit/"

  it should "execute exit-1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + exit-1.wacc, "") shouldBe Some("")
  }*/

  it should "execute exitBasic.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + exitBasic.wacc, "") shouldBe Some("")
  }*/

  it should "execute exitBasic2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + exitBasic2.wacc, "") shouldBe Some("")
  }*/

  it should "execute exitWrap.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + exitWrap.wacc, "") shouldBe Some("")
  }*/

}
