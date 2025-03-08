package wacc

import java.io.{File, FileNotFoundException, PrintWriter}
import scala.sys.exit

/** Prints a message with a title if verbose mode is enabled
 *
 * @param verbose Whether verbose mode is enabled
 * @param title   Title of the message
 * @param msg     Message to print
 * @param colour  Colour of the message
 */
def printVerboseInfo(verbose: Boolean, title: String, msg: Any, colour: String): Unit = {
  val HEADER_SIZE = 80
  if (verbose) {
    println(colour + Console.BOLD)

    // Prints "---- title ----" with total length of HEADER_SIZE
    val dashes = "-" * ((HEADER_SIZE - title.length) / 2)
    println(dashes + title + dashes + "-" * ((HEADER_SIZE - title.length) % 2))

    println(Console.RESET + colour)
    println(msg.toString)
    println(Console.RESET + colour + Console.BOLD)

    // Prints "---- /title ----" with total length of HEADER_SIZE
    val dashes2 = "-" * ((HEADER_SIZE - title.length - 1) / 2)
    println(dashes2 + "/" + title + dashes2 + "-" * ((HEADER_SIZE - title.length - 1) % 2))

    println(Console.RESET)
  }
}

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

/** Writes a string to a file
 *
 * @param path Path to the file
 * @param content Content to write to the file
 * @return The file that was written to
 */
def writeFile(path: String, content: String): File = {
  val source = new File(path)
  source.createNewFile()

  val writer = new PrintWriter(source)
  writer.write(content)
  writer.flush()
  writer.close()
  source
}

def main(args: Array[String]): Unit = {
  println("Hello, WACC! 👋😃👍\n")
  val verbose = args.contains("--verbose") || args.contains("-v")
  val enterInterpreter = args.contains("--interpreter") || args.contains("-I")

  // Enter the interpreter main function if the interpreter flag is set
  if (enterInterpreter) {
    interpreterMain()
    exit(0)
  }

  val path = args.headOption

  // If no file path is provided, print usage message
  if (path.isEmpty || args.contains("--help") || args.contains("-h")) {
    println("Usage: wacc <file> [options]")
    println("Options:")
    println("  --verbose, -v  Print verbose output")
    println("  --help, -h     Print this message")
    println("  --interpreter, -I, Launch the interpreter")
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
  runFrontend(lines, verbose) match {
    case Right(program) =>
      // Run the backend
      val assembly = runBackend(program, verbose)
      // Output the assembly file
      val filename = path.get.split("/").last.split("\\.").head + ".s"
      writeFile(filename, assembly)
      exit(0)
    case Left((status, output)) =>
      // Print output msg or errors to output stream
      println(output.foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc)))
      // Exit with status code
      exit(status)
  }
}
