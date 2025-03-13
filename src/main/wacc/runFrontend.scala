package wacc

import parsley.{Failure, Success}
import WaccErrorBuilder.{format, setLines, constructSpecialised}
import parsley.errors.ErrorBuilder

/** Recurses through all imports of the program and parses them
 * 
 * @param ast_ The program AST 
 * @return A list of either the imported ASTs or a list of errors
 * 
*/
private def getAllImports(ast_ : SyntaxAST.Program, filesImported: List[String])(implicit
    errBuilder: ErrorBuilder[WaccError],
    errCtx: ListContext[WaccError]
): List[Either[(Int, List[WaccError]), SyntaxAST.Program]] = {
  ast_.imports.flatMap { (file: SyntaxAST.Import) =>
    readFile(file.filename.s) match {
      case Some(importedLines) =>
        parser.parse(importedLines.mkString("\n")) match {
          case Success(ast) =>
            if (filesImported.contains(file.filename.s))
              println("got here")
              getAllImports(ast, filesImported)
            else Right(ast) :: getAllImports(ast, file.filename.s :: filesImported)
          case Failure(err) => Left((100, List(format(err, None, ErrType.Syntax)))) :: Nil
        }
      case None =>
        Left(
          (100, List(constructSpecialised(file.pos, file.filename.s.length, s"File not found: ${file.filename.s}")))
        ) :: Nil
    }
  }
}

/** Runs the frontend of the compiler (parser, renamer, type checker)
 *
 * @param linesList List of lines from the input file
 * @param verbose Whether verbose mode is enabled
 * @return Either a tuple of the error status code and a list of errors or the typed AST
 */
def runFrontend(linesList: List[String], verbose: Boolean): Either[(Int, List[WaccError]), TypedAST.Program] = {
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
    imports = getAllImports(syntaxAST, List.empty)
    _ = printVerboseInfo(verbose, "Imports", imports, Console.CYAN)

    // Get the imported ASTs and report any errors in included files
    (importErrs, importASTs) = imports.partitionMap(identity)
    _ <- if (importErrs.nonEmpty) Left((100, importErrs.flatMap(_._2))) else Right(importASTs)

    // Semantic analysis
    renamedAST = Renamer.rename(syntaxAST, importASTs)
    _ = printVerboseInfo(verbose, "Renamed AST", renamedAST, Console.BLUE)

    // Dead code elimination
    eliminatedAST = DeadCodeRemover().removeDeadCode(renamedAST)
    _ = printVerboseInfo(verbose, "Dead Code Eliminated AST", eliminatedAST, Console.CYAN)

    typedAST = TypeChecker().checkProg(eliminatedAST)
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
