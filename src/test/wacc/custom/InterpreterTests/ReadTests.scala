package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import scala.collection.mutable.Map as MMap
import os.RelPath

class ReadTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/read/")

  it should "read int" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 13
      )
    )

    InterpreterTester(dir / "readInt.in").getResult()._1 shouldBe resultScope
  }

  it should "read char" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> '9'
      )
    )

    InterpreterTester(dir / "readChar.in").getResult()._1 shouldBe resultScope
  }

  it should "not read non-ascii char input" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> '-'
      )
    )

    InterpreterTester(dir / "readBadAscii.in").getResult()._1 shouldBe resultScope
  }

  it should "not read bad int input" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 72
      )
    )

    InterpreterTester(dir / "readBadInt.in").getResult()._1 shouldBe resultScope
  }

  it should "not read bad char input" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 'h'
      )
    )

    InterpreterTester(dir / "readBadChar.in").getResult()._1 shouldBe resultScope
  }

  it should "not read empty input" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> 59
      )
    )

    InterpreterTester(dir / "readEmpty.in").getResult()._1 shouldBe resultScope
  }
}
