package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidAdvancedTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/advanced/"

  it should "pass binarySortTree.wacc" in {
    runFrontend(Array(dir + "binarySortTree.wacc"))._1 shouldBe 0
  }

  it should "pass hashTable.wacc" in {
    runFrontend(Array(dir+"hashTable.wacc"))._1 shouldBe 0
  }

  it should "pass ticTacToe.wacc" in {
    runFrontend(Array(dir+"ticTacToe.wacc"))._1 shouldBe 0
  }

}