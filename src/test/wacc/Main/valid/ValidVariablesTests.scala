package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "pass _VarNames.wacc" in {
    frontendStatus(dir + "_VarNames.wacc") shouldBe 0
  }

  it should "pass boolDeclaration.wacc" in {
    frontendStatus(dir + "boolDeclaration.wacc") shouldBe 0
  }

  it should "pass boolDeclaration2.wacc" in {
    frontendStatus(dir + "boolDeclaration2.wacc") shouldBe 0
  }

  it should "pass capCharDeclaration.wacc" in {
    frontendStatus(dir + "capCharDeclaration.wacc") shouldBe 0
  }

  it should "pass charDeclaration.wacc" in {
    frontendStatus(dir + "charDeclaration.wacc") shouldBe 0
  }

  it should "pass charDeclaration2.wacc" in {
    frontendStatus(dir + "charDeclaration2.wacc") shouldBe 0
  }

  it should "pass emptyStringDeclaration.wacc" in {
    frontendStatus(dir + "emptyStringDeclaration.wacc") shouldBe 0
  }

  it should "pass intDeclaration.wacc" in {
    frontendStatus(dir + "intDeclaration.wacc") shouldBe 0
  }

  it should "pass longVarNames.wacc" in {
    frontendStatus(dir + "longVarNames.wacc") shouldBe 0
  }

  it should "pass manyVariables.wacc" in {
    frontendStatus(dir + "manyVariables.wacc") shouldBe 0
  }

  it should "pass negIntDeclaration.wacc" in {
    frontendStatus(dir + "negIntDeclaration.wacc") shouldBe 0
  }

  it should "pass puncCharDeclaration.wacc" in {
    frontendStatus(dir + "puncCharDeclaration.wacc") shouldBe 0
  }

  it should "pass stringCarriageReturn.wacc" in {
    frontendStatus(dir + "stringCarriageReturn.wacc") shouldBe 0
  }

  it should "pass stringDeclaration.wacc" in {
    frontendStatus(dir + "stringDeclaration.wacc") shouldBe 0
  }

  it should "pass zeroIntDeclaration.wacc" in {
    frontendStatus(dir + "zeroIntDeclaration.wacc") shouldBe 0
  }

}
