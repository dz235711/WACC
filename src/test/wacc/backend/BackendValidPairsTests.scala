package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "execute checkRefPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + checkRefPair.wacc, "") shouldBe Some("#addrs#\n#addrs#\ntrue\n10\n10\ntrue\na\na\ntrue\n")
  }*/

  it should "execute createPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + createPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute createPair02.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + createPair02.wacc, "") shouldBe Some("")
  }*/

  it should "execute createPair03.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + createPair03.wacc, "") shouldBe Some("")
  }*/

  it should "execute createRefPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + createRefPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute free.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + free.wacc, "") shouldBe Some("")
  }*/

  it should "execute linkedList.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + linkedList.wacc, "") shouldBe Some("list = {1, 2, 4, 11}\n")
  }*/

  it should "execute nestedPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + nestedPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute nestedPairLeftAssign.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + nestedPairLeftAssign.wacc, "") shouldBe Some("7\n")
  }*/

  it should "execute nestedPairRightExtract.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + nestedPairRightExtract.wacc, "") shouldBe Some("2\n")
  }*/

  it should "execute null.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + null.wacc, "") shouldBe Some("(nil)\n(nil)\n")
  }*/

  it should "execute pairExchangeArrayOk.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + pairExchangeArrayOk.wacc, "") shouldBe Some("")
  }*/

  it should "execute pairarray.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + pairarray.wacc, "") shouldBe Some("3\n")
  }*/

  it should "execute printNull.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printNull.wacc, "") shouldBe Some("(nil)\n")
  }*/

  it should "execute printNullPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printNullPair.wacc, "") shouldBe Some("(nil)\n")
  }*/

  it should "execute printPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printPair.wacc, "") shouldBe Some("#addrs# = (10, a)\n")
  }*/

  it should "execute printPairOfNulls.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printPairOfNulls.wacc, "") shouldBe Some("#addrs# = ((nil),(nil))\n")
  }*/

  it should "execute readPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + readPair.wacc, "") shouldBe Some("Please enter the first element (char): Please enter the second element (int): The first element was f\nThe second element was 16\n")
  }*/

  it should "execute writeFst.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + writeFst.wacc, "") shouldBe Some("10\n42\n")
  }*/

  it should "execute writeSnd.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + writeSnd.wacc, "") shouldBe Some("a\nZ\n")
  }*/

}
