package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidPairsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/pairs/"

  it should "frontend analyse checkRefPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "checkRefPair.wacc") shouldBe 0
  }

  it should "frontend analyse createPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "createPair.wacc") shouldBe 0
  }

  it should "frontend analyse createPair02.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "createPair02.wacc") shouldBe 0
  }

  it should "frontend analyse createPair03.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "createPair03.wacc") shouldBe 0
  }

  it should "frontend analyse createRefPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "createRefPair.wacc") shouldBe 0
  }

  it should "frontend analyse free.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "free.wacc") shouldBe 0
  }

  it should "frontend analyse linkedList.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "linkedList.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "nestedPair.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPairLeftAssign.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "nestedPairLeftAssign.wacc") shouldBe 0
  }

  it should "frontend analyse nestedPairRightExtract.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "nestedPairRightExtract.wacc") shouldBe 0
  }

  it should "frontend analyse null.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "null.wacc") shouldBe 0
  }

  it should "frontend analyse pairExchangeArrayOk.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "pairExchangeArrayOk.wacc") shouldBe 0
  }

  it should "frontend analyse pairarray.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "pairarray.wacc") shouldBe 0
  }

  it should "frontend analyse printNull.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printNull.wacc") shouldBe 0
  }

  it should "frontend analyse printNullPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printNullPair.wacc") shouldBe 0
  }

  it should "frontend analyse printPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printPair.wacc") shouldBe 0
  }

  it should "frontend analyse printPairOfNulls.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "printPairOfNulls.wacc") shouldBe 0
  }

  it should "frontend analyse readPair.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "readPair.wacc") shouldBe 0
  }

  it should "frontend analyse writeFst.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "writeFst.wacc") shouldBe 0
  }

  it should "frontend analyse writeSnd.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "writeSnd.wacc") shouldBe 0
  }

}
