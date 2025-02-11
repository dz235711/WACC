package wacc

import parsley.{Failure, Success}
import WaccErrorBuilder.{format, setLines}
import parsley.errors.ErrorBuilder

/** Prints a message with a title if verbose mode is enabled
 *
 * @param verbose Whether verbose mode is enabled
 * @param title Title of the message
 * @param msg Message to print
 */
def printVerboseInfo(verbose: Boolean, title: String, msg: Any, colour: String): Unit = {
  if (verbose) {
    println(colour + Console.BOLD)

    val dashes = "-" * ((80 - title.length) / 2)
    println(dashes + title + dashes + "-" * ((80 - title.length) % 2))

    println(Console.RESET + colour)
    println(msg.toString)
    println(Console.RESET + colour + Console.BOLD)

    val dashes2 = "-" * ((80 - title.length - 1) / 2)
    println(dashes2 + "/" + title + dashes2 + "-" * ((80 - title.length - 1) % 2))

    println(Console.RESET)
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
  given errCtx: ErrorContext = new ErrorContext

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

    // Semantic analysis
    renamedAST = Renamer.rename(syntaxAST)
    _ = printVerboseInfo(verbose, "Renamed AST", renamedAST, Console.BLUE)

    typedAST = TypeChecker().checkProg(renamedAST)
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
