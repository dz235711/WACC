package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/scope/"

  it should "frontend analyse badParentScope.wacc" taggedAs Frontend in {
    frontendStatus(dir + "badParentScope.wacc") shouldBe 200
  }

  it should "frontend analyse badScopeRedefine.wacc" taggedAs Frontend in {
    frontendStatus(dir + "badScopeRedefine.wacc") shouldBe 200
  }

}
