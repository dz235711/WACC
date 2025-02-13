package wacc

import java.io.File
import java.lang.ProcessBuilder
import java.io.PrintWriter
import java.io.FileNotFoundException
import org.scalatest.Tag

object Frontend extends Tag("wacc.Frontend")
object Backend extends Tag("wacc.Backend")

private val TimeoutCode = 124

// Regex to match addresses in the output of the compiled program
private val addrRegex = "0x[0-9a-fA-F]+".r
private val errRegex = "fatal error:.+".r

/** Get the exit status of the frontend
 *
 * @param path Path to the file
 * @return The exit status of the frontend
 */
def frontendStatus(path: String): Int = partiallyCompile(path, _ => 0, status => status)

def fullExec(path: String, input: String): Option[String] =
  partiallyCompile(
    path,
    prog => Some(errRegex.replaceAllIn(addrRegex.replaceAllIn(runProgram(prog, input), "#addrs#"), "#runtime_error#")),
    _ => None
  )

/** Runs the frontend of the compiler
 *
 * @param path Path to the file
 * @param transformer Function to transform the typed AST
 * @param statuser Function to transform the error status code
 * @tparam T Return type of the transformer and statuser functions
 * @return The result of the transformer or statuser function
 */
private def partiallyCompile[T](path: String, transformer: TypedAST.Program => T, statuser: Int => T): T =
  readFile(path) match
    case None =>
      new FileNotFoundException()
      statuser(-1)
    case Some(lines) =>
      runFrontend(lines, false) match
        case Left(status, _) => statuser(status)
        case Right(prog)     => transformer(prog)

/** Runs backend of the compiler
 *
 * @param prog Typed AST of the program
 * @param input Input to the program
 * @return Output of the program
 */
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
  iStream.write(input.getBytes())
  iStream.flush()
  val output = process.getInputStream().readAllBytes().mkString
  process.waitFor()

  // If the process timed out, return a special string
  if process.exitValue() == TimeoutCode then "!!!process timed out!!!"
  else
    // Delete the temporary files for assembly code and executable
    source.delete()
    val asmFile = new File("test")
    asmFile.delete()
    output
}
