package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidIoReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/read/"

  it should "frontend analyse echoBigInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoBigInt.wacc") shouldBe 0
  }

  it should "frontend analyse echoBigNegInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoBigNegInt.wacc") shouldBe 0
  }

  it should "frontend analyse echoChar.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoChar.wacc") shouldBe 0
  }

  it should "frontend analyse echoInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoInt.wacc") shouldBe 0
  }

  it should "frontend analyse echoNegInt.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoNegInt.wacc") shouldBe 0
  }

  it should "frontend analyse echoPuncChar.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "echoPuncChar.wacc") shouldBe 0
  }

  it should "frontend analyse read.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "read.wacc") shouldBe 0
  }

  it should "frontend analyse readAtEof.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "readAtEof.wacc") shouldBe 0
  }

}
