package wacc

import parsley.{Success, Failure}
import scala.sys.exit

def runFrontend(args: Array[String]): (Int, String) = {
//  args.headOption match {
//    case Some(expr) =>
//      parser.parse(expr) match {
//        case Success(x) => println(s"$expr = $x")
//        case Failure(msg) => println(msg)
//      }
//    case None => println("please enter an expression")
//  }
  (100, "Success!")
}

def main(args: Array[String]): Unit = {
  val (status, message) = runFrontend(args)
  println(message)
  exit(status)
}
