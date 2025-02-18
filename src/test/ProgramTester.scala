package wacc

import java.io.{File, FileNotFoundException, PrintWriter}

// Regex to match addresses in the output of the compiled program
private val AddrRegex = "0x[0-9a-fA-F]+".r
private val ErrRegex = "fatal error:.+".r
private val HeaderRegex = "# (.*?):.*$".r

/** Class to test a program's execution
 * 
 * @param path The path to the test file
 */
class ProgramTester(path: String) {
  private val lines = readFile(path).getOrElse(throw new FileNotFoundException())

  /** Parses the test file and returns the input, output, and exit status
   * 
   * @param fileContents The contents of the test file
   * @return A tuple of the input, output, and exit status
   */
  private def parseTestFile(fileContents: String): (String, String, Int) = {
    val sections = fileContents.split("\n\n")

    var input = ""
    var output = ""
    var status = 0

    for (section <- sections) {
      val lines = section.split("\n")

      val header = lines(0)
      val content = lines.slice(1, lines.length).map(l => l.slice(2, l.length)).mkString("\n")

      header match {
        case HeaderRegex("Input")  => input = header.slice("# Input: ".length, header.length)
        case HeaderRegex("Output") => output = content
        case HeaderRegex("Exit") =>
          status = content.toIntOption match
            case Some(s) =>
              if (s == TimeoutCode) throw new Exception("Invalid exit status")
              s
            case None => throw new Exception("Invalid exit status")
        case _ => ()
      }
    }

    (input, output, status)
  }

  private val testData = parseTestFile(lines.mkString("\n"))

  /** The input specified in the test file ("" if not specified) */
  val testInput: String = testData._1

  /** The expected output specified in the test file ("" if not specified) */
  val expectedOutput: String = testData._2

  /** The expected exit status specified in the test file (0 if not specified) */
  val expectedExitStatus: Int = testData._3

  /** Compiles the program and runs it with some input
   * 
   * @param input The input to the program
   * @return The exit status and output of the program
   */
  def run(input: String): (Int, String) = {
    val prog = runFrontend(lines, false).getOrElse(throw new Exception("Compilation error"))

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

    // Assemble the file with GCC
    val assembleProcess = pBuilder.command("gcc", "-o", "test", "-z", "noexecstack", "test.s").start()
    assembleProcess.waitFor()
    if (assembleProcess.exitValue() == 1)
      throw new Exception("Failed to assemble with GCC")

    val process = pBuilder.command("timeout", "1s", "./test").start()

    // Feed input to the process
    val iStream = process.getOutputStream
    iStream.write(input.getBytes())
    iStream.flush()
    val output = process.getInputStream.readAllBytes().mkString
    process.waitFor()

    val exitStatus = process.exitValue()

    // Delete the temporary files for assembly code and executable
    source.delete()
    val asmFile = new File("test")
    asmFile.delete()
    val flattenedAddrs = AddrRegex.replaceAllIn(output, "#addrs#")
    val flattenedErrs = ErrRegex.replaceAllIn(flattenedAddrs, "#runtime_error#")
    (exitStatus, flattenedErrs)
  }
}
