package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class ImportsTests extends AnyFlatSpec {
  val dir = "src/test/examples/custom/imports/"
  val FrontendErrorMessage = "Frontend failed to run"

  // Testing whether a file can be imported, but not used
  it should "provide unchanged AST when importing a file without using it" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "progWithoutImport.wacc") shouldBe 0
  }

  // Testing whether a file can be imported and have functions used from it
  it should "frontend analyse importAndUse.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "importAndUse.wacc") shouldBe 0
  }

  // Testing whether multiple files can be imported
  it should "frontend analyse multipleImports.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "multipleImports.wacc") shouldBe 0
  }

  // Testing whether circular dependencies succeed
  it should "frontend analyse circularDependency.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "circularDependency.wacc") shouldBe 0
  }

  // Testing whether an error is flagged for incorrect file format
  it should "flag an error for an incorrect file format" taggedAs (Frontend, Imports) in {
    getFrontendErrors(dir + "incorrectFileFormat.wacc") should include("Imported files must be WACC files")
  }

  // Testing whether nested imports work
  it should "frontend analyse nestedImports.wacc" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "nestedImports.wacc") shouldBe 0
  }

  // Testing whether an error is flagged for a non-existent file
  it should "flag an error for a non-existent file" taggedAs (Frontend, Imports) in {
    getFrontendErrors(dir + "nonExistentImport.wacc") should include("File not found")
  }

  // Testing whether an error is flagged if the same file is imported multiple times
  it should "allow for the same file to be imported multiple times" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "importsSameFile.wacc") shouldBe 0
  }

  // Testing an error for if a function in a different file shares the same name as a function in the current file
  it should "flag an error for an illegal function re-declaration when imported" taggedAs (Frontend, Imports) in {
    getFrontendErrors(dir + "illegalFunctionRedeclaration.wacc") should include("Illegal function redeclaration")
  }

  // Testing whether an error is flagged if an imported file has syntax errors
  it should "flag an error for an imported file with syntax errors" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "importSyntaxError.wacc") shouldBe 100
  }

  // Testing whether an error is flagged if a file imports itself
  it should "flag an error for a file importing itself" taggedAs (Frontend, Imports) in {
    frontendStatus(dir + "importsItself.wacc") shouldBe 0
  }

  // Testing whether the syntax error is reported to be in the imported file if there is one
  it should "report the syntax error in the imported file" taggedAs (Frontend, Imports) in {
    getFrontendErrors(dir + "importSyntaxError.wacc") should include("syntaxError.wacc")
  }
}
