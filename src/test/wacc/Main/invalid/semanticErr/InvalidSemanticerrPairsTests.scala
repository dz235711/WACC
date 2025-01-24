package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/pairs/"

  it should "pass badPairAssign.wacc" in pending /*{
    runFrontend(Array(dir+"badPairAssign.wacc"))._1 shouldBe 200
  }*/

  it should "pass badPairExchange.wacc" in pending /*{
    runFrontend(Array(dir+"badPairExchange.wacc"))._1 shouldBe 200
  }*/

  it should "pass freeNonPair.wacc" in pending /*{
    runFrontend(Array(dir+"freeNonPair.wacc"))._1 shouldBe 200
  }*/

  it should "pass mismatchedPair.wacc" in pending /*{
    runFrontend(Array(dir+"mismatchedPair.wacc"))._1 shouldBe 200
  }*/

  it should "pass noPairCovariance.wacc" in pending /*{
    runFrontend(Array(dir+"noPairCovariance.wacc"))._1 shouldBe 200
  }*/

  it should "pass nonMatchingPairs.wacc" in pending /*{
    runFrontend(Array(dir+"nonMatchingPairs.wacc"))._1 shouldBe 200
  }*/

  it should "pass readUnknown.wacc" in pending /*{
    runFrontend(Array(dir+"readUnknown.wacc"))._1 shouldBe 200
  }*/

  it should "pass wrongTypeInParameterlessPair.wacc" in pending /*{
    runFrontend(Array(dir+"wrongTypeInParameterlessPair.wacc"))._1 shouldBe 200
  }*/

}