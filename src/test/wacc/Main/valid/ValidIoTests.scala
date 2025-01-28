package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/"

  it should "pass IOLoop.wacc" in pending /*{
    runFrontend(Array(dir+"IOLoop.wacc"))._1 shouldBe 0
  }*/

  it should "pass IOSequence.wacc" in pending /*{
    runFrontend(Array(dir+"IOSequence.wacc"))._1 shouldBe 0
  }*/

}