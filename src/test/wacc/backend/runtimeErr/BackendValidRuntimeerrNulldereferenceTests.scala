package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "correctly execute freeNull.wacc" in pending /*{
    fullExec(dir + freeNull.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute readNull1.wacc" in pending /*{
    fullExec(dir + readNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute readNull2.wacc" in pending /*{
    fullExec(dir + readNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute setNull1.wacc" in pending /*{
    fullExec(dir + setNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute setNull2.wacc" in pending /*{
    fullExec(dir + setNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute useNull1.wacc" in pending /*{
    fullExec(dir + useNull1.wacc, "") shouldBe Some("#runtime_error#")
  }*/

  it should "correctly execute useNull2.wacc" in pending /*{
    fullExec(dir + useNull2.wacc, "") shouldBe Some("#runtime_error#")
  }*/

}