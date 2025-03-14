package wacc

import wacc.TypedAST.{Semi, Exit, Return, IntLiter}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import os.RelPath
import scala.collection.mutable.Map as MMap

class ExitTests extends AnyFlatSpec {
  val dir = os.pwd / RelPath("src/test/examples/custom/interpreter/exit/")

  it should "set the exit value" taggedAs Repl in {
    InterpreterTester(dir / "exitSimple.in").getResult()._3 shouldBe Some(7)
  }

  it should "not run any statements after exit" taggedAs Repl in {
    InterpreterTester(dir / "exitEarly.in").getResult() shouldBe (MapContext(), MapContext(), Some(0))
  }

  it should "not run any statements after exit even when in a function" taggedAs Repl in {
    val resultFuncScope = MapContext(
      MMap(
        1 -> (List(), Semi(Exit(IntLiter(0)), Return(IntLiter(23))))
      )
    )

    InterpreterTester(dir / "exitEarlyInFunc.in").getResult() shouldBe (MapContext(), resultFuncScope, Some(0))
  }

  it should "bind the exit value to least significant eight bits" taggedAs Repl in {
    InterpreterTester(dir / "exitLarge.in").getResult()._3 shouldBe Some(3)
  }
}
