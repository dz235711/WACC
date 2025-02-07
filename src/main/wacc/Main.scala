package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException
import WaccErrorBuilder.{format, setLines}

import parsley.errors.ErrorBuilder

/** Manages syntax parsing and semantic analysis of a WACC program
   *
   * @param args  Cmdline arguments
   * @return (Exit code, Either[Exit message, List of errors])
   */
def runFrontend(args: Array[String]): (Int, Either[String, List[WaccError]]) = {
  // Determine whether to pretty print output
  val verbose = args.contains("--verbose") || args.contains("-v")

  // Match the first cmdline argument and try to parse it as file path
  args.headOption match {
    case Some(path) =>
      try {
        // Get file handle
        val source = io.Source.fromFile(path)

        // Read first as list so we can later set the lines in errors by index
        val listifiedLines = source.getLines().toList

        // Copy to a string to pass through parser
        val lines = listifiedLines.mkString("\n")
        source.close()

        // Prase the file
        if (verbose)
          println(
            "\n------------------------------ Input File ------------------------------"
          )
          println(lines)
          println(
            "------------------------------ /Input File ------------------------------\n"
          )
        given ErrorBuilder[WaccError] = new WaccErrorBuilder
        given errCtx: ErrorContext = new ErrorContext
        parser.parse(lines) match {
          case Success(x) =>
            // Successfully parsed so we conitnue to semantic analysis
            if (verbose)
              println(
                "\n------------------------------ Pretty-Printed AST ------------------------------"
              )
              println(prettyPrint(x))
              println(
                "------------------------------ /Pretty-Printed AST ------------------------------\n"
              )

            // Semantic analysis
            Renamer().rename(x)

            // Convert list buffer to list to allow mapping
            val listifiedErrs = errCtx.get
            if (listifiedErrs.nonEmpty)
              /* Return correctly formatted errors with lines set if error list
               * is nonempty */
              (
                200,
                Right(
                  listifiedErrs.map(e =>
                    setLines(
                      format(e, Some(path), ErrType.Semantic),
                      listifiedLines
                    )
                  )
                )
              )
            else
              // Otherwise semantic analysis passed
              (0, Left("Parsed successfully! 🎉"))
          case Failure(err) =>
            if (verbose)
              println("Failed to parse! 😢")
            // Return correctly formatted syntax error
            (100, Right(List(format(err, Some(path), ErrType.Syntax))))
        }
      } catch {
        case _: FileNotFoundException => (-1, Left(s"file not found: $path 💀"))
      }
    case None => (-2, Left("please enter a file path 😡"))
  }
}

def main(args: Array[String]): Unit = {
  println("Hello, WACC! 👋😃👍")
  val (status, output) = runFrontend(args)

  // Print output msg or errors to output stream
  output match {
    case Left(msg) => println(msg)
    case Right(err) =>
      println(
        err.foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc))
      )
  }

  // Exit with status code
  exit(status)
}
