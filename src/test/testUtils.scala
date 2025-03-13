package wacc

import java.io.FileNotFoundException
import org.scalatest.Tag

object Frontend extends Tag("wacc.Frontend")
object Backend extends Tag("wacc.Backend")
object Repl extends Tag("wacc.Interpreter")
object Debug extends Tag("wacc.Debug")

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
