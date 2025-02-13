package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "execute array.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + array.wacc, "") shouldBe Some("#addrs# = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}\n")
  }*/

  it should "execute arrayBasic.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayBasic.wacc, "") shouldBe Some("")
  }*/

  it should "execute arrayEmpty.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayEmpty.wacc, "") shouldBe Some("")
  }*/

  it should "execute arrayIndexMayBeArrayIndex.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayIndexMayBeArrayIndex.wacc, "") shouldBe Some("6\n7\n8\n")
  }*/

  it should "execute arrayLength.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayLength.wacc, "") shouldBe Some("4\n")
  }*/

  it should "execute arrayLookup.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayLookup.wacc, "") shouldBe Some("43\n")
  }*/

  it should "execute arrayNested.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayNested.wacc, "") shouldBe Some("3\n3\n")
  }*/

  it should "execute arrayOnHeap.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayOnHeap.wacc, "") shouldBe Some("false\n0\n1\n")
  }*/

  it should "execute arrayPrint.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arrayPrint.wacc, "") shouldBe Some("#addrs# = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}\n")
  }*/

  it should "execute arraySimple.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + arraySimple.wacc, "") shouldBe Some("42\n")
  }*/

  it should "execute charArrayInStringArray.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + charArrayInStringArray.wacc, "") shouldBe Some("")
  }*/

  it should "execute emptyArrayAloneIsFine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyArrayAloneIsFine.wacc, "") shouldBe Some("")
  }*/

  it should "execute emptyArrayNextLine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyArrayNextLine.wacc, "") shouldBe Some("")
  }*/

  it should "execute emptyArrayPrint.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyArrayPrint.wacc, "") shouldBe Some("true\n")
  }*/

  it should "execute emptyArrayReplace.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyArrayReplace.wacc, "") shouldBe Some("true\n")
  }*/

  it should "execute emptyArrayScope.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + emptyArrayScope.wacc, "") shouldBe Some("")
  }*/

  it should "execute free.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + free.wacc, "") shouldBe Some("")
  }*/

  it should "execute lenArrayIndex.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + lenArrayIndex.wacc, "") shouldBe Some("0")
  }*/

  it should "execute modifyString.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + modifyString.wacc, "") shouldBe Some("hello world!\nHello world!\nHi!\n")
  }*/

  it should "execute printRef.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printRef.wacc, "") shouldBe Some("Printing an array variable gives an address, such as #addrs#\n")
  }*/

  it should "execute stringFromArray.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + stringFromArray.wacc, "") shouldBe Some("")
  }*/

}
