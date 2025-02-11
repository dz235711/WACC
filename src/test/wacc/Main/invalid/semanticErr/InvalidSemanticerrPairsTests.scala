package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/pairs/"

  it should "pass badPairAssign.wacc" in {
    frontendStatus(dir + "badPairAssign.wacc") shouldBe 200
  }

  it should "pass badPairExchange.wacc" in {
    frontendStatus(dir + "badPairExchange.wacc") shouldBe 200
  }

  it should "pass freeNonPair.wacc" in {
    frontendStatus(dir + "freeNonPair.wacc") shouldBe 200
  }

  it should "pass mismatchedPair.wacc" in {
    frontendStatus(dir + "mismatchedPair.wacc") shouldBe 200
  }

  it should "pass noPairCovariance.wacc" in {
    frontendStatus(dir + "noPairCovariance.wacc") shouldBe 200
  }

  it should "pass nonMatchingPairs.wacc" in {
    frontendStatus(dir + "nonMatchingPairs.wacc") shouldBe 200
  }

  it should "pass readUnknown.wacc" in {
    frontendStatus(dir + "readUnknown.wacc") shouldBe 200
  }

  it should "pass wrongTypeInParameterlessPair.wacc" in {
    frontendStatus(dir + "wrongTypeInParameterlessPair.wacc") shouldBe 200
  }

}
