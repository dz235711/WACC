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
  private def parseTestFile(fileContents: String): (String, String, Option[Int]) = {
    val sections = fileContents.split("\n\n")

    var input = ""
    var output = ""
    var status: Option[Int] = None

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
              Some(s)
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
  val expectedExitStatus: Option[Int] = testData._3

  /** Runs a command in the assembler container
   *
   * @param command The command to run
   * @return The process builder for the command
   */
  private def dockerCommand(command: String): ProcessBuilder = {
    val pBuilder = new ProcessBuilder()
    pBuilder.command(
      "docker",
      "run",
      "--rm",
      "-v",
      s"${new java.io.File(".").getAbsolutePath}:/workspace",
      "--platform",
      "linux/amd64",
      "assembly-container",
      command
    )
  }

  /** Compiles the program and runs it with some input
   * 
   * @param input The input to the program
   * @return The exit status and output of the program
   */
  def run(input: String): (Option[Int], String) = {
    val prog = runFrontend(lines, false).getOrElse(throw new Exception("Compilation error"))

    // Write the program to a temporary file
    val source = writeFile("test.s", runBackend(prog, false))

    val pBuilder = new ProcessBuilder()

    // Assemble the program
    val assembleProcess =
      if sys.env.get("USE_DOCKER").contains("true") then dockerCommand("gcc -o test -z noexecstack test.s").start()
      else pBuilder.command("gcc", "-o", "test", "-z", "noexecstack", "test.s").start()

    // Check if the assembly was successful
    assembleProcess.waitFor()
    if (assembleProcess.exitValue() == 1)
      val assemblerOutput = new String(assembleProcess.getErrorStream.readAllBytes())
      throw new Exception(s"Failed to assemble with GCC: $assemblerOutput")

    // Run the program
    val command =
      val bashCmd = s"echo -e \"$input\" | ./test"
      if sys.env.get("USE_DOCKER").contains("true") then dockerCommand("timeout 1s " + bashCmd)
      else pBuilder.command("bash", "-c", s"timeout 1s $bashCmd")
    // Feed input to the process
    val process = command.start()

    // Read output
    val output = new String(process.getInputStream.readAllBytes())
    process.waitFor()

    // Get exit status
    val exitStatus = process.exitValue()

    // Delete the temporary files for assembly code and executable
    source.delete()
    val asmFile = new File("test")
    asmFile.delete()
    val flattenedAddrs = AddrRegex.replaceAllIn(output, "#addrs#")
    val flattenedErrs = ErrRegex.replaceAllIn(flattenedAddrs, "#runtime_error#")
    (Some(exitStatus), flattenedErrs)
  }
}
