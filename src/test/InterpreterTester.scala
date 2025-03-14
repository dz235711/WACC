package wacc

import os.SubProcess.{InputStream, OutputStream}
import os.Path
import java.io.{BufferedInputStream, ByteArrayOutputStream}
import java.io.FileInputStream
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Future

class InterpreterTester(path: Path) {
  private val WaitTime = 3.seconds

  private val lines = BufferedInputStream(FileInputStream(path.toIO))
  val testOut = ByteArrayOutputStream()
  private val testResult = Future(runInterpreter())

  private def runInterpreter(): (InterpreterVariableScope, InterpreterFunctionScope, Option[Int]) = {
    interpreterMain(Nil)(using InputStream(testOut), OutputStream(lines))
  }

  def getResult(): (InterpreterVariableScope, InterpreterFunctionScope, Option[Int]) =
    Await.result(testResult, WaitTime)
}
