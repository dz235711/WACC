package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrSequenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/sequence/"

  it should "pass doubleSeq.wacc" in {
    runFrontend(Array(dir + "doubleSeq.wacc"))._1 shouldBe 100
  }

  it should "pass emptySeq.wacc" in {
    runFrontend(Array(dir + "emptySeq.wacc"))._1 shouldBe 100
  }

  it should "pass endSeq.wacc" in {
    runFrontend(Array(dir + "endSeq.wacc"))._1 shouldBe 100
  }

  it should "pass extraSeq.wacc" in {
    runFrontend(Array(dir + "extraSeq.wacc"))._1 shouldBe 100
  }

  it should "pass missingSeq.wacc" in {
    runFrontend(Array(dir + "missingSeq.wacc"))._1 shouldBe 100
  }

}
