package wacc

import parsley.errors.*

import WaccErrorLines.*
import WaccErrorItem.*

import parsley.errors.tokenextractors.TillNextWhitespace

// Constants to format builder 
val DefaultLinesBefore = 1
val DefaultLinesAfter = 1

// Constants to format indentation in stringified errors 
val LinesIndent = 2
val LinesInfoIndent = 2

// Types of errors
enum ErrType{
  case Syntax
  case Semantic
}

// Wrapper class for error information tree
case class WaccError(
    pos: (Int, Int),
    var source: Option[String],
    lines: WaccErrorLines,
    var errType: Option[ErrType],
)

case class WaccLineInfo(
    line: String,
    linesBefore: Seq[String],
    linesAfter: Seq[String],
    lineNum: Int,
    errorPointsAt: Int,
    errorWidth: Int
)

enum WaccErrorLines {
  case VanillaError(
      unexpected: Option[WaccErrorItem],
      expecteds: Set[WaccErrorItem],
      reasons: Set[String],
      lineinfo: WaccLineInfo
  )
  case SpecialisedError(msgs: Set[String], lineinfo: WaccLineInfo)
}

enum WaccErrorItem {
  case WaccRaw(item: String)
  case WaccNamed(item: String)
  case WaccEndOfInput
}

/** Converts a WaccError to a string
   *
   * @param wErr The wacc error to stringify
   * @param sBuilder The string builder to capture stringified error
   * @return Modified sBuilder
   */
def printWaccError(wErr: WaccError, sBuilder: StringBuilder): StringBuilder = {
  sBuilder.append("\n")

  // Printing error type, position and source
  if (wErr.errType.isDefined) sBuilder.append(s"${wErr.errType.get} error")
  else sBuilder.append("Error")
  if (wErr.source.isDefined) sBuilder.append(s" in ${wErr.source.get} ")
  val pos = wErr.pos
  sBuilder.append(s"at ${pos._1}:${pos._2}\n")

  // Delegating error lines to another function to recursively peel back information layers
  printLines(wErr.lines, sBuilder)
}

/** Converts a WaccErrorLines to a string
   *
   * @param lines Wacc error lines to stringify
   * @param sBuilder The string builder to capture stringified error
   * @return Modified sBuilder
   */
private def printLines(
    lines: WaccErrorLines,
    sBuilder: StringBuilder
): StringBuilder = lines match {
  // Lines can either be vanilla or specialised error, we pattern match here and send them to their functions respectively
  case vErr @ VanillaError(_, _, _, _) => printVanillaError(vErr, sBuilder)
  case sErr @ SpecialisedError(msgs, lineinfo) =>
    printSpecialisedError(sErr, sBuilder)
}

/** Converts a VanillaError to a string
   *
   * @param vErr The vanilla error to stringify
   * @param sBuilder The string builder to capture stringified error
   * @return Modified sBuilder
   */
private def printVanillaError(
    vErr: WaccErrorLines.VanillaError,
    sBuilder: StringBuilder
): StringBuilder = {
  // We stringify the unexpected and expected items delimited by , and enclosed in {}
  if (vErr.unexpected.isDefined)
    sBuilder
      .append(s"Unexpected { ${{ extractWaccErrorItem(vErr.unexpected.get) }} }".indent(LinesIndent))
  sBuilder
    .append(
      s"Expected   { ${vErr.expecteds.map(extractWaccErrorItem).mkString(", ")} }".indent(LinesIndent)
    )

  // If there are reasons given for the error, we append those too
  if (vErr.reasons.nonEmpty)
    sBuilder.append(vErr.reasons.mkString("\n").indent(LinesIndent))
  
  // Recursive delegation of work to line info printer
  printLineInfo(vErr.lineinfo, sBuilder)
}

/** Extracts the item from wErrItem enum
   *
   * @param wErrItem The enum containing different types
   * @return Item extracted from wErrItem
   */
private def extractWaccErrorItem(wErrItem: WaccErrorItem) = wErrItem match {
  case WaccRaw(item)   => item
  case WaccNamed(item) => item
  case WaccEndOfInput  => "end of file"
}


/** Converts a SpecialisedError to a string
   *
   * @param sErr The specialised error to stringify
   * @param sBuilder The string builder to capture stringified error
   * @return Modified sBuilder
   */
private def printSpecialisedError(
    sErr: WaccErrorLines.SpecialisedError,
    sBuilder: StringBuilder
): StringBuilder = {
  // We print each specialised error message on a new line
  sBuilder.append(sErr.msgs.mkString("\n").indent(LinesIndent))

  // Recursively delegate work to line info printer
  printLineInfo(sErr.lineinfo, sBuilder)
}


