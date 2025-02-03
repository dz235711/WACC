package wacc

import parsley.errors.*

import WaccErrorLines.*
import WaccErrorItem.*

import parsley.errors.tokenextractors.SingleChar

case class WaccError(pos: (Int, Int), source: Option[String], lines: WaccErrorLines)

case class WaccLineInfo(line: String, linesBefore: Seq[String], linesAfter: Seq[String], lineNum: Int, errorPointsAt: Int, errorWidth: Int)

case class WaccToken(cs: Iterable[Char], amountOfInputParserWanted: Int, lexicalError: Boolean)

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

def printWaccError(wErr: WaccError): String = {
  return "a"
}


class WaccErrorBuilder[Error] extends ErrorBuilder[WaccError] {

  override def unexpectedToken(cs: Iterable[Char], amountOfInputParserWanted: Int, lexicalError: Boolean): Token = SingleChar.unexpectedToken(cs)

  type Item = WaccErrorItem
  override def build(pos: Position, source: Source, lines: ErrorInfoLines): WaccError = WaccError(pos, source, lines)

  type Position = (Int, Int)
  override def pos(line: Int, col: Int): Position = (line, col)

  type Source = Option[String]
  override def source(sourceName: Option[String]): Source = sourceName

  type ErrorInfoLines = WaccErrorLines
  override def vanillaError(unexpected: UnexpectedLine, expected: ExpectedLine, reasons: Messages, line: LineInfo): ErrorInfoLines = {
    VanillaError(unexpected, expected, reasons, line)
  }

  override def specializedError(msgs: Messages, line: LineInfo): ErrorInfoLines = {
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
  override def lineInfo(line: String, linesBefore: Seq[String], linesAfter: Seq[String], lineNum: Int, errorPointsAt: Int, errorWidth: Int): LineInfo = 
    WaccLineInfo(line, linesBefore, linesAfter, lineNum, errorPointsAt, errorWidth)

  override val numLinesBefore: Int = 0

  override val numLinesAfter: Int = 0

  type Raw = WaccRaw
  override def raw(item: String): Raw = WaccRaw(item)

  type Named = WaccNamed
  override def named(item: String): Named = WaccNamed(item)

  type EndOfInput = WaccEndOfInput.type
  override val endOfInput: EndOfInput = WaccEndOfInput
}