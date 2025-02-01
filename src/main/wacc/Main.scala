package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException

def runFrontend(args: Array[String]): (Int, String) = {
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
        parser.parse(lines) match {
          case Success(x)   =>
            if (verbose)
              println("\n------------------------------ Pretty-Printed AST ------------------------------")
              println(prettyPrint(x))
              println("------------------------------ /Pretty-Printed AST ------------------------------\n")
            (0, "Parsed successfully! 🎉")
          case Failure(msg) =>
            if (verbose)
              println("Failed to parse! 😢")
            (100, msg)
        }

      } catch {
        case _: FileNotFoundException => (-1, s"file not found: $path 💀")
      }
    case None => (-1, "please enter a file path 😡")
  }
}

def main(args: Array[String]): Unit = {
  println("Hello, WACC! 👋😃👍")
  val (status, message) = runFrontend(args)
  println(message)
  exit(status)
}
