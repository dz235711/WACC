package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ImportsTests extends AnyFlatSpec {
  val dir = "src/test/examples/custom/imports/"
  val FrontendErrorMessage = "Frontend failed to run"

  // Testing whether a file can be imported, but not used
  it should "provide unchanged AST when importing a file without using it" taggedAs (Frontend, Imports) in pending /*{
    val programFile = readFile(dir + "importUnused.wacc") match {
      case None        => throw new FileNotFoundException()
      case Some(lines) => lines
    }
    val programAST = runFrontend(programFile, false) match {
      case Left(err)    =>
        val sb = new StringBuilder()
        err._2.foldRight(sb)((e, acc) => printWaccError(e, acc))
        throw new Exception(sb.toString())
      case Right(ast) => ast
    }
    compareFrontend(dir + "progWithoutImport.wacc", programAST) shouldBe true
  }*/ // TODO: See if this test is needed/a better way to compare frontend

  // Testing whether a file can be imported and have functions used from it
  it should "frontend analyse importAndUse.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "importAndUse.wacc") shouldBe 0
  }

  // Testing whether multiple files can be imported
  it should "frontend analyse multipleImports.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "multipleImports.wacc") shouldBe 0
  }

  // Testing whether circular dependencies result in an error
  it should "frontend analyse circularDependency.wacc" taggedAs (Frontend, Imports) in pending /*{
    frontendStatus(dir + "circularDependency.wacc") should not be 0
  } */ // TODO: Finish after separate import stage is merged in

  // Testing whether an error is flagged for incorrect file format

  // Testing whether nested imports work
  it should "frontend analyse nestedImports.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "nestedImports.wacc") shouldBe 0
  }

  // Testing whether an error is flagged for a non-existent file
  it should "flag an error for a non-existent file" taggedAs (Frontend, Imports) in {
    getFrontendErrors(dir + "nonExistentImport.wacc") should include("File not found")
  }

  // Testing behaviour when a file is imported multiple times

  // Testing an error for if a function in a different file shares the same name as a function in the current file

  // Testing whether a function with colliding names can be used if the file is specified i.e., call example.f()

  // Testing whether an error is flagged if an imported file has syntax/semantic errors

  // Testing whether an error is flagged if a file imports itself
}
