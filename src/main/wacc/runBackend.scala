package wacc

/** Runs the backend of the compiler
 *
 * @param program Typed AST of the program
 * @param verbose Whether verbose mode is enabled
 * @return Output of the backend - ASM code as a string
 */
def runBackend(program: TypedAST.Program, verbose: Boolean): String = {
  val (strings, asmInstrs) = Translator().translate(program)
  printVerboseInfo(verbose, "ASM IR", asmInstrs.map(i => "\t" + i.toString).mkString("\n"), Console.CYAN)
  val output = x86Stringifier().stringify(strings, asmInstrs)
  printVerboseInfo(verbose, "Output", output, Console.GREEN)
  output
}
