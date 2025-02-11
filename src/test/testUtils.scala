package wacc

import java.io.File
import java.lang.ProcessBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

def readFile(path: String): List[String] = {
  val source = scala.io.Source.fromFile(path)
  val lines = source.getLines.toList
  source.close()
  lines
}

def runProgram(prog: TypedAST.Program): String = {
  val source = new File("test.s")
  source.createNewFile()
  val writer = new PrintWriter(source)
  writer.write(runBackend(prog))
  writer.flush()
  writer.close()
  val pBuilder = new ProcessBuilder()
  pBuilder.command("gcc", "-o", "test", "-z", "noexecstack", "test.s").start().waitFor()
  val process = pBuilder.command("./test").start()
  val output = process.getInputStream().readAllBytes().mkString
  process.waitFor()
  source.delete()
  val asmFile = new File("test")
  asmFile.delete()
  output
}
