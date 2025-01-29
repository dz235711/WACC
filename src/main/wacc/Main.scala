package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException

def runFrontend(args: Array[String]): (Int, String) = {
  println("Hello, WACC! 👋😃👍\n")

  args.headOption match {
    case Some(path) =>
      try {
        val source = io.Source.fromFile(path)
        val lines = source.mkString
        source.close()

        println("------------------------------ Input File ------------------------------")
        println(lines)
        println("------------------------------ /Input File ------------------------------")
        parser.parse(lines) match {
          case Success(x)   =>
            println("\nParsed successfully! 🎉\n")
            println("------------------------------ Pretty-Printed AST ------------------------------")
            println(prettyPrint(x))
            println("------------------------------ /Pretty-Printed AST ------------------------------")
            (0, "Success!")
          case Failure(msg) =>
            println("\nFailed to parse! 😢\n")
            (100, msg)
        }

      } catch {
        case _: FileNotFoundException => (-1, s"file not found: $path")
      }
    case None => (-1, "please enter a file path")
  }
}

def main(args: Array[String]): Unit = {
  val (status, message) = runFrontend(args)
  println(message)
  exit(status)
}
