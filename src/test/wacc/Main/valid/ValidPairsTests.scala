package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "pass checkRefPair.wacc" in {
    frontendStatus(dir + "checkRefPair.wacc") shouldBe 0
  }

  it should "pass createPair.wacc" in {
    frontendStatus(dir + "createPair.wacc") shouldBe 0
  }

  it should "pass createPair02.wacc" in {
    frontendStatus(dir + "createPair02.wacc") shouldBe 0
  }

  it should "pass createPair03.wacc" in {
    frontendStatus(dir + "createPair03.wacc") shouldBe 0
  }

  it should "pass createRefPair.wacc" in {
    frontendStatus(dir + "createRefPair.wacc") shouldBe 0
  }

  it should "pass free.wacc" in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "pass linkedList.wacc" in {
    frontendStatus(dir + "linkedList.wacc") shouldBe 0
  }

  it should "pass nestedPair.wacc" in {
    frontendStatus(dir + "nestedPair.wacc") shouldBe 0
  }

  it should "pass nestedPairLeftAssign.wacc" in {
    frontendStatus(dir + "nestedPairLeftAssign.wacc") shouldBe 0
  }

  it should "pass nestedPairRightExtract.wacc" in {
    frontendStatus(dir + "nestedPairRightExtract.wacc") shouldBe 0
  }

  it should "pass null.wacc" in {
    frontendStatus(dir + "null.wacc") shouldBe 0
  }

  it should "pass pairExchangeArrayOk.wacc" in {
    frontendStatus(dir + "pairExchangeArrayOk.wacc") shouldBe 0
  }

  it should "pass pairarray.wacc" in {
    frontendStatus(dir + "pairarray.wacc") shouldBe 0
  }

  it should "pass printNull.wacc" in {
    frontendStatus(dir + "printNull.wacc") shouldBe 0
  }

  it should "pass printNullPair.wacc" in {
    frontendStatus(dir + "printNullPair.wacc") shouldBe 0
  }

  it should "pass printPair.wacc" in {
    frontendStatus(dir + "printPair.wacc") shouldBe 0
  }

  it should "pass printPairOfNulls.wacc" in {
    frontendStatus(dir + "printPairOfNulls.wacc") shouldBe 0
  }

  it should "pass readPair.wacc" in {
    frontendStatus(dir + "readPair.wacc") shouldBe 0
  }

  it should "pass writeFst.wacc" in {
    frontendStatus(dir + "writeFst.wacc") shouldBe 0
  }

  it should "pass writeSnd.wacc" in {
    frontendStatus(dir + "writeSnd.wacc") shouldBe 0
  }

}
