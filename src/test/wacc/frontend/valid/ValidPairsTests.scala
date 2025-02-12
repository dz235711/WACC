package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "frontend analyse checkRefPair.wacc" in {
    frontendStatus(dir + "checkRefPair.wacc") shouldBe 0
  }

  it should "frontend analyse createPair.wacc" in {
    frontendStatus(dir + "createPair.wacc") shouldBe 0
  }

  it should "frontend analyse createPair02.wacc" in {
    frontendStatus(dir + "createPair02.wacc") shouldBe 0
  }

  it should "frontend analyse createPair03.wacc" in {
    frontendStatus(dir + "createPair03.wacc") shouldBe 0
  }

  it should "frontend analyse createRefPair.wacc" in {
    frontendStatus(dir + "createRefPair.wacc") shouldBe 0
  }

  it should "frontend analyse free.wacc" in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "frontend analyse linkedList.wacc" in {
    frontendStatus(dir + "linkedList.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPair.wacc" in {
    frontendStatus(dir + "nestedPair.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPairLeftAssign.wacc" in {
    frontendStatus(dir + "nestedPairLeftAssign.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPairRightExtract.wacc" in {
    frontendStatus(dir + "nestedPairRightExtract.wacc") shouldBe 0
  }

  it should "frontend analyse null.wacc" in {
    frontendStatus(dir + "null.wacc") shouldBe 0
  }

  it should "frontend analyse pairExchangeArrayOk.wacc" in {
    frontendStatus(dir + "pairExchangeArrayOk.wacc") shouldBe 0
  }

  it should "frontend analyse pairarray.wacc" in {
    frontendStatus(dir + "pairarray.wacc") shouldBe 0
  }

  it should "frontend analyse printNull.wacc" in {
    frontendStatus(dir + "printNull.wacc") shouldBe 0
  }

  it should "frontend analyse printNullPair.wacc" in {
    frontendStatus(dir + "printNullPair.wacc") shouldBe 0
  }

  it should "frontend analyse printPair.wacc" in {
    frontendStatus(dir + "printPair.wacc") shouldBe 0
  }

  it should "frontend analyse printPairOfNulls.wacc" in {
    frontendStatus(dir + "printPairOfNulls.wacc") shouldBe 0
  }

  it should "frontend analyse readPair.wacc" in {
    frontendStatus(dir + "readPair.wacc") shouldBe 0
  }

  it should "frontend analyse writeFst.wacc" in {
    frontendStatus(dir + "writeFst.wacc") shouldBe 0
  }

  it should "frontend analyse writeSnd.wacc" in {
    frontendStatus(dir + "writeSnd.wacc") shouldBe 0
  }

}
