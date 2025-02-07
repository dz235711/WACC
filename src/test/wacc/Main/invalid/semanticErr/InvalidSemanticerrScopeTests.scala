package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class InvalidSemanticerrScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/invalid/semanticErr/scope/"

  it should "pass badParentScope.wacc" in {
    runFrontend(Array(dir + "badParentScope.wacc"))._1 shouldBe 200
  }

  it should "pass badScopeRedefine.wacc" in {
    runFrontend(Array(dir + "badScopeRedefine.wacc"))._1 shouldBe 200
  }

}
