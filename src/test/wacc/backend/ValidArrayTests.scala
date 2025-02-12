package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "pass array.wacc" in {
    frontendStatus(dir + "array.wacc") shouldBe 0
  }

  it should "pass arrayBasic.wacc" in {
    frontendStatus(dir + "arrayBasic.wacc") shouldBe 0
  }

  it should "pass arrayEmpty.wacc" in {
    frontendStatus(dir + "arrayEmpty.wacc") shouldBe 0
  }

  it should "pass arrayIndexMayBeArrayIndex.wacc" in {
    frontendStatus(dir + "arrayIndexMayBeArrayIndex.wacc") shouldBe 0
  }

  it should "pass arrayLength.wacc" in {
    frontendStatus(dir + "arrayLength.wacc") shouldBe 0
  }

  it should "pass arrayLookup.wacc" in {
    frontendStatus(dir + "arrayLookup.wacc") shouldBe 0
  }

  it should "pass arrayNested.wacc" in {
    frontendStatus(dir + "arrayNested.wacc") shouldBe 0
  }

  it should "pass arrayOnHeap.wacc" in {
    frontendStatus(dir + "arrayOnHeap.wacc") shouldBe 0
  }

  it should "pass arrayPrint.wacc" in {
    frontendStatus(dir + "arrayPrint.wacc") shouldBe 0
  }

  it should "pass arraySimple.wacc" in {
    frontendStatus(dir + "arraySimple.wacc") shouldBe 0
  }

  it should "pass charArrayInStringArray.wacc" in {
    frontendStatus(dir + "charArrayInStringArray.wacc") shouldBe 0
  }

  it should "pass emptyArrayAloneIsFine.wacc" in {
    frontendStatus(dir + "emptyArrayAloneIsFine.wacc") shouldBe 0
  }

  it should "pass emptyArrayNextLine.wacc" in {
    frontendStatus(dir + "emptyArrayNextLine.wacc") shouldBe 0
  }

  it should "pass emptyArrayPrint.wacc" in {
    frontendStatus(dir + "emptyArrayPrint.wacc") shouldBe 0
  }

  it should "pass emptyArrayReplace.wacc" in {
    frontendStatus(dir + "emptyArrayReplace.wacc") shouldBe 0
  }

  it should "pass emptyArrayScope.wacc" in {
    frontendStatus(dir + "emptyArrayScope.wacc") shouldBe 0
  }

  it should "pass free.wacc" in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "pass lenArrayIndex.wacc" in {
    frontendStatus(dir + "lenArrayIndex.wacc") shouldBe 0
  }

  it should "pass modifyString.wacc" in {
    frontendStatus(dir + "modifyString.wacc") shouldBe 0
  }

  it should "pass printRef.wacc" in {
    frontendStatus(dir + "printRef.wacc") shouldBe 0
  }

  it should "pass stringFromArray.wacc" in {
    frontendStatus(dir + "stringFromArray.wacc") shouldBe 0
  }

}
