package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "pass ifNested1.wacc" in {
    frontendStatus(dir + "ifNested1.wacc") shouldBe 0
  }

  it should "pass ifNested2.wacc" in {
    frontendStatus(dir + "ifNested2.wacc") shouldBe 0
  }

  it should "pass indentationNotImportant.wacc" in {
    frontendStatus(dir + "indentationNotImportant.wacc") shouldBe 0
  }

  it should "pass intsAndKeywords.wacc" in {
    frontendStatus(dir + "intsAndKeywords.wacc") shouldBe 0
  }

  it should "pass printAllTypes.wacc" in {
    frontendStatus(dir + "printAllTypes.wacc") shouldBe 0
  }

  it should "pass scope.wacc" in {
    frontendStatus(dir + "scope.wacc") shouldBe 0
  }

  it should "pass scopeBasic.wacc" in {
    frontendStatus(dir + "scopeBasic.wacc") shouldBe 0
  }

  it should "pass scopeIfRedefine.wacc" in {
    frontendStatus(dir + "scopeIfRedefine.wacc") shouldBe 0
  }

  it should "pass scopeRedefine.wacc" in {
    frontendStatus(dir + "scopeRedefine.wacc") shouldBe 0
  }

  it should "pass scopeSimpleRedefine.wacc" in {
    frontendStatus(dir + "scopeSimpleRedefine.wacc") shouldBe 0
  }

  it should "pass scopeVars.wacc" in {
    frontendStatus(dir + "scopeVars.wacc") shouldBe 0
  }

  it should "pass scopeWhileNested.wacc" in {
    frontendStatus(dir + "scopeWhileNested.wacc") shouldBe 0
  }

  it should "pass scopeWhileRedefine.wacc" in {
    frontendStatus(dir + "scopeWhileRedefine.wacc") shouldBe 0
  }

  it should "pass splitScope.wacc" in {
    frontendStatus(dir + "splitScope.wacc") shouldBe 0
  }

}
