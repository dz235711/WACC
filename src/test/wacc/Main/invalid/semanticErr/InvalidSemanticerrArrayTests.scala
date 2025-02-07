package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/array/"

  it should "pass arrayIndexComplexNotInt.wacc" in {
    runFrontend(Array(dir+"arrayIndexComplexNotInt.wacc"))._1 shouldBe 200
  }

  it should "pass arrayIndexNotInt.wacc" in {
    runFrontend(Array(dir+"arrayIndexNotInt.wacc"))._1 shouldBe 200
  }

  it should "pass arrayMultipleIndexError.wacc" in {
    runFrontend(Array(dir+"arrayMultipleIndexError.wacc"))._1 shouldBe 200
  }

  it should "pass badIndex.wacc" in {
    runFrontend(Array(dir+"badIndex.wacc"))._1 shouldBe 200
  }

  it should "pass indexUndefIdent.wacc" in {
    runFrontend(Array(dir+"indexUndefIdent.wacc"))._1 shouldBe 200
  }

  it should "pass mixingTypesInArrays.wacc" in {
    runFrontend(Array(dir+"mixingTypesInArrays.wacc"))._1 shouldBe 200
  }

  it should "pass noArrayCovariance.wacc" in {
    runFrontend(Array(dir+"noArrayCovariance.wacc"))._1 shouldBe 200
  }

  it should "pass noStringIndex.wacc" in {
    runFrontend(Array(dir+"noStringIndex.wacc"))._1 shouldBe 200
  }

  it should "pass nonMatchingArrays.wacc" in {
    runFrontend(Array(dir+"nonMatchingArrays.wacc"))._1 shouldBe 200
  }

  it should "pass wrongArrayDimension.wacc" in {
    runFrontend(Array(dir+"wrongArrayDimension.wacc"))._1 shouldBe 200
  }

  it should "pass wrongArrayType.wacc" in {
    runFrontend(Array(dir+"wrongArrayType.wacc"))._1 shouldBe 200
  }

}