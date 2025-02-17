package wacc

/** Runs the backend of the compiler
 *
 * @param program Typed AST of the program
 * @param verbose Whether verbose mode is enabled
 * @return Output of the backend - ASM code as a string
 */
def runBackend(program: TypedAST.Program, verbose: Boolean): String = {
  val asmCode = Translator().translate(program)
  printVerboseInfo(verbose, "ASM IR", asmCode, Console.CYAN)
  val output = Stringifier().stringify(asmCode)
  printVerboseInfo(verbose, "Output", output, Console.GREEN)
  output
}
