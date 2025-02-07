package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/array/"

  it should "pass array.wacc" in {
    runFrontend(Array(dir + "array.wacc"))._1 shouldBe 0
  }

  it should "pass arrayBasic.wacc" in {
    runFrontend(Array(dir + "arrayBasic.wacc"))._1 shouldBe 0
  }

  it should "pass arrayEmpty.wacc" in {
    runFrontend(Array(dir + "arrayEmpty.wacc"))._1 shouldBe 0
  }

  it should "pass arrayIndexMayBeArrayIndex.wacc" in {
    runFrontend(Array(dir + "arrayIndexMayBeArrayIndex.wacc"))._1 shouldBe 0
  }

  it should "pass arrayLength.wacc" in {
    runFrontend(Array(dir + "arrayLength.wacc"))._1 shouldBe 0
  }

  it should "pass arrayLookup.wacc" in {
    runFrontend(Array(dir + "arrayLookup.wacc"))._1 shouldBe 0
  }

  it should "pass arrayNested.wacc" in {
    runFrontend(Array(dir + "arrayNested.wacc"))._1 shouldBe 0
  }

  it should "pass arrayOnHeap.wacc" in {
    runFrontend(Array(dir + "arrayOnHeap.wacc"))._1 shouldBe 0
  }

  it should "pass arrayPrint.wacc" in {
    runFrontend(Array(dir + "arrayPrint.wacc"))._1 shouldBe 0
  }

  it should "pass arraySimple.wacc" in {
    runFrontend(Array(dir + "arraySimple.wacc"))._1 shouldBe 0
  }

  it should "pass charArrayInStringArray.wacc" in {
    runFrontend(Array(dir + "charArrayInStringArray.wacc"))._1 shouldBe 0
  }

  it should "pass emptyArrayAloneIsFine.wacc" in {
    runFrontend(Array(dir + "emptyArrayAloneIsFine.wacc"))._1 shouldBe 0
  }

  it should "pass emptyArrayNextLine.wacc" in {
    runFrontend(Array(dir + "emptyArrayNextLine.wacc"))._1 shouldBe 0
  }

  it should "pass emptyArrayPrint.wacc" in {
    runFrontend(Array(dir + "emptyArrayPrint.wacc"))._1 shouldBe 0
  }

  it should "pass emptyArrayReplace.wacc" in {
    runFrontend(Array(dir + "emptyArrayReplace.wacc"))._1 shouldBe 0
  }

  it should "pass emptyArrayScope.wacc" in {
    runFrontend(Array(dir + "emptyArrayScope.wacc"))._1 shouldBe 0
  }

  it should "pass free.wacc" in {
    runFrontend(Array(dir + "free.wacc"))._1 shouldBe 0
  }

  it should "pass lenArrayIndex.wacc" in {
    runFrontend(Array(dir + "lenArrayIndex.wacc"))._1 shouldBe 0
  }

  it should "pass modifyString.wacc" in {
    runFrontend(Array(dir + "modifyString.wacc"))._1 shouldBe 0
  }

  it should "pass printRef.wacc" in {
    runFrontend(Array(dir + "printRef.wacc"))._1 shouldBe 0
  }

  it should "pass stringFromArray.wacc" in {
    runFrontend(Array(dir + "stringFromArray.wacc"))._1 shouldBe 0
  }

}
