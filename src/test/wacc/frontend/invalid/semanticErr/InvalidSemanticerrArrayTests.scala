package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrArrayTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/array/"

  it should "frontend analyse arrayIndexComplexNotInt.wacc" in {
    frontendStatus(dir + "arrayIndexComplexNotInt.wacc") shouldBe 200
  }

  it should "frontend analyse arrayIndexNotInt.wacc" in {
    frontendStatus(dir + "arrayIndexNotInt.wacc") shouldBe 200
  }

  it should "frontend analyse arrayMultipleIndexError.wacc" in {
    frontendStatus(dir + "arrayMultipleIndexError.wacc") shouldBe 200
  }

  it should "frontend analyse badIndex.wacc" in {
    frontendStatus(dir + "badIndex.wacc") shouldBe 200
  }

  it should "frontend analyse indexUndefIdent.wacc" in {
    frontendStatus(dir + "indexUndefIdent.wacc") shouldBe 200
  }

  it should "frontend analyse mixingTypesInArrays.wacc" in {
    frontendStatus(dir + "mixingTypesInArrays.wacc") shouldBe 200
  }

  it should "frontend analyse noArrayCovariance.wacc" in {
    frontendStatus(dir + "noArrayCovariance.wacc") shouldBe 200
  }

  it should "frontend analyse noStringIndex.wacc" in {
    frontendStatus(dir + "noStringIndex.wacc") shouldBe 200
  }

  it should "frontend analyse nonMatchingArrays.wacc" in {
    frontendStatus(dir + "nonMatchingArrays.wacc") shouldBe 200
  }

  it should "frontend analyse wrongArrayDimension.wacc" in {
    frontendStatus(dir + "wrongArrayDimension.wacc") shouldBe 200
  }

  it should "frontend analyse wrongArrayType.wacc" in {
    frontendStatus(dir + "wrongArrayType.wacc") shouldBe 200
  }

}
