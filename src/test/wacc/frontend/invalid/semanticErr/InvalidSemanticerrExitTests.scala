package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/exit/"

  it should "frontend analyse badCharExit.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badCharExit.wacc") shouldBe 200
  }

  it should "frontend analyse exitNonInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "exitNonInt.wacc") shouldBe 200
  }

  it should "frontend analyse globalReturn.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "globalReturn.wacc") shouldBe 200
  }

  it should "frontend analyse returnsInMain.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "returnsInMain.wacc") shouldBe 200
  }

}
