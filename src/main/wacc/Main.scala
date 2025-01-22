package wacc

import parsley.{Success, Failure}
import java.io.FileNotFoundException

def main(args: Array[String]): Unit = {
  println("hello WACC!")

  args.headOption match {
    case Some(path) =>
      try {
        val source = io.Source.fromFile(path)
        val lines = source.mkString
        source.close()

        println(s"compiling ${lines.length} lines from $path")
        parser.parse(source.mkString) match {
          case Success(x)   => println(s"$path = $x")
          case Failure(msg) => println(msg)
        }
      } catch {
        case e: FileNotFoundException => println(s"file not found: $path")
      }
    case None => println("please enter a file path")
  }

  println("goodbye WACC!")
}
