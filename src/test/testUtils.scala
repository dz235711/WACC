package wacc

import java.io.File
import java.lang.ProcessBuilder
import java.io.PrintWriter
import java.io.FileNotFoundException
import org.scalatest.Tag

object Frontend extends Tag("wacc.Frontend")
object Backend extends Tag("wacc.Backend")

private val TimeoutCode = 124

private val addrRegex = "0x[0-9a-fA-F]+".r
private val errRegex = "fatal error:.+".r

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
  // Create temporary file to write compiled assembly into
  val source = new File("test.s")
  source.createNewFile()

  // Write assembly
  val writer = new PrintWriter(source)
  writer.write(runBackend(prog, false))
  writer.flush()
  writer.close()

  // Create the processes to assemble and run the compiled assembly
  val pBuilder = new ProcessBuilder()
  pBuilder.command("gcc", "-o", "test", "-z", "noexecstack", "test.s").start().waitFor()
  val process = pBuilder.command("timeout", "1s", "./test").start()
  // Feed input to the process
  val iStream = process.getOutputStream()
  process.waitFor()
  if process.exitValue() == TimeoutCode then "!!!process timed out!!!"
  else
    // If the process hasn't t
    iStream.write(input.getBytes())
    iStream.flush()
    val output = process.getInputStream().readAllBytes().mkString
    process.waitFor()

    // Delete the temporary files for assembly code and executable
    source.delete()
    val asmFile = new File("test")
    asmFile.delete()
    output
}
