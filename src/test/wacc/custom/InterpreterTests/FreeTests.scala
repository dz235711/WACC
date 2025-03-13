package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath
import scala.collection.mutable.Map as MMap
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.ListBuffer

class FreeTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/free/")

  it should "not allow fst on free pairs" taggedAs Repl in {
    Try(InterpreterTester(dir / "freePairFst.in").getResult()) match {
      case Success(_) => fail("The interpreter should have thrown an exception!")
      case Failure(_) => ()
    }
  }

  it should "not allow snd on free pairs" taggedAs Repl in {
    Try(InterpreterTester(dir / "freePairSnd.in").getResult()) match {
      case Success(_) => fail("The interpreter should have thrown an exception!")
      case Failure(_) => ()
    }
  }

  it should "not allow array access on freed array" taggedAs Repl in {
    Try(InterpreterTester(dir / "freeArr.in").getResult()) match {
      case Success(_) => fail("The interpreter should have thrown an exception!")
      case Failure(_) => ()
    }
  }

  it should "allow reassignment of freed pair" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> PairValue("would be consistent with", "if statements")
      )
    )

    InterpreterTester(dir / "freeReasgnPair.in").getResult()._1 shouldBe resultScope
  }

  it should "allow reassignment of free array" taggedAs Repl in {
    val resultScope = MapContext(
      MMap(
        1 -> ArrayValue(ListBuffer(4, 5, 6, 7, 8))
      )
    )

    InterpreterTester(dir / "freeReasgnArr.in").getResult()._1 shouldBe resultScope
  }

  it should "not allow freeing of null" taggedAs Repl in {
    Try(InterpreterTester(dir / "freeNull.in").getResult()) match {
      case Success(_) => fail("The interpreter should have thrown an exception!")
      case Failure(_) => ()
    }
  }
}
