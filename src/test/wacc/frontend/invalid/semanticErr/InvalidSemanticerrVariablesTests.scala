package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/variables/"

  it should "frontend analyse basicTypeErr01.wacc" in {
    frontendStatus(dir + "basicTypeErr01.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr02.wacc" in {
    frontendStatus(dir + "basicTypeErr02.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr03.wacc" in {
    frontendStatus(dir + "basicTypeErr03.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr04.wacc" in {
    frontendStatus(dir + "basicTypeErr04.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr05.wacc" in {
    frontendStatus(dir + "basicTypeErr05.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr06.wacc" in {
    frontendStatus(dir + "basicTypeErr06.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr07.wacc" in {
    frontendStatus(dir + "basicTypeErr07.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr08.wacc" in {
    frontendStatus(dir + "basicTypeErr08.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr09.wacc" in {
    frontendStatus(dir + "basicTypeErr09.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr10.wacc" in {
    frontendStatus(dir + "basicTypeErr10.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr11.wacc" in {
    frontendStatus(dir + "basicTypeErr11.wacc") shouldBe 200
  }

  it should "frontend analyse basicTypeErr12.wacc" in {
    frontendStatus(dir + "basicTypeErr12.wacc") shouldBe 200
  }

  it should "frontend analyse caseMatters.wacc" in {
    frontendStatus(dir + "caseMatters.wacc") shouldBe 200
  }

  it should "frontend analyse doubleDeclare.wacc" in {
    frontendStatus(dir + "doubleDeclare.wacc") shouldBe 200
  }

  it should "frontend analyse undeclaredScopeVar.wacc" in {
    frontendStatus(dir + "undeclaredScopeVar.wacc") shouldBe 200
  }

  it should "frontend analyse undeclaredVar.wacc" in {
    frontendStatus(dir + "undeclaredVar.wacc") shouldBe 200
  }

  it should "frontend analyse undeclaredVarAccess.wacc" in {
    frontendStatus(dir + "undeclaredVarAccess.wacc") shouldBe 200
  }

}
