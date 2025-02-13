package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidScopeTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/scope/"

  it should "execute ifNested1.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + ifNested1.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute ifNested2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + ifNested2.wacc, "") shouldBe Some("correct\n")
  }*/

  it should "execute indentationNotImportant.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + indentationNotImportant.wacc, "") shouldBe Some("")
  }*/

  it should "execute intsAndKeywords.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + intsAndKeywords.wacc, "") shouldBe Some("")
  }*/

  it should "execute printAllTypes.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + printAllTypes.wacc, "") shouldBe Some("( [1, 2, 3] , [a, b, c] )\n[ #addrs# = (a, true), #addrs# = (b, false) ]\n1, 2\narray, of, strings\ntrue, false, true\nxyz\n1, 2, 3\nthis is a string\ntrue\nx\n5\n")
  }*/

  it should "execute scope.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scope.wacc, "") shouldBe Some("")
  }*/

  it should "execute scopeBasic.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeBasic.wacc, "") shouldBe Some("")
  }*/

  it should "execute scopeIfRedefine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeIfRedefine.wacc, "") shouldBe Some("true\n12\n")
  }*/

  it should "execute scopeRedefine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeRedefine.wacc, "") shouldBe Some("true\n2\n")
  }*/

  it should "execute scopeSimpleRedefine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeSimpleRedefine.wacc, "") shouldBe Some("true\n12\n")
  }*/

  it should "execute scopeVars.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeVars.wacc, "") shouldBe Some("2\n4\n2\n")
  }*/

  it should "execute scopeWhileNested.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeWhileNested.wacc, "") shouldBe Some("counting... 5\ncounting... 4\ncounting... 3\ncounting... 2\ncounting... 1\n0 Boom!\n")
  }*/

  it should "execute scopeWhileRedefine.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + scopeWhileRedefine.wacc, "") shouldBe Some("counting... 5\ncounting... 4\ncounting... 3\ncounting... 2\ncounting... 1\n0 Boom!\n")
  }*/

  it should "execute splitScope.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + splitScope.wacc, "") shouldBe Some("3\n2\n")
  }*/

}
