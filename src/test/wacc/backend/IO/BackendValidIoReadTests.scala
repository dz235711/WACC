package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoReadTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/read/"

  it should "execute echoBigInt.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoBigInt.wacc", "2147483647") shouldBe Some("enter an integer to echo\n2147483647\n")
  }*/

  it should "execute echoBigNegInt.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoBigNegInt.wacc", "-2147483648") shouldBe Some("enter an integer to echo\n-2147483648\n")
  }*/

  it should "execute echoChar.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoChar.wacc", "K") shouldBe Some("enter a character to echo\nK\n")
  }*/

  it should "execute echoInt.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoInt.wacc", "101") shouldBe Some("enter an integer to echo\n101\n")
  }*/

  it should "execute echoNegInt.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoNegInt.wacc", "-5") shouldBe Some("enter an integer to echo\n-5\n")
  }*/

  it should "execute echoPuncChar.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "echoPuncChar.wacc", "!") shouldBe Some("enter a character to echo\n!\n")
  }*/

  it should "execute read.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "read.wacc", "350") shouldBe Some("input an integer to continue...\n")
  }*/

  it should "execute readAtEof.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "readAtEof.wacc", "X") shouldBe Some("XZ\n")
  }*/

}
