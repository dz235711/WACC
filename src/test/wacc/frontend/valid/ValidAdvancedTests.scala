package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidAdvancedTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/advanced/"

  it should "frontend analyse binarySortTree.wacc" in {
    frontendStatus(dir + "binarySortTree.wacc") shouldBe 0
  }

  it should "frontend analyse hashTable.wacc" in {
    frontendStatus(dir + "hashTable.wacc") shouldBe 0
  }

  it should "frontend analyse ticTacToe.wacc" in {
    frontendStatus(dir + "ticTacToe.wacc") shouldBe 0
  }

}
