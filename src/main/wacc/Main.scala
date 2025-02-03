package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException

import parsley.errors.ErrorBuilder

def runFrontend(args: Array[String]): (Int, Either[String, WaccError]) = {
  val verbose = args.contains("--verbose") || args.contains("-v")

  args.headOption match {
    case Some(path) =>
      try {
        val source = io.Source.fromFile(path)
        val lines = source.mkString
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
            (0, Left("Parsed successfully! 🎉"))
          case Failure(err) =>
            if (verbose)
              println("Failed to parse! 😢")
            (100, Right(err))
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
    case Right(err) => printWaccError(err)
  }
  exit(status)
}
