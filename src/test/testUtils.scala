package wacc

import java.io.File
import java.lang.ProcessBuilder
import java.io.PrintWriter
import java.io.FileNotFoundException

def frontendStatus(path: String): Int = {
  readFile(path) match
    case None =>
      new FileNotFoundException()
      -1
    case Some(value) =>
      runFrontend(value, false) match
        case Left(status, _) => status
        case Right(value)    => 0

}

def runProgram(prog: TypedAST.Program, input: String): String = {
  val source = new File("test.s")
  source.createNewFile()
  val writer = new PrintWriter(source)
  writer.write(runBackend(prog, false))
  writer.flush()
  writer.close()
  val pBuilder = new ProcessBuilder()
  pBuilder.command("gcc", "-o", "test", "-z", "noexecstack", "test.s").start().waitFor()
  val process = pBuilder.command("./test").start()
  val iStream = process.getOutputStream()
  iStream.write(input.getBytes())
  iStream.flush()
  val output = process.getInputStream().readAllBytes().mkString
  process.waitFor()
  source.delete()
  val asmFile = new File("test")
  asmFile.delete()
  output
}
