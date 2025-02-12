package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "correctly execute ifNested1.wacc" in pending /*{
    fullExec(dir + ifNested1.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute ifNested2.wacc" in pending /*{
    fullExec(dir + ifNested2.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "correctly execute indentationNotImportant.wacc" in pending /*{
    fullExec(dir + indentationNotImportant.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute intsAndKeywords.wacc" in pending /*{
    fullExec(dir + intsAndKeywords.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute printAllTypes.wacc" in pending /*{
    fullExec(dir + printAllTypes.wacc, "") shouldBe Some("( [1, 2, 3] , [a, b, c] )\n[ #addrs# = (a, true), #addrs# = (b, false) ]\n1, 2\narray, of, strings\ntrue, false, true\nxyz\n1, 2, 3\nthis is a string\ntrue\nx\n5\n")
  }*/

  it should "correctly execute scope.wacc" in pending /*{
    fullExec(dir + scope.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute scopeBasic.wacc" in pending /*{
    fullExec(dir + scopeBasic.wacc, "") shouldBe Some("")
  }*/

  it should "correctly execute scopeIfRedefine.wacc" in pending /*{
    fullExec(dir + scopeIfRedefine.wacc, "") shouldBe Some("true\n12\n")
  }*/

  it should "correctly execute scopeRedefine.wacc" in pending /*{
    fullExec(dir + scopeRedefine.wacc, "") shouldBe Some("true\n2\n")
  }*/

  it should "correctly execute scopeSimpleRedefine.wacc" in pending /*{
    fullExec(dir + scopeSimpleRedefine.wacc, "") shouldBe Some("true\n12\n")
  }*/

  it should "correctly execute scopeVars.wacc" in pending /*{
    fullExec(dir + scopeVars.wacc, "") shouldBe Some("2\n4\n2\n")
  }*/

  it should "correctly execute scopeWhileNested.wacc" in pending /*{
    fullExec(dir + scopeWhileNested.wacc, "") shouldBe Some("counting... 5\ncounting... 4\ncounting... 3\ncounting... 2\ncounting... 1\n0 Boom!\n")
  }*/

  it should "correctly execute scopeWhileRedefine.wacc" in pending /*{
    fullExec(dir + scopeWhileRedefine.wacc, "") shouldBe Some("counting... 5\ncounting... 4\ncounting... 3\ncounting... 2\ncounting... 1\n0 Boom!\n")
  }*/

  it should "correctly execute splitScope.wacc" in pending /*{
    fullExec(dir + splitScope.wacc, "") shouldBe Some("3\n2\n")
  }*/

}