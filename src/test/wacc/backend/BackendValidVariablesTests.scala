package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "execute _VarNames.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + _VarNames.wacc, "") shouldBe Some("")
  }*/

  it should "execute boolDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + boolDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute boolDeclaration2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + boolDeclaration2.wacc, "") shouldBe Some("")
  }*/

  it should "execute capCharDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + capCharDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute charDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + charDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute charDeclaration2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + charDeclaration2.wacc, "") shouldBe Some("")
  }*/

  it should "execute emptyStringDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyStringDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute intDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + intDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute longVarNames.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + longVarNames.wacc, "") shouldBe Some("")
  }*/

  it should "execute manyVariables.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + manyVariables.wacc, "") shouldBe Some("")
  }*/

  it should "execute negIntDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + negIntDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute puncCharDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + puncCharDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute stringCarriageReturn.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + stringCarriageReturn.wacc, "") shouldBe Some("")
  }*/

  it should "execute stringDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + stringDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute zeroIntDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + zeroIntDeclaration.wacc, "") shouldBe Some("")
  }*/

}
