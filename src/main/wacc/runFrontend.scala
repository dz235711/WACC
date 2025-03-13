package wacc

import parsley.{Failure, Success}
import WaccErrorBuilder.{constructSpecialised, format, setLines}
import parsley.errors.ErrorBuilder

/** Recurses through all imports of the program and parses them
 * 
 * @param ast The main program AST
 * @return Either a list of errors (syntax) or a list of the imported syntax ASTs
 * 
*/
private def getAllImports(ast: SyntaxAST.Program, imported: Set[String])(implicit
    errBuilder: ErrorBuilder[WaccError],
    errCtx: ListContext[WaccError]
): Either[(Int, List[WaccError]), List[SyntaxAST.Program]] = {
  // Get this file's imports
  val imports = ast.imports

  // Filter out already imported files
  val newImports = imports.filterNot(file => imported.contains(file.filename.s)).toSet

  // Add the new imports to the set of imported files
  val allImports = imported ++ newImports.map(_.filename.s)

  newImports.foldLeft(Right(Nil): Either[(Int, List[WaccError]), List[SyntaxAST.Program]]) { (acc, importFile) =>
    for {
      // Check that the file is a WACC file
      _ <-
        if (importFile.filename.s.endsWith(".wacc")) Right(())
        else
          Left(
            (
              100,
              List(
                constructSpecialised(
                  importFile.pos,
                  importFile.filename.s.length,
                  s"Imported files must be WACC files"
                )
              )
            )
          )

      // Read the file
      file <- readFile(importFile.filename.s).toRight(
        (
          100,
          List(
            constructSpecialised(
              importFile.pos,
              importFile.filename.s.length,
              s"File not found: ${importFile.filename.s}"
            )
          )
        )
      )

      // Parse the file
      importedAst <- parser.parse(file.mkString("\n")) match {
        case Success(ast) => Right(ast)
        case Failure(err) => Left((100, List(format(err, Some(importFile.filename.s), ErrType.Syntax))))
      }

      // Recurse through the imports
      res <- getAllImports(importedAst, allImports)
    } yield importedAst :: (res ++ acc.getOrElse(Nil))
  }
}

/** Runs the frontend of the compiler (parser, renamer, type checker)
 *
 * @param linesList List of lines from the input file
 * @param verbose Whether verbose mode is enabled
 * @param path The path of the file being compiled
 * @return Either a tuple of the error status code and a list of errors or the typed AST
 */
def runFrontend(
    linesList: List[String],
    verbose: Boolean,
    path: String
): Either[(Int, List[WaccError]), TypedAST.Program] = {
  val lines = linesList.mkString("\n")

  given ErrorBuilder[WaccError] = new WaccErrorBuilder
  given errCtx: ListContext[WaccError] = new ListContext()

  // Print input
  printVerboseInfo(verbose, "Input", lines, Console.YELLOW)

  for {
    // Parse the file
    syntaxAST <- parser.parse(lines) match {
      case Success(ast) => Right(ast)
      case Failure(err) => Left((100, List(format(err, None, ErrType.Syntax))))
    }

    // Print pretty-printed AST
    _ = printVerboseInfo(verbose, "Pretty-Printed AST", prettyPrint(syntaxAST), Console.GREEN)

    // Parse the imported files
    importASTs <- getAllImports(syntaxAST, Set(path))
    _ = printVerboseInfo(verbose, "Imports", importASTs, Console.CYAN)

    // Semantic analysis
    renamedAST = Renamer.rename(syntaxAST, imports = importASTs)._1
    _ = printVerboseInfo(verbose, "Renamed AST", renamedAST, Console.BLUE)

    // Dead code elimination
    eliminatedAST = DeadCodeRemover().removeDeadCode(renamedAST)
    _ = printVerboseInfo(verbose, "Dead Code Eliminated AST", eliminatedAST, Console.CYAN)

    typedAST = TypeChecker().checkProg(eliminatedAST)._1
    _ = printVerboseInfo(verbose, "Typed AST", typedAST, Console.MAGENTA)

    // Convert list buffer to list to allow mapping
    errsList = errCtx.get
    result <-
      if (errsList.nonEmpty)
        Left((200, errsList.map(e => setLines(format(e, None, ErrType.Semantic), linesList))))
      else
        Right(typedAST)
  } yield result
}
