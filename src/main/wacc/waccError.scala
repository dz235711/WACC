package wacc

import parsley.errors.*

import WaccErrorLines.*
import WaccErrorItem.*

import parsley.errors.tokenextractors.TillNextWhitespace

private object Indents {
  val Lines = "\t"
  val LinesInfo = "\t"
}

case class WaccError(
    pos: (Int, Int),
    source: Option[String],
    lines: WaccErrorLines
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

def printWaccError(wErr: WaccError, sBuilder: StringBuilder): StringBuilder = {
  val pos = wErr.pos
  sBuilder.append("Error")
  if (wErr.source.isDefined) sBuilder.append(s" in ${wErr.source.get} ")
  sBuilder.append(s"at ${pos._1}:${pos._2}\n")
  printLines(wErr.lines, sBuilder)
}

private def printLines(
    lines: WaccErrorLines,
    sBuilder: StringBuilder
): StringBuilder = lines match {
  case vErr @ VanillaError(_, _, _, _) => printVanillaError(vErr, sBuilder)
  case sErr @ SpecialisedError(msgs, lineinfo) =>
    printSpecialisedError(sErr, sBuilder)
}

private def printVanillaError(
    vErr: WaccErrorLines.VanillaError,
    sBuilder: StringBuilder
): StringBuilder = {
  if (vErr.unexpected.isDefined)
    sBuilder
      .append(Indents.Lines)
      .append(s"Unexpected {${{ extractWaccErrorItem(vErr.unexpected.get) }}}")
      .append("\n")
  sBuilder
    .append(Indents.Lines)
    .append(
      s"Expected   {${vErr.expecteds.map(extractWaccErrorItem).mkString(", ")}}"
    )
    .append("\n")
  if (vErr.reasons.nonEmpty)
    sBuilder.append(Indents.Lines).append(vErr.reasons.mkString("\n")).append("\n")
  printLineInfo(vErr.lineinfo, sBuilder)
}

private def extractWaccErrorItem(wErrItem: WaccErrorItem) = wErrItem match {
  case WaccRaw(item)   => item
  case WaccNamed(item) => item
  case WaccEndOfInput  => "end of file"
}

private def printSpecialisedError(
    sErr: WaccErrorLines.SpecialisedError,
    sBuilder: StringBuilder
): StringBuilder = {
  sBuilder.append(Indents.Lines).append(sErr.msgs.mkString("\n"))
  printLineInfo(sErr.lineinfo, sBuilder)
}

private def printLineInfo(
    lineinfo: WaccLineInfo,
    sBuilder: StringBuilder
): StringBuilder = {
  if (lineinfo.linesBefore.nonEmpty) {
    val start = lineinfo.lineNum - lineinfo.linesBefore.length - 1
    for (i <- Seq(1, 2, 3)) 
      sBuilder.append(Indents.LinesInfo).append(s"|${start + i}. ${lineinfo.linesBefore(i)}\n")
  }
  val linePrefix = f"|${lineinfo.lineNum}. "
  sBuilder.append(Indents.LinesInfo).append(linePrefix).append(s"${lineinfo.line}\n")
  sBuilder
    .append(Indents.LinesInfo)
    .append(s"${" " * (lineinfo.errorPointsAt + linePrefix.length())}${"^" * lineinfo.errorWidth}\n")
  if (lineinfo.linesAfter.nonEmpty)
    for (i <- Seq(1, 2, 3)) 
      sBuilder.append(Indents.LinesInfo).append(s"|${lineinfo.lineNum + i}. ${lineinfo.linesBefore(i)}\n")
  sBuilder
}

class WaccErrorBuilder[Error](source: String) extends ErrorBuilder[WaccError] {

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
  ): WaccError = WaccError(pos, source, lines)

  type Position = (Int, Int)
  override def pos(line: Int, col: Int): Position = (line, col)

  type Source = Option[String]
  override def source(sourceName: Option[String]): Source = Some(source)

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

  override val numLinesBefore: Int = 0

  override val numLinesAfter: Int = 0

  type Raw = WaccRaw
  override def raw(item: String): Raw = WaccRaw(item)

  type Named = WaccNamed
  override def named(item: String): Named = WaccNamed(item)

  type EndOfInput = WaccEndOfInput.type
  override val endOfInput: EndOfInput = WaccEndOfInput
}
