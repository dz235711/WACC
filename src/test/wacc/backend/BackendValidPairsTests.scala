package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "execute checkRefPair.wacc" in pending /*{
    fullExec(dir + checkRefPair.wacc, "") shouldBe Some("#addrs#\n#addrs#\ntrue\n10\n10\ntrue\na\na\ntrue\n")
  }*/

  it should "execute createPair.wacc" in pending /*{
    fullExec(dir + createPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute createPair02.wacc" in pending /*{
    fullExec(dir + createPair02.wacc, "") shouldBe Some("")
  }*/

  it should "execute createPair03.wacc" in pending /*{
    fullExec(dir + createPair03.wacc, "") shouldBe Some("")
  }*/

  it should "execute createRefPair.wacc" in pending /*{
    fullExec(dir + createRefPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute free.wacc" in pending /*{
    fullExec(dir + free.wacc, "") shouldBe Some("")
  }*/

  it should "execute linkedList.wacc" in pending /*{
    fullExec(dir + linkedList.wacc, "") shouldBe Some("list = {1, 2, 4, 11}\n")
  }*/

  it should "execute nestedPair.wacc" in pending /*{
    fullExec(dir + nestedPair.wacc, "") shouldBe Some("")
  }*/

  it should "execute nestedPairLeftAssign.wacc" in pending /*{
    fullExec(dir + nestedPairLeftAssign.wacc, "") shouldBe Some("7\n")
  }*/

  it should "execute nestedPairRightExtract.wacc" in pending /*{
    fullExec(dir + nestedPairRightExtract.wacc, "") shouldBe Some("2\n")
  }*/

  it should "execute null.wacc" in pending /*{
    fullExec(dir + null.wacc, "") shouldBe Some("(nil)\n(nil)\n")
  }*/

  it should "execute pairExchangeArrayOk.wacc" in pending /*{
    fullExec(dir + pairExchangeArrayOk.wacc, "") shouldBe Some("")
  }*/

  it should "execute pairarray.wacc" in pending /*{
    fullExec(dir + pairarray.wacc, "") shouldBe Some("3\n")
  }*/

  it should "execute printNull.wacc" in pending /*{
    fullExec(dir + printNull.wacc, "") shouldBe Some("(nil)\n")
  }*/

  it should "execute printNullPair.wacc" in pending /*{
    fullExec(dir + printNullPair.wacc, "") shouldBe Some("(nil)\n")
  }*/

  it should "execute printPair.wacc" in pending /*{
    fullExec(dir + printPair.wacc, "") shouldBe Some("#addrs# = (10, a)\n")
  }*/

  it should "execute printPairOfNulls.wacc" in pending /*{
    fullExec(dir + printPairOfNulls.wacc, "") shouldBe Some("#addrs# = ((nil),(nil))\n")
  }*/

  it should "execute readPair.wacc" in pending /*{
    fullExec(dir + readPair.wacc, "") shouldBe Some("Please enter the first element (char): Please enter the second element (int): The first element was f\nThe second element was 16\n")
  }*/

  it should "execute writeFst.wacc" in pending /*{
    fullExec(dir + writeFst.wacc, "") shouldBe Some("10\n42\n")
  }*/

  it should "execute writeSnd.wacc" in pending /*{
    fullExec(dir + writeSnd.wacc, "") shouldBe Some("a\nZ\n")
  }*/

}
