package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrBadcharTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/badChar/"

  it should "execute negativeChr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "negativeChr.wacc", "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute tooBigChr.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "tooBigChr.wacc", "") shouldBe Some("#runtime_error#")
  }*/

}
