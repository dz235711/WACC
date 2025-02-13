package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidIoTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/IO/"

  it should "execute IOLoop.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "IOLoop.wacc", "") shouldBe Some("Please input an integer: echo input: 1\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\nPlease input an integer: echo input: 2\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\nPlease input an integer: echo input: 3\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\nPlease input an integer: echo input: 4\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\nPlease input an integer: echo input: 5\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\nPlease input an integer: echo input: 142\nDo you want to continue entering input?\n(enter Y for 'yes' and N for 'no')\n")
  }*/

  it should "execute IOSequence.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + "IOSequence.wacc", "") shouldBe Some("Please input an integer: You input: 37\n")
  }*/

}
