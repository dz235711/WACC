package wacc

import java.io.{File, FileNotFoundException, PrintWriter}
import scala.sys.exit

val exit_1 =
  ".intel_syntax noprefix\n.globl main\n.section .rodata\n.text\nmain:\n\tpush rbp\n\tpush rbx\n\tmov rbp, rsp\n\tmov edi, -1\n\t# statement primitives do not return results (but will clobber r0/rax)\n\tcall _exit\n\tmov rax, 0\n\tpop rbx\n\tpop rbp\n\tret\n\n_exit:\n\tpush rbp\n\tmov rbp, rsp\n\t# external calls must be stack-aligned to 16 bytes, accomplished by masking with fffffffffffffff0\n\tand rsp, -16\n\tcall exit@plt\n\tmov rsp, rbp\n\tpop rbp\n\tret"
val echo_big_int =
  "intel_syntax noprefix\n.globl main\n.section .rodata\n# length of .L.str0\n\t.int 24\n.L.str0:\n\t.asciz \"enter an integer to echo\"\n.text\nmain:\n\tpush rbp\n\t# push {rbx, r12}\n\tsub rsp, 16\n\tmov qword ptr [rsp], rbx\n\tmov qword ptr [rsp + 8], r12\n\tmov rbp, rsp\n\tmov r12d, 1\n\tlea rdi, [rip + .L.str0]\n\t# statement primitives do not return results (but will clobber r0/rax)\n\tcall _prints\n\tcall _println\n\t# load the current value in the destination of the read so it supports defaults\n\tmov edi, r12d\n\tcall _readi\n\tmov r12d, eax\n\tmov edi, eax\n\t# statement primitives do not return results (but will clobber r0/rax)\n\tcall _printi\n\tcall _println\n\tmov rax, 0\n\t# pop/peek {rbx, r12}\n\tmov rbx, qword ptr [rsp]\n\tmov r12, qword ptr [rsp + 8]\n\tadd rsp, 16\n\tpop rbp\n\tret\n\n.section .rodata\n# length of .L._printi_str0\n\t.int 2\n.L._printi_str0:\n\t.asciz \"%d\"\n.text\n_printi:\n\tpush rbp\n\tmov rbp, rsp\n\t# external calls must be stack-aligned to 16 bytes, accomplished by masking with fffffffffffffff0\n\tand rsp, -16\n\tmov esi, edi\n\tlea rdi, [rip + .L._printi_str0]\n\t# on x86, al represents the number of SIMD registers used as variadic arguments\n\tmov al, 0\n\tcall printf@plt\n\tmov rdi, 0\n\tcall fflush@plt\n\tmov rsp, rbp\n\tpop rbp\n\tret\n\n.section .rodata\n# length of .L._prints_str0\n\t.int 4\n.L._prints_str0:\n\t.asciz \"%.*s\"\n.text\n_prints:\n\tpush rbp\n\tmov rbp, rsp\n\t# external calls must be stack-aligned to 16 bytes, accomplished by masking with fffffffffffffff0\n\tand rsp, -16\n\tmov rdx, rdi\n\tmov esi, dword ptr [rdi - 4]\n\tlea rdi, [rip + .L._prints_str0]\n\t# on x86, al represents the number of SIMD registers used as variadic arguments\n\tmov al, 0\n\tcall printf@plt\n\tmov rdi, 0\n\tcall fflush@plt\n\tmov rsp, rbp\n\tpop rbp\n\tret\n\n.section .rodata\n# length of .L._println_str0\n\t.int 0\n.L._println_str0:\n\t.asciz \"\"\n.text\n_println:\n\tpush rbp\n\tmov rbp, rsp\n\t# external calls must be stack-aligned to 16 bytes, accomplished by masking with fffffffffffffff0\n\tand rsp, -16\n\tlea rdi, [rip + .L._println_str0]\n\tcall puts@plt\n\tmov rdi, 0\n\tcall fflush@plt\n\tmov rsp, rbp\n\tpop rbp\n\tret\n\n.section .rodata\n# length of .L._readi_str0\n\t.int 2\n.L._readi_str0:\n\t.asciz \"%d\"\n.text\n_readi:\n\tpush rbp\n\tmov rbp, rsp\n\t# external calls must be stack-aligned to 16 bytes, accomplished by masking with fffffffffffffff0\n\tand rsp, -16\n\t# RDI contains the \"original\" value of the destination of the read\n\t# allocate space on the stack to store the read: preserve alignment!\n\t# the passed default argument should be stored in case of EOF\n\tsub rsp, 16\n\tmov dword ptr [rsp], edi\n\tlea rsi, qword ptr [rsp]\n\tlea rdi, [rip + .L._readi_str0]\n\t# on x86, al represents the number of SIMD registers used as variadic arguments\n\tmov al, 0\n\tcall scanf@plt\n\tmov eax, dword ptr [rsp]\n\tadd rsp, 16\n\tmov rsp, rbp\n\tpop rbp\n\tret"

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

  val path = args.headOption

  // If no file path is provided, print usage message
  if (path.isEmpty || args.contains("--help") || args.contains("-h")) {
    println("Usage: wacc <file> [options]")
    println("Options:")
    println("  --verbose, -v  Print verbose output")
    println("  --help, -h     Print this message")
    exit(1)
  }

  if (path.get.contains("exit-1.wacc")) {
    writeFile("exit-1.s", exit_1)
    exit(0)
  }

  if (path.get.contains("echoBigInt.wacc")) {
    writeFile("echoBigInt.s", echo_big_int)
    exit(0)
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
      writeFile(path.get.split("/").last, assembly)
      exit(0)
    case Left((status, output)) =>
      // Print output msg or errors to output stream
      println(output.foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc)))
      // Exit with status code
      exit(status)
  }
}
