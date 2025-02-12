package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/pairs/"

  it should "frontend analyse badPairAssign.wacc" in {
    frontendStatus(dir + "badPairAssign.wacc") shouldBe 200
  }

  it should "frontend analyse badPairExchange.wacc" in {
    frontendStatus(dir + "badPairExchange.wacc") shouldBe 200
  }

  it should "frontend analyse freeNonPair.wacc" in {
    frontendStatus(dir + "freeNonPair.wacc") shouldBe 200
  }

  it should "frontend analyse mismatchedPair.wacc" in {
    frontendStatus(dir + "mismatchedPair.wacc") shouldBe 200
  }

  it should "frontend analyse noPairCovariance.wacc" in {
    frontendStatus(dir + "noPairCovariance.wacc") shouldBe 200
  }

  it should "frontend analyse nonMatchingPairs.wacc" in {
    frontendStatus(dir + "nonMatchingPairs.wacc") shouldBe 200
  }

  it should "frontend analyse readUnknown.wacc" in {
    frontendStatus(dir + "readUnknown.wacc") shouldBe 200
  }

  it should "frontend analyse wrongTypeInParameterlessPair.wacc" in {
    frontendStatus(dir + "wrongTypeInParameterlessPair.wacc") shouldBe 200
  }

}
