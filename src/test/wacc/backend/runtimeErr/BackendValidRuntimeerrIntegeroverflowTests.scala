package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrIntegeroverflowTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/integerOverflow/"

  it should "execute intJustOverflow.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intJustOverflow.wacc", "") shouldBe Some("2147483646\n2147483647\n#runtime_error#")
  }*/

  it should "execute intUnderflow.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intUnderflow.wacc", "") shouldBe Some("-2147483647\n-2147483648\n#runtime_error#")
  }*/

  it should "execute intWayOverflow.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intWayOverflow.wacc", "") shouldBe Some("2000000000\n#runtime_error#")
  }*/

  it should "execute intmultOverflow.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intmultOverflow.wacc", "") shouldBe Some("2147483\n2147483000\n#runtime_error#")
  }*/

  it should "execute intnegateOverflow.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intnegateOverflow.wacc", "") shouldBe Some("-2147483648\n#runtime_error#")
  }*/

  it should "execute intnegateOverflow2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intnegateOverflow2.wacc", "") shouldBe Some("-2147483648\n#runtime_error#")
  }*/

  it should "execute intnegateOverflow3.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intnegateOverflow3.wacc", "") shouldBe Some("-20000\n#runtime_error#")
  }*/

  it should "execute intnegateOverflow4.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "intnegateOverflow4.wacc", "") shouldBe Some("-2000000000\n#runtime_error#")
  }*/

}
