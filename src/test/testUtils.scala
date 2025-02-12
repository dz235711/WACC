package wacc

import java.io.File
import java.lang.ProcessBuilder
import java.io.PrintWriter
import java.io.FileNotFoundException

val addrRegex = "0x[0-9a-fA-F]+".r
val errRegex = "fatal error:.+".r

def frontendStatus(path: String): Int = partiallyCompile(path, _ => 0, status => status)

def fullExec(path: String, input: String): Option[String] =
  partiallyCompile(
    path,
    prog => Some(errRegex.replaceAllIn(addrRegex.replaceAllIn(runProgram(prog, input), "#addrs#"), "#runtime_error#")),
    _ => None
  )

private def partiallyCompile[T](path: String, transformer: TypedAST.Program => T, statuser: Int => T): T =
  readFile(path) match
    case None =>
      new FileNotFoundException()
      statuser(-1)
    case Some(lines) =>
      runFrontend(lines, false) match
        case Left(status, _) => statuser(status)
        case Right(prog)     => transformer(prog)

private def runProgram(prog: TypedAST.Program, input: String): String = {
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
