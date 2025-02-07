package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/variables/"

  it should "pass basicTypeErr01.wacc" in {
    runFrontend(Array(dir + "basicTypeErr01.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr02.wacc" in {
    runFrontend(Array(dir + "basicTypeErr02.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr03.wacc" in {
    runFrontend(Array(dir + "basicTypeErr03.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr04.wacc" in {
    runFrontend(Array(dir + "basicTypeErr04.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr05.wacc" in {
    runFrontend(Array(dir + "basicTypeErr05.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr06.wacc" in {
    runFrontend(Array(dir + "basicTypeErr06.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr07.wacc" in {
    runFrontend(Array(dir + "basicTypeErr07.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr08.wacc" in {
    runFrontend(Array(dir + "basicTypeErr08.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr09.wacc" in {
    runFrontend(Array(dir + "basicTypeErr09.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr10.wacc" in {
    runFrontend(Array(dir + "basicTypeErr10.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr11.wacc" in {
    runFrontend(Array(dir + "basicTypeErr11.wacc"))._1 shouldBe 200
  }

  it should "pass basicTypeErr12.wacc" in {
    runFrontend(Array(dir + "basicTypeErr12.wacc"))._1 shouldBe 200
  }

  it should "pass caseMatters.wacc" in {
    runFrontend(Array(dir + "caseMatters.wacc"))._1 shouldBe 200
  }

  it should "pass doubleDeclare.wacc" in {
    runFrontend(Array(dir + "doubleDeclare.wacc"))._1 shouldBe 200
  }

  it should "pass undeclaredScopeVar.wacc" in {
    runFrontend(Array(dir + "undeclaredScopeVar.wacc"))._1 shouldBe 200
  }

  it should "pass undeclaredVar.wacc" in {
    runFrontend(Array(dir + "undeclaredVar.wacc"))._1 shouldBe 200
  }

  it should "pass undeclaredVarAccess.wacc" in {
    runFrontend(Array(dir + "undeclaredVarAccess.wacc"))._1 shouldBe 200
  }

}
