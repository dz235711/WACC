package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoPrintTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/print/"

  it should "execute hashInProgram.wacc" in pending /*{
    fullExec(dir + hashInProgram.wacc, "") shouldBe Some("We can print the hash character: #\nWe can also print # when its in a string.\n")
  }*/

  it should "execute multipleStringsAssignment.wacc" in pending /*{
    fullExec(dir + multipleStringsAssignment.wacc, "") shouldBe Some("s1 is Hi\ns2 is Hello\nThey are not the same string.\nNow make s1 = s2\ns1 is Hello\ns2 is Hello\nThey are the same string.\n")
  }*/

  it should "execute print-backspace.wacc" in pending /*{
    fullExec(dir + print-backspace.wacc, "") shouldBe Some("Hello World!\n")
  }*/

  it should "execute print.wacc" in pending /*{
    fullExec(dir + print.wacc, "") shouldBe Some("Hello World!")
  }*/

  it should "execute printBool.wacc" in pending /*{
    fullExec(dir + printBool.wacc, "") shouldBe Some("True is true\nFalse is false\n")
  }*/

  it should "execute printChar.wacc" in pending /*{
    fullExec(dir + printChar.wacc, "") shouldBe Some("A simple character example is f\n")
  }*/

  it should "execute printCharArray.wacc" in pending /*{
    fullExec(dir + printCharArray.wacc, "") shouldBe Some("hi!\n")
  }*/

  it should "execute printCharAsString.wacc" in pending /*{
    fullExec(dir + printCharAsString.wacc, "") shouldBe Some("foo\nbar\n")
  }*/

  it should "execute printEscChar.wacc" in pending /*{
    fullExec(dir + printEscChar.wacc, "") shouldBe Some("An escaped character example is "\n")
  }*/

  it should "execute printInt.wacc" in pending /*{
    fullExec(dir + printInt.wacc, "") shouldBe Some("An example integer is 189\n")
  }*/

  it should "execute println.wacc" in pending /*{
    fullExec(dir + println.wacc, "") shouldBe Some("Hello World!\n")
  }*/

}
