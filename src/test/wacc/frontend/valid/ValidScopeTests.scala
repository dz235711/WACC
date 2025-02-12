package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "frontend analyse ifNested1.wacc" in {
    frontendStatus(dir + "ifNested1.wacc") shouldBe 0
  }

  it should "frontend analyse ifNested2.wacc" in {
    frontendStatus(dir + "ifNested2.wacc") shouldBe 0
  }

  it should "frontend analyse indentationNotImportant.wacc" in {
    frontendStatus(dir + "indentationNotImportant.wacc") shouldBe 0
  }

  it should "frontend analyse intsAndKeywords.wacc" in {
    frontendStatus(dir + "intsAndKeywords.wacc") shouldBe 0
  }

  it should "frontend analyse printAllTypes.wacc" in {
    frontendStatus(dir + "printAllTypes.wacc") shouldBe 0
  }

  it should "frontend analyse scope.wacc" in {
    frontendStatus(dir + "scope.wacc") shouldBe 0
  }

  it should "frontend analyse scopeBasic.wacc" in {
    frontendStatus(dir + "scopeBasic.wacc") shouldBe 0
  }

  it should "frontend analyse scopeIfRedefine.wacc" in {
    frontendStatus(dir + "scopeIfRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeRedefine.wacc" in {
    frontendStatus(dir + "scopeRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeSimpleRedefine.wacc" in {
    frontendStatus(dir + "scopeSimpleRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse scopeVars.wacc" in {
    frontendStatus(dir + "scopeVars.wacc") shouldBe 0
  }

  it should "frontend analyse scopeWhileNested.wacc" in {
    frontendStatus(dir + "scopeWhileNested.wacc") shouldBe 0
  }

  it should "frontend analyse scopeWhileRedefine.wacc" in {
    frontendStatus(dir + "scopeWhileRedefine.wacc") shouldBe 0
  }

  it should "frontend analyse splitScope.wacc" in {
    frontendStatus(dir + "splitScope.wacc") shouldBe 0
  }

}
