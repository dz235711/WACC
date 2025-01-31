package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "pass checkRefPair.wacc" in {
    runFrontend(Array(dir+"checkRefPair.wacc"))._1 shouldBe 0
  }

  it should "pass createPair.wacc" in {
    runFrontend(Array(dir+"createPair.wacc"))._1 shouldBe 0
  }

  it should "pass createPair02.wacc" in {
    runFrontend(Array(dir+"createPair02.wacc"))._1 shouldBe 0
  }

  it should "pass createPair03.wacc" in {
    runFrontend(Array(dir+"createPair03.wacc"))._1 shouldBe 0
  }

  it should "pass createRefPair.wacc" in {
    runFrontend(Array(dir+"createRefPair.wacc"))._1 shouldBe 0
  }

  it should "pass free.wacc" in {
    runFrontend(Array(dir+"free.wacc"))._1 shouldBe 0
  }

  it should "pass linkedList.wacc" in {
    runFrontend(Array(dir+"linkedList.wacc"))._1 shouldBe 0
  }

  it should "pass nestedPair.wacc" in {
    runFrontend(Array(dir+"nestedPair.wacc"))._1 shouldBe 0
  }

  it should "pass nestedPairLeftAssign.wacc" in {
    runFrontend(Array(dir+"nestedPairLeftAssign.wacc"))._1 shouldBe 0
  }

  it should "pass nestedPairRightExtract.wacc" in {
    runFrontend(Array(dir+"nestedPairRightExtract.wacc"))._1 shouldBe 0
  }

  it should "pass null.wacc" in {
    runFrontend(Array(dir+"null.wacc"))._1 shouldBe 0
  }

  it should "pass pairExchangeArrayOk.wacc" in {
    runFrontend(Array(dir+"pairExchangeArrayOk.wacc"))._1 shouldBe 0
  }

  it should "pass pairarray.wacc" in {
    runFrontend(Array(dir+"pairarray.wacc"))._1 shouldBe 0
  }

  it should "pass printNull.wacc" in {
    runFrontend(Array(dir+"printNull.wacc"))._1 shouldBe 0
  }

  it should "pass printNullPair.wacc" in {
    runFrontend(Array(dir+"printNullPair.wacc"))._1 shouldBe 0
  }

  it should "pass printPair.wacc" in {
    runFrontend(Array(dir+"printPair.wacc"))._1 shouldBe 0
  }

  it should "pass printPairOfNulls.wacc" in {
    runFrontend(Array(dir+"printPairOfNulls.wacc"))._1 shouldBe 0
  }

  it should "pass readPair.wacc" in {
    runFrontend(Array(dir+"readPair.wacc"))._1 shouldBe 0
  }

  it should "pass writeFst.wacc" in {
    runFrontend(Array(dir+"writeFst.wacc"))._1 shouldBe 0
  }

  it should "pass writeSnd.wacc" in {
    runFrontend(Array(dir+"writeSnd.wacc"))._1 shouldBe 0
  }

}