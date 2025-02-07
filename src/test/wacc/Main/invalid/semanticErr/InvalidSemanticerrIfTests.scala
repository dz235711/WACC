package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrIfTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/if/"

  it should "pass ifIntCondition.wacc" in {
    runFrontend(Array(dir + "ifIntCondition.wacc"))._1 shouldBe 200
  }

}
