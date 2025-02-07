package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/pairs/"

  it should "pass badPairAssign.wacc" in {
    runFrontend(Array(dir+"badPairAssign.wacc"))._1 shouldBe 200
  }

  it should "pass badPairExchange.wacc" in {
    runFrontend(Array(dir+"badPairExchange.wacc"))._1 shouldBe 200
  }

  it should "pass freeNonPair.wacc" in {
    runFrontend(Array(dir+"freeNonPair.wacc"))._1 shouldBe 200
  }

  it should "pass mismatchedPair.wacc" in {
    runFrontend(Array(dir+"mismatchedPair.wacc"))._1 shouldBe 200
  }

  it should "pass noPairCovariance.wacc" in {
    runFrontend(Array(dir+"noPairCovariance.wacc"))._1 shouldBe 200
  }

  it should "pass nonMatchingPairs.wacc" in {
    runFrontend(Array(dir+"nonMatchingPairs.wacc"))._1 shouldBe 200
  }

  it should "pass readUnknown.wacc" in {
    runFrontend(Array(dir+"readUnknown.wacc"))._1 shouldBe 200
  }

  it should "pass wrongTypeInParameterlessPair.wacc" in {
    runFrontend(Array(dir+"wrongTypeInParameterlessPair.wacc"))._1 shouldBe 200
  }

}