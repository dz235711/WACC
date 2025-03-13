package wacc

import WaccErrorBuilder.{format, setLines}

import parsley.{Result, Failure, Success}
import parsley.errors.ErrorBuilder
import os.SubProcess.OutputStream
import os.SubProcess.InputStream

type RenamerUID = Int
// To distinguish between renamer and interpreter scopes.
type RenamerScope = Scope
type RenamerFunctionScope = Map[String, (RenamedAST.QualifiedName, Int)]
type TypeCheckerFunctionScope = Map[RenamerUID, List[RenamedAST.Ident]]
type InterpreterVariableScope = VariableScope
type InterpreterFunctionScope = FunctionScope

def interpreterMain()(using
    interpreterIn: InputStream = InputStream(System.out),
    interpreterOut: OutputStream = OutputStream(System.in)
): (InterpreterVariableScope, InterpreterFunctionScope, Option[Int]) = {
  interpreterIn.writeLine("Welcome to the WACC interpreter!")

  var renamerScope: Option[RenamerScope] = None
  var renamerFunctionScope: Option[RenamerFunctionScope] = None
  var renamerUid: Option[RenamerUID] = None

  var typeCheckerFunctionTable: Option[TypeCheckerFunctionScope] = None

  var interpreterScope: InterpreterVariableScope = MapContext()
  var interpreterFunctionScope: InterpreterFunctionScope = MapContext()

  var exitValue: Option[Int] = None

  while exitValue.isEmpty do
    val frontendResult =
      promptInputAndRunFrontend(renamerScope, renamerFunctionScope, renamerUid, typeCheckerFunctionTable)
    val typedProgram = frontendResult._1
    renamerScope = Some(frontendResult._2)
    renamerFunctionScope = Some(frontendResult._3)
    renamerUid = Some(frontendResult._4)
    typeCheckerFunctionTable = Some(frontendResult._5)

    val interpreterResult = Interpreter.interpret(typedProgram, interpreterScope, interpreterFunctionScope)
    interpreterScope = interpreterResult._1
    interpreterFunctionScope = interpreterResult._2
    exitValue = interpreterResult._3

  (interpreterScope, interpreterFunctionScope, exitValue)
}

def promptInputAndRunFrontend(
    inheritedRenamerScope: Option[Scope],
    inheritedRenamerFunctionScope: Option[RenamerFunctionScope],
    inheritedRenamerUid: Option[RenamerUID],
    inheritedTypeCheckerFuncTable: Option[TypeCheckerFunctionScope]
)(using
    interpreterIn: InputStream,
    interpreterOut: OutputStream
): (TypedAST.Program, RenamerScope, RenamerFunctionScope, RenamerUID, TypeCheckerFunctionScope) = {
  given ErrorBuilder[WaccError] = new WaccErrorBuilder
  var errCtx: ListContext[WaccError] = new ListContext()

  // ==========================
  // RENAMING AND TYPE CHECKING
  // ==========================

  var lines: List[String] = List()
  var newRenamedScope: RenamerScope = Map()
  var newRenamedFunctionScope: RenamerFunctionScope = Map()
  var newUid = 0
  var typedProgram = TypedAST.Program(List(), TypedAST.Skip)
  var newTypedFuncTable: TypeCheckerFunctionScope = Map()

  // Parse input until you get a valid typed program.
  while
    // Prompt and parse for input
    val result = promptInputAndParse()
    val parsedProgram = result._1
    lines = result._2

    // Rename and type check
    val renamerResult =
      Renamer.rename(parsedProgram, inheritedRenamerScope, inheritedRenamerFunctionScope, inheritedRenamerUid, Nil)(
        using errCtx
      )
    val renamedProgram = renamerResult._1
    newRenamedScope = renamerResult._2
    newRenamedFunctionScope = renamerResult._3
    newUid = renamerResult._4

    val typeCheckerResult = TypeChecker(inheritedTypeCheckerFuncTable).checkProg(renamedProgram)(using errCtx)
    typedProgram = typeCheckerResult._1
    newTypedFuncTable = typeCheckerResult._2

    // If there are errors, print them and then prompt for input again.
    errCtx.get.nonEmpty
  do
    interpreterIn.write(
      errCtx.get
        .map(e => setLines(format(e, None, ErrType.Semantic), lines))
        .foldRight(new StringBuilder)((e, acc) => printWaccError(e, acc))
        .result()
    )
    errCtx = ListContext()

  (typedProgram, newRenamedScope, newRenamedFunctionScope, newUid, newTypedFuncTable)
}

def promptInputAndParse()(using
    wErr: ErrorBuilder[WaccError],
    interpreterIn: InputStream,
    interpreterOut: OutputStream
): (SyntaxAST.Program, List[String]) = {
  var parserResult: Result[WaccError, SyntaxAST.Program] = Failure(
    WaccErrorBuilder().constructSpecialised((0, 0), 0, "")
  ) // Dummy value.
  var program = SyntaxAST.Program(Nil, List(), SyntaxAST.Skip()(0, 0))(0, 0) // Dummy value.
  var input = StringBuilder()

  // We prompt for input until it is syntactically correct.
  while
    interpreterIn.write("WACC> ")

    // We read input until all opened scopes have been closed.
    while
      interpreterIn.flush()
      val line = interpreterOut.readLine()
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
    do interpreterIn.write("    | ")

    // Print the errors, and prompt for new input if there are any errors, otherwise exit the loop.
    parserResult match {
      case Failure(msg) =>
        interpreterIn.writeLine(printWaccError(format(msg, None, ErrType.Syntax), StringBuilder()).result())
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
