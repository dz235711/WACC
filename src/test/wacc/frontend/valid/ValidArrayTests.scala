package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "frontend analyse array.wacc" taggedAs Frontend in {
    frontendStatus(dir + "array.wacc") shouldBe 0
  }

  it should "frontend analyse arrayBasic.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayBasic.wacc") shouldBe 0
  }

  it should "frontend analyse arrayEmpty.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayEmpty.wacc") shouldBe 0
  }

  it should "frontend analyse arrayIndexMayBeArrayIndex.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayIndexMayBeArrayIndex.wacc") shouldBe 0
  }

  it should "frontend analyse arrayLength.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayLength.wacc") shouldBe 0
  }

  it should "frontend analyse arrayLookup.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayLookup.wacc") shouldBe 0
  }

  it should "frontend analyse arrayNested.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayNested.wacc") shouldBe 0
  }

  it should "frontend analyse arrayOnHeap.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayOnHeap.wacc") shouldBe 0
  }

  it should "frontend analyse arrayPrint.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arrayPrint.wacc") shouldBe 0
  }

  it should "frontend analyse arraySimple.wacc" taggedAs Frontend in {
    frontendStatus(dir + "arraySimple.wacc") shouldBe 0
  }

  it should "frontend analyse charArrayInStringArray.wacc" taggedAs Frontend in {
    frontendStatus(dir + "charArrayInStringArray.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayAloneIsFine.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyArrayAloneIsFine.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayNextLine.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyArrayNextLine.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayPrint.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyArrayPrint.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayReplace.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyArrayReplace.wacc") shouldBe 0
  }

  it should "frontend analyse emptyArrayScope.wacc" taggedAs Frontend in {
    frontendStatus(dir + "emptyArrayScope.wacc") shouldBe 0
  }

  it should "frontend analyse free.wacc" taggedAs Frontend in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "frontend analyse lenArrayIndex.wacc" taggedAs Frontend in {
    frontendStatus(dir + "lenArrayIndex.wacc") shouldBe 0
  }

  it should "frontend analyse modifyString.wacc" taggedAs Frontend in {
    frontendStatus(dir + "modifyString.wacc") shouldBe 0
  }

  it should "frontend analyse printRef.wacc" taggedAs Frontend in {
    frontendStatus(dir + "printRef.wacc") shouldBe 0
  }

  it should "frontend analyse stringFromArray.wacc" taggedAs Frontend in {
    frontendStatus(dir + "stringFromArray.wacc") shouldBe 0
  }

}
