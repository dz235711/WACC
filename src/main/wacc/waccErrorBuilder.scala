package wacc

import parsley.errors.*

import WaccErrorLines.*
import WaccErrorItem.*


case class WaccError(pos: (Int, Int), lines: WaccErrorLines)

enum WaccErrorLines {
    case VanillaError(unexpected: Option[WaccErrorItem], expecteds: Set[WaccErrorItem], reasons: Set[String], width: Int)
    case SpecialisedError(msgs: Set[String], width: Int)
}

enum WaccErrorItem {
    case WaccRaw(item: String)
    case WaccNamed(item: String)
    case WaccEndOfInput
}


abstract class WaccErrorBuilder extends ErrorBuilder[WaccError] {
    override def build(pos: Position, source: Source, lines: ErrorInfoLines): WaccError = WaccError(pos, lines)

    type Position = (Int, Int)
    override def pos(line: Int, col: Int): Position = (line, col)

    type Source = Unit
    override def source(sourceName: Option[String]): Source = ()

    type ErrorInfoLines = WaccErrorLines
    override def vanillaError(unexpected: UnexpectedLine, expected: ExpectedLine, reasons: Messages, line: LineInfo): ErrorInfoLines = {
        VanillaError(unexpected, expected, reasons, line)
    }
    override def specializedError(msgs: Messages, line: LineInfo): ErrorInfoLines = SpecialisedError(msgs, line)

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

    type LineInfo = Int
    override def lineInfo(line: String, linesBefore: Seq[String], linesAfter: Seq[String], lineNum: Int, errorPointsAt: Int, errorWidth: Int): Int = errorWidth

    override val numLinesBefore: Int = 0
    override val numLinesAfter: Int = 0

    type Item = WaccErrorItem
    type Raw = WaccRaw
    type Named = WaccNamed
    type EndOfInput = WaccEndOfInput.type
    override def raw(item: String): Raw = WaccRaw(item)
    override def named(item: String): Named = WaccNamed(item)
    override val endOfInput: EndOfInput = WaccEndOfInput
}