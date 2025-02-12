package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/"

  it should "frontend analyse IOLoop.wacc" in {
    frontendStatus(dir + "IOLoop.wacc") shouldBe 0
  }

  it should "frontend analyse IOSequence.wacc" in {
    frontendStatus(dir + "IOSequence.wacc") shouldBe 0
  }

}
