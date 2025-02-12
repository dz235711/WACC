package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/"

  it should "pass IOLoop.wacc" in {
    frontendStatus(dir + "IOLoop.wacc") shouldBe 0
  }

  it should "pass IOSequence.wacc" in {
    frontendStatus(dir + "IOSequence.wacc") shouldBe 0
  }

}
