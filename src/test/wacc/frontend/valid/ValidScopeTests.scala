package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "frontend analyse ifNested1.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ifNested1.wacc") shouldBe 0
  }

  it should "frontend analyse ifNested2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "ifNested2.wacc") shouldBe 0
  }

  it should "frontend analyse indentationNotImportant.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "indentationNotImportant.wacc") shouldBe 0
  }

  it should "frontend analyse intsAndKeywords.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "intsAndKeywords.wacc") shouldBe 0
  }

  it should "frontend analyse printAllTypes.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printAllTypes.wacc") shouldBe 0
  }

  it should "frontend analyse scope.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scope.wacc") shouldBe 0
  }

  it should "frontend analyse scopeBasic.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeBasic.wacc") shouldBe 0
  }

  it should "frontend analyse scopeIfRedefine.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeIfRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeRedefine.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeSimpleRedefine.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeSimpleRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeVars.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeVars.wacc") shouldBe 0
  }

  it should "frontend analyse scopeWhileNested.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeWhileNested.wacc") shouldBe 0
  }

  it should "frontend analyse scopeWhileRedefine.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "scopeWhileRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse splitScope.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "splitScope.wacc") shouldBe 0
  }

}
