package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/variables/"

  it should "pass basicTypeErr01.wacc" in {
    frontendStatus(dir + "basicTypeErr01.wacc") shouldBe 200
  }

  it should "pass basicTypeErr02.wacc" in {
    frontendStatus(dir + "basicTypeErr02.wacc") shouldBe 200
  }

  it should "pass basicTypeErr03.wacc" in {
    frontendStatus(dir + "basicTypeErr03.wacc") shouldBe 200
  }

  it should "pass basicTypeErr04.wacc" in {
    frontendStatus(dir + "basicTypeErr04.wacc") shouldBe 200
  }

  it should "pass basicTypeErr05.wacc" in {
    frontendStatus(dir + "basicTypeErr05.wacc") shouldBe 200
  }

  it should "pass basicTypeErr06.wacc" in {
    frontendStatus(dir + "basicTypeErr06.wacc") shouldBe 200
  }

  it should "pass basicTypeErr07.wacc" in {
    frontendStatus(dir + "basicTypeErr07.wacc") shouldBe 200
  }

  it should "pass basicTypeErr08.wacc" in {
    frontendStatus(dir + "basicTypeErr08.wacc") shouldBe 200
  }

  it should "pass basicTypeErr09.wacc" in {
    frontendStatus(dir + "basicTypeErr09.wacc") shouldBe 200
  }

  it should "pass basicTypeErr10.wacc" in {
    frontendStatus(dir + "basicTypeErr10.wacc") shouldBe 200
  }

  it should "pass basicTypeErr11.wacc" in {
    frontendStatus(dir + "basicTypeErr11.wacc") shouldBe 200
  }

  it should "pass basicTypeErr12.wacc" in {
    frontendStatus(dir + "basicTypeErr12.wacc") shouldBe 200
  }

  it should "pass caseMatters.wacc" in {
    frontendStatus(dir + "caseMatters.wacc") shouldBe 200
  }

  it should "pass doubleDeclare.wacc" in {
    frontendStatus(dir + "doubleDeclare.wacc") shouldBe 200
  }

  it should "pass undeclaredScopeVar.wacc" in {
    frontendStatus(dir + "undeclaredScopeVar.wacc") shouldBe 200
  }

  it should "pass undeclaredVar.wacc" in {
    frontendStatus(dir + "undeclaredVar.wacc") shouldBe 200
  }

  it should "pass undeclaredVarAccess.wacc" in {
    frontendStatus(dir + "undeclaredVarAccess.wacc") shouldBe 200
  }

}
