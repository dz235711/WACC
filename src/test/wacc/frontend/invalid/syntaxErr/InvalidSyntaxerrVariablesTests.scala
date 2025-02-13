package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSyntaxerrVariablesTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/syntaxErr/variables/"

  it should "frontend analyse badintAssignments.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badintAssignments.wacc") shouldBe 100
  }

  it should "frontend analyse badintAssignments1.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badintAssignments1.wacc") shouldBe 100
  }

  it should "frontend analyse badintAssignments2.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "badintAssignments2.wacc") shouldBe 100
  }

  it should "frontend analyse bigIntAssignment.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "bigIntAssignment.wacc") shouldBe 100
  }

  it should "frontend analyse varNoName.wacc" taggedAs (Frontend) in {
    frontendStatus(dir + "varNoName.wacc") shouldBe 100
  }

}
