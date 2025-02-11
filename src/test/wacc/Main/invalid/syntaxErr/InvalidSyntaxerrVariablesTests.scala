package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/variables/"

  it should "pass badintAssignments.wacc" in {
    frontendStatus(dir + "badintAssignments.wacc") shouldBe 100
  }

  it should "pass badintAssignments1.wacc" in {
    frontendStatus(dir + "badintAssignments1.wacc") shouldBe 100
  }

  it should "pass badintAssignments2.wacc" in {
    frontendStatus(dir + "badintAssignments2.wacc") shouldBe 100
  }

  it should "pass bigIntAssignment.wacc" in {
    frontendStatus(dir + "bigIntAssignment.wacc") shouldBe 100
  }

  it should "pass varNoName.wacc" in {
    frontendStatus(dir + "varNoName.wacc") shouldBe 100
  }

}
