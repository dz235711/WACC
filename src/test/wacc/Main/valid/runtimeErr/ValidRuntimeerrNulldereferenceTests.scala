package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ValidRuntimeerrNulldereferenceTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/runtimeErr/nullDereference/"

  it should "pass freeNull.wacc" in {
    runFrontend(Array(dir + "freeNull.wacc"))._1 shouldBe 0
  }

  it should "pass readNull1.wacc" in {
    runFrontend(Array(dir + "readNull1.wacc"))._1 shouldBe 0
  }

  it should "pass readNull2.wacc" in {
    runFrontend(Array(dir + "readNull2.wacc"))._1 shouldBe 0
  }

  it should "pass setNull1.wacc" in {
    runFrontend(Array(dir + "setNull1.wacc"))._1 shouldBe 0
  }

  it should "pass setNull2.wacc" in {
    runFrontend(Array(dir + "setNull2.wacc"))._1 shouldBe 0
  }

  it should "pass useNull1.wacc" in {
    runFrontend(Array(dir + "useNull1.wacc"))._1 shouldBe 0
  }

  it should "pass useNull2.wacc" in {
    runFrontend(Array(dir + "useNull2.wacc"))._1 shouldBe 0
  }

}
