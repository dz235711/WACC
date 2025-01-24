package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrIntegeroverflowTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/integerOverflow/"

  it should "pass intJustOverflow.wacc" in pending /*{
    runFrontend(Array(dir+"intJustOverflow.wacc"))._1 shouldBe 0
  }*/

  it should "pass intUnderflow.wacc" in pending /*{
    runFrontend(Array(dir+"intUnderflow.wacc"))._1 shouldBe 0
  }*/

  it should "pass intWayOverflow.wacc" in pending /*{
    runFrontend(Array(dir+"intWayOverflow.wacc"))._1 shouldBe 0
  }*/

  it should "pass intmultOverflow.wacc" in pending /*{
    runFrontend(Array(dir+"intmultOverflow.wacc"))._1 shouldBe 0
  }*/

  it should "pass intnegateOverflow.wacc" in pending /*{
    runFrontend(Array(dir+"intnegateOverflow.wacc"))._1 shouldBe 0
  }*/

  it should "pass intnegateOverflow2.wacc" in pending /*{
    runFrontend(Array(dir+"intnegateOverflow2.wacc"))._1 shouldBe 0
  }*/

  it should "pass intnegateOverflow3.wacc" in pending /*{
    runFrontend(Array(dir+"intnegateOverflow3.wacc"))._1 shouldBe 0
  }*/

  it should "pass intnegateOverflow4.wacc" in pending /*{
    runFrontend(Array(dir+"intnegateOverflow4.wacc"))._1 shouldBe 0
  }*/

}