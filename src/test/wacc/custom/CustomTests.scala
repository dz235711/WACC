package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class CustomTests extends AnyFlatSpec {
  val dir = "src/test/examples/custom/"

  it should "frontend analyse spaceAfterNegate.wacc" in {
    frontendStatus(dir + "spaceAfterNegate.wacc") shouldBe 0
  }

  it should "frontend analyse nestedNegate.wacc" in {
    frontendStatus(dir + "nestedNegate.wacc") shouldBe 0
  }
}
