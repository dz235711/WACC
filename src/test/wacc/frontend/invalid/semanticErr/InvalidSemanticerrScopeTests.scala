package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/scope/"

  it should "pass badParentScope.wacc" in {
    frontendStatus(dir + "badParentScope.wacc") shouldBe 200
  }

  it should "pass badScopeRedefine.wacc" in {
    frontendStatus(dir + "badScopeRedefine.wacc") shouldBe 200
  }

}
