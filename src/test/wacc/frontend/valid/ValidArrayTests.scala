package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "frontend analyse array.wacc" in {
    frontendStatus(dir + "array.wacc") shouldBe 0
  }

  it should "frontend analyse arrayBasic.wacc" in {
    frontendStatus(dir + "arrayBasic.wacc") shouldBe 0
  }

  it should "frontend analyse arrayEmpty.wacc" in {
    frontendStatus(dir + "arrayEmpty.wacc") shouldBe 0
  }

  it should "frontend analyse arrayIndexMayBeArrayIndex.wacc" in {
    frontendStatus(dir + "arrayIndexMayBeArrayIndex.wacc") shouldBe 0
  }

  it should "frontend analyse arrayLength.wacc" in {
    frontendStatus(dir + "arrayLength.wacc") shouldBe 0
  }

  it should "frontend analyse arrayLookup.wacc" in {
    frontendStatus(dir + "arrayLookup.wacc") shouldBe 0
  }

  it should "frontend analyse arrayNested.wacc" in {
    frontendStatus(dir + "arrayNested.wacc") shouldBe 0
  }

  it should "frontend analyse arrayOnHeap.wacc" in {
    frontendStatus(dir + "arrayOnHeap.wacc") shouldBe 0
  }

  it should "frontend analyse arrayPrint.wacc" in {
    frontendStatus(dir + "arrayPrint.wacc") shouldBe 0
  }

  it should "frontend analyse arraySimple.wacc" in {
    frontendStatus(dir + "arraySimple.wacc") shouldBe 0
  }

  it should "frontend analyse charArrayInStringArray.wacc" in {
    frontendStatus(dir + "charArrayInStringArray.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayAloneIsFine.wacc" in {
    frontendStatus(dir + "emptyArrayAloneIsFine.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayNextLine.wacc" in {
    frontendStatus(dir + "emptyArrayNextLine.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayPrint.wacc" in {
    frontendStatus(dir + "emptyArrayPrint.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayReplace.wacc" in {
    frontendStatus(dir + "emptyArrayReplace.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayScope.wacc" in {
    frontendStatus(dir + "emptyArrayScope.wacc") shouldBe 0
  }

  it should "frontend analyse free.wacc" in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "frontend analyse lenArrayIndex.wacc" in {
    frontendStatus(dir + "lenArrayIndex.wacc") shouldBe 0
  }

  it should "frontend analyse modifyString.wacc" in {
    frontendStatus(dir + "modifyString.wacc") shouldBe 0
  }

  it should "frontend analyse printRef.wacc" in {
    frontendStatus(dir + "printRef.wacc") shouldBe 0
  }

  it should "frontend analyse stringFromArray.wacc" in {
    frontendStatus(dir + "stringFromArray.wacc") shouldBe 0
  }

}
