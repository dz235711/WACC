package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "correctly execute array.wacc" in pending /*{
    fullExec(dir + array.wacc, "") shouldBe Some("#addrs# = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}\n")
  }*/

  it should "correctly execute arrayBasic.wacc" in pending /*{
    fullExec(dir + arrayBasic.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute arrayEmpty.wacc" in pending /*{
    fullExec(dir + arrayEmpty.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute arrayIndexMayBeArrayIndex.wacc" in pending /*{
    fullExec(dir + arrayIndexMayBeArrayIndex.wacc, "") shouldBe Some("6\n7\n8\n")
  }*/

  it should "correctly execute arrayLength.wacc" in pending /*{
    fullExec(dir + arrayLength.wacc, "") shouldBe Some("4\n")
  }*/

  it should "correctly execute arrayLookup.wacc" in pending /*{
    fullExec(dir + arrayLookup.wacc, "") shouldBe Some("43\n")
  }*/

  it should "correctly execute arrayNested.wacc" in pending /*{
    fullExec(dir + arrayNested.wacc, "") shouldBe Some("3\n3\n")
  }*/

  it should "correctly execute arrayOnHeap.wacc" in pending /*{
    fullExec(dir + arrayOnHeap.wacc, "") shouldBe Some("false\n0\n1\n")
  }*/

  it should "correctly execute arrayPrint.wacc" in pending /*{
    fullExec(dir + arrayPrint.wacc, "") shouldBe Some("#addrs# = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}\n")
  }*/

  it should "correctly execute arraySimple.wacc" in pending /*{
    fullExec(dir + arraySimple.wacc, "") shouldBe Some("42\n")
  }*/

  it should "correctly execute charArrayInStringArray.wacc" in pending /*{
    fullExec(dir + charArrayInStringArray.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute emptyArrayAloneIsFine.wacc" in pending /*{
    fullExec(dir + emptyArrayAloneIsFine.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute emptyArrayNextLine.wacc" in pending /*{
    fullExec(dir + emptyArrayNextLine.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute emptyArrayPrint.wacc" in pending /*{
    fullExec(dir + emptyArrayPrint.wacc, "") shouldBe Some("true\n")
  }*/

  it should "correctly execute emptyArrayReplace.wacc" in pending /*{
    fullExec(dir + emptyArrayReplace.wacc, "") shouldBe Some("true\n")
  }*/

  it should "correctly execute emptyArrayScope.wacc" in pending /*{
    fullExec(dir + emptyArrayScope.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute free.wacc" in pending /*{
    fullExec(dir + free.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute lenArrayIndex.wacc" in pending /*{
    fullExec(dir + lenArrayIndex.wacc, "") shouldBe Some("0")
  }*/

  it should "correctly execute modifyString.wacc" in pending /*{
    fullExec(dir + modifyString.wacc, "") shouldBe Some("hello world!\nHello world!\nHi!\n")
  }*/

  it should "correctly execute printRef.wacc" in pending /*{
    fullExec(dir + printRef.wacc, "") shouldBe Some("Printing an array variable gives an address, such as #addrs#\n")
  }*/

  it should "correctly execute stringFromArray.wacc" in pending /*{
    fullExec(dir + stringFromArray.wacc, "") shouldBe Some("")
  }*/

}