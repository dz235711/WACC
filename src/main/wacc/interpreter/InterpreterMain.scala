package wacc

import WaccErrorBuilder.{format, setLines}

import parsley.{Result, Failure, Success}
import parsley.errors.ErrorBuilder
import scala.io.StdIn.readLine

def interpreterMain(): Unit = {
  println("Welcome to the WACC interpreter!")

  while true do
    val typedProgram = promptInputAndRunFrontend()

    Interpreter.interpret(typedProgram)
  ()
}

def promptInputAndRunFrontend(): TypedAST.Program = {
  given ErrorBuilder[WaccError] = new WaccErrorBuilder
  var errCtx: ListContext[WaccError] = new ListContext()

  // ==========================
  // RENAMING AND TYPE CHECKING
  // ==========================

  var typedProgram = TypedAST.Program(List(), TypedAST.Skip)
  var lines: List[String] = List()

  // Parse input until you get a valid typed program.
  while
    // Prompt and parse for input
    val result = promptInputAndParse()
    val parsedProgram = result._1
    lines = result._2

    // Rename and type check
    val renamedProgram = Renamer.rename(parsedProgram)(using errCtx)
    typedProgram = TypeChecker().checkProg(renamedProgram)(using errCtx)

    // If there are errors, print them and then prompt for input again.
    !errCtx.get.isEmpty
  do
    println(
      errCtx.get
        .map(e => setLines(format(e, None, ErrType.Semantic), lines))
        .foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc))
        .result()
    )
    errCtx = new ListContext()

  typedProgram
}

def promptInputAndParse()(using wErr: ErrorBuilder[WaccError]): (SyntaxAST.Program, List[String]) = {
  var parserResult: Result[WaccError, SyntaxAST.Program] = Failure(
    WaccErrorBuilder().constructSpecialised((0, 0), 0, "")
  ) // Dummy value.
  var program = SyntaxAST.Program(List(), SyntaxAST.Skip()(0, 0))(0, 0) // Dummy value.
  var input = StringBuilder()

  // We prompt for input until it is syntactically correct.
  while
    print("WACC> ")

    // We read input until all opened scopes have been closed.
    while
      val line = readLine()
      input.append(line + "\n")

      parserResult = parser.interpreterParse(input.result())

      // If not all scopes are closed, keep consuming input.
      parserResult match {
        case Failure(msg) =>
          msg.lines match {
            case WaccErrorLines.VanillaError(Some(WaccErrorItem.WaccEndOfInput), _, _, _) => true
            case _                                                                        => false
          }
        case _ => false
      }
    do print("    | ")

    // Discard input if it is erroneous and prompt again.
    parserResult match {
      case Failure(msg) =>
        println(format(msg, None, ErrType.Syntax))
        true
      case Success(p) =>
        program = p
        false
    }
  do
    // Clear input.
    input = StringBuilder()

  (program, input.result().split("\n").toList)
}
