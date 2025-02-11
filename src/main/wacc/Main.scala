package wacc

import java.io.FileNotFoundException
import scala.sys.exit

/** Reads a file and returns a list of lines
 *
 * @param path Path to the file
 * @return List of lines in the file or None if the file is not found
 */
def readFile(path: String): Option[List[String]] = {
  try {
    val source = io.Source.fromFile(path)
    val lines = source.getLines().toList
    source.close()
    Some(lines)
  } catch {
    case _: FileNotFoundException => None
  }
}

def main(args: Array[String]): Unit = {
  println("Hello, WACC! 👋😃👍\n")
  val verbose = args.contains("--verbose") || args.contains("-v")

  val path = args.headOption

  // If no file path is provided, print usage message
  if (path.isEmpty || args.contains("--help") || args.contains("-h")) {
    println("Usage: wacc <file> [options]")
    println("Options:")
    println("  --verbose, -v  Print verbose output")
    println("  --help, -h     Print this message")
    exit(1)
  }

  // Read the file
  val file = readFile(path.get)
  if (file.isEmpty) {
    println(s"Error: File '${path.get}' not found")
    exit(1)
  }
  val lines = file.get

  // Run the frontend
  runFrontendPending(lines, verbose) match {
    case Right(program) =>
      // Run the backend
      val output = runBackend(program, verbose)
      println(output)
      exit(0)
    case Left((status, output)) =>
      // Print output msg or errors to output stream
      println(output.foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc)))
      // Exit with status code
      exit(status)
  }
}
