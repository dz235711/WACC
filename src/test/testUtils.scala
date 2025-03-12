package wacc

import java.io.FileNotFoundException
import org.scalatest.Tag

object Frontend extends Tag("wacc.Frontend")
object Backend extends Tag("wacc.Backend")
object Debug extends Tag("wacc.Debug")
object Imports extends Tag("wacc.Imports")

// Timeout code for the timeout command
val TimeoutCode = 124

/** Get the exit status of the frontend
 *
 * @param path Path to the file
 * @return The exit status of the frontend
 */
def frontendStatus(path: String): Int =
  readFile(path) match
    case None =>
      throw new FileNotFoundException()
    case Some(lines) =>
      runFrontend(lines, false).fold(status => status._1, _ => 0)

/** Compare the output of the frontend with the expected output
 *
 * @param path Path to the file
 * @param expected Expected output
 * @return Whether the output of the frontend matches the expected output
 */
def compareFrontend(path: String, expected: TypedAST.Program): Boolean =
  val programLines = readFile(path) match
    case None =>
      throw new FileNotFoundException()
    case Some(lines) =>
      lines
  val frontendOutput = runFrontend(programLines, false)
  frontendOutput match
    case Left(_) => false
    case Right(ast) =>
      ast.toString() == expected.toString()

/** Give error output for the frontend, with an empty string if there are no errors
 * 
 * @param path Path to the file
 * @return The error output of the frontend 
*/
def getFrontendErrors(path: String): String =
  runFrontend(readFile(path).get, false) match {
    case Left(err) =>
      val sb = new StringBuilder()
      err._2.foldRight(sb)((e, acc) => printWaccError(e, acc))
      sb.toString()
    case Right(ast) => ""
  }
