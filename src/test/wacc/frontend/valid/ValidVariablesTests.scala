package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "frontend analyse _VarNames.wacc" in {
    frontendStatus(dir + "_VarNames.wacc") shouldBe 0
  }

  it should "frontend analyse boolDeclaration.wacc" in {
    frontendStatus(dir + "boolDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse boolDeclaration2.wacc" in {
    frontendStatus(dir + "boolDeclaration2.wacc") shouldBe 0
  }

  it should "frontend analyse capCharDeclaration.wacc" in {
    frontendStatus(dir + "capCharDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse charDeclaration.wacc" in {
    frontendStatus(dir + "charDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse charDeclaration2.wacc" in {
    frontendStatus(dir + "charDeclaration2.wacc") shouldBe 0
  }

  it should "frontend analyse emptyStringDeclaration.wacc" in {
    frontendStatus(dir + "emptyStringDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse intDeclaration.wacc" in {
    frontendStatus(dir + "intDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse longVarNames.wacc" in {
    frontendStatus(dir + "longVarNames.wacc") shouldBe 0
  }

  it should "frontend analyse manyVariables.wacc" in {
    frontendStatus(dir + "manyVariables.wacc") shouldBe 0
  }

  it should "frontend analyse negIntDeclaration.wacc" in {
    frontendStatus(dir + "negIntDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse puncCharDeclaration.wacc" in {
    frontendStatus(dir + "puncCharDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse stringCarriageReturn.wacc" in {
    frontendStatus(dir + "stringCarriageReturn.wacc") shouldBe 0
  }

  it should "frontend analyse stringDeclaration.wacc" in {
    frontendStatus(dir + "stringDeclaration.wacc") shouldBe 0
  }

  it should "frontend analyse zeroIntDeclaration.wacc" in {
    frontendStatus(dir + "zeroIntDeclaration.wacc") shouldBe 0
  }

}
