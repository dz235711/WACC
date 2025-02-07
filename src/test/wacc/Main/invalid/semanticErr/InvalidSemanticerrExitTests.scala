package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrExitTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/exit/"

  it should "pass badCharExit.wacc" in {
    runFrontend(Array(dir + "badCharExit.wacc"))._1 shouldBe 200
  }

  it should "pass exitNonInt.wacc" in {
    runFrontend(Array(dir + "exitNonInt.wacc"))._1 shouldBe 200
  }

  it should "pass globalReturn.wacc" in {
    runFrontend(Array(dir + "globalReturn.wacc"))._1 shouldBe 200
  }

  it should "pass returnsInMain.wacc" in {
    runFrontend(Array(dir + "returnsInMain.wacc"))._1 shouldBe 200
  }

}
