package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrBadcharTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/badChar/"

  it should "pass negativeChr.wacc" in pending /*{
    runFrontend(Array(dir+"negativeChr.wacc"))._1 shouldBe 0
  }*/

  it should "pass tooBigChr.wacc" in pending /*{
    runFrontend(Array(dir+"tooBigChr.wacc"))._1 shouldBe 0
  }*/

}