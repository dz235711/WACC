package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/exit/"

  it should "pass badCharExit.wacc" in {
    frontendStatus(dir + "badCharExit.wacc") shouldBe 200
  }

  it should "pass exitNonInt.wacc" in {
    frontendStatus(dir + "exitNonInt.wacc") shouldBe 200
  }

  it should "pass globalReturn.wacc" in {
    frontendStatus(dir + "globalReturn.wacc") shouldBe 200
  }

  it should "pass returnsInMain.wacc" in {
    frontendStatus(dir + "returnsInMain.wacc") shouldBe 200
  }

}
