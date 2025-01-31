package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "pass ifNested1.wacc" in {
    runFrontend(Array(dir+"ifNested1.wacc"))._1 shouldBe 0
  }

  it should "pass ifNested2.wacc" in {
    runFrontend(Array(dir+"ifNested2.wacc"))._1 shouldBe 0
  }

  it should "pass indentationNotImportant.wacc" in {
    runFrontend(Array(dir+"indentationNotImportant.wacc"))._1 shouldBe 0
  }

  it should "pass intsAndKeywords.wacc" in {
    runFrontend(Array(dir+"intsAndKeywords.wacc"))._1 shouldBe 0
  }

  it should "pass printAllTypes.wacc" in {
    runFrontend(Array(dir+"printAllTypes.wacc"))._1 shouldBe 0
  }

  it should "pass scope.wacc" in {
    runFrontend(Array(dir+"scope.wacc"))._1 shouldBe 0
  }

  it should "pass scopeBasic.wacc" in {
    runFrontend(Array(dir+"scopeBasic.wacc"))._1 shouldBe 0
  }

  it should "pass scopeIfRedefine.wacc" in {
    runFrontend(Array(dir+"scopeIfRedefine.wacc"))._1 shouldBe 0
  }

  it should "pass scopeRedefine.wacc" in {
    runFrontend(Array(dir+"scopeRedefine.wacc"))._1 shouldBe 0
  }

  it should "pass scopeSimpleRedefine.wacc" in {
    runFrontend(Array(dir+"scopeSimpleRedefine.wacc"))._1 shouldBe 0
  }

  it should "pass scopeVars.wacc" in {
    runFrontend(Array(dir+"scopeVars.wacc"))._1 shouldBe 0
  }

  it should "pass scopeWhileNested.wacc" in {
    runFrontend(Array(dir+"scopeWhileNested.wacc"))._1 shouldBe 0
  }

  it should "pass scopeWhileRedefine.wacc" in {
    runFrontend(Array(dir+"scopeWhileRedefine.wacc"))._1 shouldBe 0
  }

  it should "pass splitScope.wacc" in {
    runFrontend(Array(dir+"splitScope.wacc"))._1 shouldBe 0
  }

}