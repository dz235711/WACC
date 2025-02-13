package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "execute comment.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + comment.wacc, "") shouldBe Some("")
  }*/

  it should "execute commentEoF.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + commentEoF.wacc, "") shouldBe Some("")
  }*/

  it should "execute commentInLine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + commentInLine.wacc, "") shouldBe Some("")
  }*/

  it should "execute skip.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + skip.wacc, "") shouldBe Some("")
  }*/

}
