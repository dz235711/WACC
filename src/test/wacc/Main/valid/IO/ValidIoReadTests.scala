package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/read/"

  it should "pass echoBigInt.wacc" in pending /*{
    runFrontend(Array(dir+"echoBigInt.wacc"))._1 shouldBe 0
  }*/

  it should "pass echoBigNegInt.wacc" in pending /*{
    runFrontend(Array(dir+"echoBigNegInt.wacc"))._1 shouldBe 0
  }*/

  it should "pass echoChar.wacc" in pending /*{
    runFrontend(Array(dir+"echoChar.wacc"))._1 shouldBe 0
  }*/

  it should "pass echoInt.wacc" in pending /*{
    runFrontend(Array(dir+"echoInt.wacc"))._1 shouldBe 0
  }*/

  it should "pass echoNegInt.wacc" in pending /*{
    runFrontend(Array(dir+"echoNegInt.wacc"))._1 shouldBe 0
  }*/

  it should "pass echoPuncChar.wacc" in pending /*{
    runFrontend(Array(dir+"echoPuncChar.wacc"))._1 shouldBe 0
  }*/

  it should "pass read.wacc" in pending /*{
    runFrontend(Array(dir+"read.wacc"))._1 shouldBe 0
  }*/

  it should "pass readAtEof.wacc" in pending /*{
    runFrontend(Array(dir+"readAtEof.wacc"))._1 shouldBe 0
  }*/

}