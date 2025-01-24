package wacc

import parsley.Parsley
import parsley.quick.*
import parsley.token.{Lexer, Basic}
import parsley.token.descriptions.*

object lexer {
  private val desc = LexicalDesc.plain.copy(
    nameDesc = NameDesc.plain.copy(
      identifierStart = Basic(c => c.isLetter || c == '_'),
      identifierLetter = Basic(c => c.isLetterOrDigit || c == '_')
    ),
    spaceDesc = SpaceDesc.plain.copy(
      lineCommentStart = "#"
    ),
    symbolDesc = SymbolDesc.plain.copy(
      hardKeywords = Set("begin", "end", "is", "skip", "read", "free", "return", "exit", "print", "println", "if", "then", "else", "fi", "while", "do", "done", "newpair", "call", "fst", "snd"),
      hardOperators = Set("!", "-", "len", "ord", "chr", "*", "/", "%", "+", "-", ">", ">=", "<", "<=", "==", "!=", "&&", "||"),
    ),
    textDesc = TextDesc.plain.copy(
      graphicCharacter = Basic(c => 31 < c.toInt && c.toInt < 127 && !Set('\\', '\'', '"').contains(c)),
      escapeSequences = EscapeDesc.plain.copy(
        literals = Set('0', 'b', 't', 'n', 'f', 'r', '"', '\'', '\\'),
      ),
    ),
    numericDesc = NumericDesc.plain.copy(
      integerNumbersCanBeHexadecimal = false,
      integerNumbersCanBeOctal = false,
    ),
  )
  private val lexer = Lexer(desc)

  val int = lexer.lexeme.integer.decimal32
  val bool = atomic(lexer.lexeme.symbol("true") as true) | atomic(lexer.lexeme.symbol("false") as false)
  val char = lexer.lexeme.character.ascii
  val str = lexer.lexeme.string.ascii
  val pair = lexer.lexeme.symbol("null")
  val ident = lexer.lexeme.names.identifier
  val implicits = lexer.lexeme.symbol.implicits
  def fully[A](p: Parsley[A]): Parsley[A] = lexer.fully(p)
}
