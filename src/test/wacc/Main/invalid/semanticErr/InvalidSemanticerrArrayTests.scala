package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/array/"

  it should "pass arrayIndexComplexNotInt.wacc" in {
    frontendStatus(dir + "arrayIndexComplexNotInt.wacc") shouldBe 200
  }

  it should "pass arrayIndexNotInt.wacc" in {
    frontendStatus(dir + "arrayIndexNotInt.wacc") shouldBe 200
  }

  it should "pass arrayMultipleIndexError.wacc" in {
    frontendStatus(dir + "arrayMultipleIndexError.wacc") shouldBe 200
  }

  it should "pass badIndex.wacc" in {
    frontendStatus(dir + "badIndex.wacc") shouldBe 200
  }

  it should "pass indexUndefIdent.wacc" in {
    frontendStatus(dir + "indexUndefIdent.wacc") shouldBe 200
  }

  it should "pass mixingTypesInArrays.wacc" in {
    frontendStatus(dir + "mixingTypesInArrays.wacc") shouldBe 200
  }

  it should "pass noArrayCovariance.wacc" in {
    frontendStatus(dir + "noArrayCovariance.wacc") shouldBe 200
  }

  it should "pass noStringIndex.wacc" in {
    frontendStatus(dir + "noStringIndex.wacc") shouldBe 200
  }

  it should "pass nonMatchingArrays.wacc" in {
    frontendStatus(dir + "nonMatchingArrays.wacc") shouldBe 200
  }

  it should "pass wrongArrayDimension.wacc" in {
    frontendStatus(dir + "wrongArrayDimension.wacc") shouldBe 200
  }

  it should "pass wrongArrayType.wacc" in {
    frontendStatus(dir + "wrongArrayType.wacc") shouldBe 200
  }

}
