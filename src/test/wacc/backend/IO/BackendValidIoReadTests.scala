package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/read/"

  it should "execute echoBigInt.wacc" in pending /*{
    fullExec(dir + echoBigInt.wacc, "") shouldBe Some("enter an integer to echo\n2147483647\n")
  }*/

  it should "execute echoBigNegInt.wacc" in pending /*{
    fullExec(dir + echoBigNegInt.wacc, "") shouldBe Some("enter an integer to echo\n-2147483648\n")
  }*/

  it should "execute echoChar.wacc" in pending /*{
    fullExec(dir + echoChar.wacc, "") shouldBe Some("enter a character to echo\nK\n")
  }*/

  it should "execute echoInt.wacc" in pending /*{
    fullExec(dir + echoInt.wacc, "") shouldBe Some("enter an integer to echo\n101\n")
  }*/

  it should "execute echoNegInt.wacc" in pending /*{
    fullExec(dir + echoNegInt.wacc, "") shouldBe Some("enter an integer to echo\n-5\n")
  }*/

  it should "execute echoPuncChar.wacc" in pending /*{
    fullExec(dir + echoPuncChar.wacc, "") shouldBe Some("enter a character to echo\n!\n")
  }*/

  it should "execute read.wacc" in pending /*{
    fullExec(dir + read.wacc, "") shouldBe Some("input an integer to continue...\n")
  }*/

  it should "execute readAtEof.wacc" in pending /*{
    fullExec(dir + readAtEof.wacc, "") shouldBe Some("XZ\n")
  }*/

}
