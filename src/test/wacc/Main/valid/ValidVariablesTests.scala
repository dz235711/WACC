package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/variables/"

  it should "pass _VarNames.wacc" in pending /*{
    runFrontend(Array(dir+"_VarNames.wacc"))._1 shouldBe 0
  }*/

  it should "pass boolDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"boolDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass boolDeclaration2.wacc" in pending /*{
    runFrontend(Array(dir+"boolDeclaration2.wacc"))._1 shouldBe 0
  }*/

  it should "pass capCharDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"capCharDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass charDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"charDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass charDeclaration2.wacc" in pending /*{
    runFrontend(Array(dir+"charDeclaration2.wacc"))._1 shouldBe 0
  }*/

  it should "pass emptyStringDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"emptyStringDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass intDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"intDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass longVarNames.wacc" in pending /*{
    runFrontend(Array(dir+"longVarNames.wacc"))._1 shouldBe 0
  }*/

  it should "pass manyVariables.wacc" in pending /*{
    runFrontend(Array(dir+"manyVariables.wacc"))._1 shouldBe 0
  }*/

  it should "pass negIntDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"negIntDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass puncCharDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"puncCharDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass stringCarriageReturn.wacc" in pending /*{
    runFrontend(Array(dir+"stringCarriageReturn.wacc"))._1 shouldBe 0
  }*/

  it should "pass stringDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"stringDeclaration.wacc"))._1 shouldBe 0
  }*/

  it should "pass zeroIntDeclaration.wacc" in pending /*{
    runFrontend(Array(dir+"zeroIntDeclaration.wacc"))._1 shouldBe 0
  }*/

}