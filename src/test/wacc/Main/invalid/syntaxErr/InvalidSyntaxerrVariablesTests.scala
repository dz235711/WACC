package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/variables/"

  it should "pass badintAssignments.wacc" in {
    runFrontend(Array(dir + "badintAssignments.wacc"))._1 shouldBe 100
  }

  it should "pass badintAssignments1.wacc" in {
    runFrontend(Array(dir + "badintAssignments1.wacc"))._1 shouldBe 100
  }

  it should "pass badintAssignments2.wacc" in {
    runFrontend(Array(dir + "badintAssignments2.wacc"))._1 shouldBe 100
  }

  it should "pass bigIntAssignment.wacc" in {
    runFrontend(Array(dir + "bigIntAssignment.wacc"))._1 shouldBe 100
  }

  it should "pass varNoName.wacc" in {
    runFrontend(Array(dir + "varNoName.wacc"))._1 shouldBe 100
  }

}
