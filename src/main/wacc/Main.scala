package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException
import WaccErrorBuilder.{format, setLines}

import parsley.errors.ErrorBuilder
import scala.collection.mutable.ListBuffer

def runFrontend(args: Array[String]): (Int, Either[String, List[WaccError]]) = {
  val verbose = args.contains("--verbose") || args.contains("-v")

  args.headOption match {
    case Some(path) =>
      try {
        val source = io.Source.fromFile(path)
        val listifiedLines = source.getLines().toList
        val lines = listifiedLines.mkString("\n")
        source.close()
        if (verbose)
          println("\n------------------------------ Input File ------------------------------")
          println(lines)
          println("------------------------------ /Input File ------------------------------\n")
        given ErrorBuilder[WaccError] = new WaccErrorBuilder
        parser.parse(lines) match {
          case Success(x) =>
            if (verbose)
              println("\n------------------------------ Pretty-Printed AST ------------------------------")
              println(prettyPrint(x))
              println("------------------------------ /Pretty-Printed AST ------------------------------\n")
            val errs = new ListBuffer[WaccError]()
            Renamer.rename(errs)(x)
            val listifiedErrs = errs.toList
            // println(listifiedErrs.map(e => setLines(format(e, Some(path), ErrType.Semantic), listifiedLines)).foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc)))
            if (listifiedErrs.nonEmpty)
              (200, Right(listifiedErrs.map(e => setLines(format(e, Some(path), ErrType.Semantic), listifiedLines))))
            else
              (0, Left("Parsed successfully! 🎉"))
          case Failure(err) =>
            if (verbose)
              println("Failed to parse! 😢")
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

  output match {
    case Left(msg) => println(msg)
    case Right(err) => println(err.foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc)))
  }
  exit(status)
}
