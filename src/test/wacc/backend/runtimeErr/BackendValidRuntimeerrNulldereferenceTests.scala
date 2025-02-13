package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "execute freeNull.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + freeNull.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute readNull1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + readNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute readNull2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + readNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute setNull1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + setNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute setNull2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + setNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute useNull1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + useNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "execute useNull2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + useNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

}
