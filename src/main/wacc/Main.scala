package wacc

import parsley.{Success, Failure}
import scala.sys.exit
import java.io.FileNotFoundException

def runFrontend(args: Array[String]): (Int, String) = {
  args.headOption match {
    case Some(path) =>
      try {
        val source = io.Source.fromFile(path)
        val lines = source.mkString

        println(s"compiling ${lines.length} lines from $path")
        parser.parse(lines) match {
          case Success(x)   => println(s"$path = $x")
          case Failure(msg) => println(msg)
        }

        source.close()
      } catch {
        case e: FileNotFoundException => println(s"file not found: $path")
      }
    case None => println("please enter a file path")
  }

  println("goodbye WACC!")
  (100, "Success!")
}

def main(args: Array[String]): Unit = {
  val (status, message) = runFrontend(args)
  println(message)
  exit(status)
}
