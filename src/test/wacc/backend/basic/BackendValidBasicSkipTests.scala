package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidBasicSkipTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/basic/skip/"

  it should "execute comment.wacc" in pending /*{
    fullExec(dir + comment.wacc, "") shouldBe Some("")
  }*/

  it should "execute commentEoF.wacc" in pending /*{
    fullExec(dir + commentEoF.wacc, "") shouldBe Some("")
  }*/

  it should "execute commentInLine.wacc" in pending /*{
    fullExec(dir + commentInLine.wacc, "") shouldBe Some("")
  }*/

  it should "execute skip.wacc" in pending /*{
    fullExec(dir + skip.wacc, "") shouldBe Some("")
  }*/

}
