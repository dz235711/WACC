package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "frontend analyse _VarNames.wacc" taggedAs Frontend in {
    frontendStatus(dir + "_VarNames.wacc") shouldBe 0
  }

  it should "frontend analyse boolDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "boolDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse boolDeclaration2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "boolDeclaration2.wacc") shouldBe 0
  }

  it should "frontend analyse capCharDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "capCharDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse charDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "charDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse charDeclaration2.wacc" taggedAs Frontend in {
    frontendStatus(dir + "charDeclaration2.wacc") shouldBe 0
  }

  it should "frontend analyse emptyStringDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyStringDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse intDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "intDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse longVarNames.wacc" taggedAs Frontend in {
    frontendStatus(dir + "longVarNames.wacc") shouldBe 0
  }

  it should "frontend analyse manyVariables.wacc" taggedAs Frontend in {
    frontendStatus(dir + "manyVariables.wacc") shouldBe 0
  }

  it should "frontend analyse negIntDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "negIntDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse puncCharDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "puncCharDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse stringCarriageReturn.wacc" taggedAs Frontend in {
    frontendStatus(dir + "stringCarriageReturn.wacc") shouldBe 0
  }

  it should "frontend analyse stringDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "stringDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse zeroIntDeclaration.wacc" taggedAs Frontend in {
    frontendStatus(dir + "zeroIntDeclaration.wacc") shouldBe 0
  }

}
