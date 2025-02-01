package wacc

import parsley.Parsley
import parsley.quick.*
import parsley.token.descriptions.*
import parsley.token.symbol.ImplicitSymbol
import parsley.token.{Basic, Lexer}
import parsley.token.errors.*

val MIN_GRAPHIC_CHAR = 32
val MAX_GRAPHIC_CHAR = 126

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
      hardKeywords = Set(
        "begin",
        "end",
        "is",
        "skip",
        "read",
        "free",
        "return",
        "exit",
        "print",
        "println",
        "if",
        "then",
        "else",
        "fi",
        "while",
        "do",
        "done",
        "newpair",
        "call",
        "fst",
        "snd",
        "null"
      ),
      hardOperators = Set(
        "!",
        "-",
        "len",
        "ord",
        "chr",
        "*",
        "/",
        "%",
        "+",
        ">",
        ">=",
        "<",
        "<=",
        "==",
        "!=",
        "&&",
        "||"
      )
    ),
    textDesc = TextDesc.plain.copy(
      graphicCharacter = Basic(c =>
        MIN_GRAPHIC_CHAR <= c.toInt && c.toInt <= MAX_GRAPHIC_CHAR && !Set(
          '\\',
          '\'',
          '"'
        ).contains(c)
      ),
      escapeSequences = EscapeDesc.plain.copy(
        literals = Set('0', 'b', 't', 'n', 'f', 'r', '"', '\'', '\\')
      )
    ),
    numericDesc = NumericDesc.plain.copy(
      integerNumbersCanBeHexadecimal = false,
      integerNumbersCanBeOctal = false
    )
  )

  private val errConfig = new ErrorConfig {
    override def labelSymbol = Map(
      "+"   -> Label("binary operator"),
      "*"   -> Label("binary operator"),
      "/"   -> Label("binary operator"),
      "%"   -> Label("binary operator"),
      "-"   -> Label("binary operator"),
      ">"   -> Label("binary operator"),
      ">="  -> Label("binary operator"),
      "<"   -> Label("binary operator"),
      "<="  -> Label("binary operator"),
      "=="  -> Label("binary operator"),
      "!="  -> Label("binary operator"),
      "&&"  -> Label("binary operator"),
      "||"  -> Label("binary operator"),
      "chr" -> Label("unary operator"),
      "!"   -> Label("unary operator"),
      "len" -> Label("unary operator"),
      "ord" -> Label("unary operator"),
    )
    override def labelIntegerSignedNumber = Label("number")
  }
  private val lexer = Lexer(desc, errConfig)

  val int: Parsley[Int] = lexer.lexeme.integer.decimal32
  val bool: Parsley[Boolean] = choice(
    lexer.lexeme.symbol("true") as true,
    lexer.lexeme.symbol("false") as false
  )
  val char: Parsley[Char] = lexer.lexeme.character.ascii
  val str: Parsley[String] = lexer.lexeme.string.ascii
  val pair: Parsley[Unit] = lexer.lexeme.symbol("null")
  val ident: Parsley[String] = lexer.lexeme.names.identifier
  val implicits: ImplicitSymbol = lexer.lexeme.symbol.implicits
  def fully[A](p: Parsley[A]): Parsley[A] = lexer.fully(p)
}