/** Converts a LineInfo to a string
   *
   * @param lineinfo The LineInfo to stringify
   * @param sBuilder The string builder to capture stringified error
   * @return Modified sBuilder
   */
private def printLineInfo(
    lineinfo: WaccLineInfo,
    sBuilder: StringBuilder
): StringBuilder = {
  // If there are lines before then we print all of them
  if (lineinfo.linesBefore.nonEmpty) {
    // Calculate the starting line number of each line then iteratively printing them to sBuilder
    val start = lineinfo.lineNum - lineinfo.linesBefore.length
    for (i <- 0 until lineinfo.linesBefore.length) 
      sBuilder.append(s"|${start + i}. ${lineinfo.linesBefore(i)}\n".indent(LinesInfoIndent))
  }
  
  // Printing the error line, we extract linePrefix here to allow for easier calculation of how the "^^^" error indication should be indented
  val linePrefix = f"|${lineinfo.lineNum}. "
  sBuilder.append(s"${linePrefix}${lineinfo.line}\n".indent(LinesInfoIndent))

  // Denotes the erroring token
  sBuilder
    .append(s"${" " * (lineinfo.errorPointsAt + linePrefix.length())}${"^" * lineinfo.errorWidth}\n".indent(LinesInfoIndent))

  // Prints lines after the error in the same way we printed lines before
  if (lineinfo.linesAfter.nonEmpty)
    for (i <- 0 until lineinfo.linesAfter.length) 
      sBuilder.append(s"|${lineinfo.lineNum + i + 1}. ${lineinfo.linesAfter(i)}\n".indent(LinesInfoIndent))
  sBuilder
}


// A concrete, lossless implementation of ErrorBuilder for WACC to replace the DefaultErrorBuilder
class WaccErrorBuilder extends ErrorBuilder[WaccError] {

  override def unexpectedToken(
      cs: Iterable[Char],
      amountOfInputParserWanted: Int,
      lexicalError: Boolean
  ): Token = TillNextWhitespace.unexpectedToken(cs)

  type Item = WaccErrorItem
  override def build(
      pos: Position,
      source: Source,
      lines: ErrorInfoLines
  ): WaccError = WaccError(pos, source, lines, None)

  type Position = (Int, Int)
  override def pos(line: Int, col: Int): Position = (line, col)

  type Source = Option[String]
  override def source(sourceName: Option[String]): Source = sourceName

  type ErrorInfoLines = WaccErrorLines
  override def vanillaError(
      unexpected: UnexpectedLine,
      expected: ExpectedLine,
      reasons: Messages,
      line: LineInfo
  ): ErrorInfoLines = {
    VanillaError(unexpected, expected, reasons, line)
  }

  override def specializedError(
      msgs: Messages,
      line: LineInfo
  ): ErrorInfoLines = {
    SpecialisedError(msgs, line)
  }

  type ExpectedItems = Set[Item]
  override def combineExpectedItems(alts: Set[Item]): ExpectedItems = alts

  type Messages = Set[Message]
  override def combineMessages(alts: Seq[Message]): Messages = alts.toSet

  type UnexpectedLine = Option[Item]
  override def unexpected(item: Option[Item]): UnexpectedLine = item

  type ExpectedLine = ExpectedItems
  override def expected(alts: ExpectedItems): ExpectedLine = alts

  type Message = String
  override def reason(reason: String): Message = reason

  override def message(msg: String): Message = msg

  type LineInfo = WaccLineInfo
  override def lineInfo(
      line: String,
      linesBefore: Seq[String],
      linesAfter: Seq[String],
      lineNum: Int,
      errorPointsAt: Int,
      errorWidth: Int
  ): LineInfo =
    WaccLineInfo(
      line,
      linesBefore,
      linesAfter,
      lineNum,
      errorPointsAt,
      errorWidth
    )

  override val numLinesBefore: Int = 1

  override val numLinesAfter: Int = 1

  type Raw = WaccRaw
  override def raw(item: String): Raw = WaccRaw(item)

  type Named = WaccNamed
  override def named(item: String): Named = WaccNamed(item)

  type EndOfInput = WaccEndOfInput.type
  override val endOfInput: EndOfInput = WaccEndOfInput
}


// An object to allow for easy formatting of WaccErrors with source and error type
object WaccErrorBuilder extends WaccErrorBuilder {

    
  /** Adds source and error type to a WACC error
   *
   * @param wErr The WACC error to format
   * @param source The source file
   * @param errType Whether the error is syntax or semantic
   * @return Modified wErr
   */
    def format(wErr: WaccError, source: Source, errType: ErrType): WaccError = {
    wErr.source = source
    wErr.errType = Some(errType)
    wErr
  }
}
