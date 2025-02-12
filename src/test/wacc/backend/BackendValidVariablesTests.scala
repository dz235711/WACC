package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "correctly execute _VarNames.wacc" in pending /*{
    fullExec(dir + _VarNames.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute boolDeclaration.wacc" in pending /*{
    fullExec(dir + boolDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute boolDeclaration2.wacc" in pending /*{
    fullExec(dir + boolDeclaration2.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute capCharDeclaration.wacc" in pending /*{
    fullExec(dir + capCharDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute charDeclaration.wacc" in pending /*{
    fullExec(dir + charDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute charDeclaration2.wacc" in pending /*{
    fullExec(dir + charDeclaration2.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute emptyStringDeclaration.wacc" in pending /*{
    fullExec(dir + emptyStringDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute intDeclaration.wacc" in pending /*{
    fullExec(dir + intDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute longVarNames.wacc" in pending /*{
    fullExec(dir + longVarNames.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute manyVariables.wacc" in pending /*{
    fullExec(dir + manyVariables.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute negIntDeclaration.wacc" in pending /*{
    fullExec(dir + negIntDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute puncCharDeclaration.wacc" in pending /*{
    fullExec(dir + puncCharDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute stringCarriageReturn.wacc" in pending /*{
    fullExec(dir + stringCarriageReturn.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute stringDeclaration.wacc" in pending /*{
    fullExec(dir + stringDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute zeroIntDeclaration.wacc" in pending /*{
    fullExec(dir + zeroIntDeclaration.wacc, "") shouldBe Some("")
  }*/

}