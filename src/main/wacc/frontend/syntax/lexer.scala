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
        "null",
        "int",
        "char",
        "bool",
        "string",
        "pair",
        "true",
        "false",
        "len",
        "ord",
        "chr",
        "import",
        // Exception keywords
        "throw",
        "try",
        "catch",
        "finally",
        "yrt"
      ),
      hardOperators = Set(
        "!",
        "-",
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
        ).contains(c),
      ),
      escapeSequences = EscapeDesc.plain.copy(
        mapping = Map("0" -> 0x00, "b" -> 0x08, "t" -> 0x09, "n" -> 0x0a, "f" -> 0x0c, "r" -> 0x0d),
        literals = Set('\"', '\'', '\\')
      )
    ),
    numericDesc = NumericDesc.plain.copy(
      integerNumbersCanBeHexadecimal = false,
      integerNumbersCanBeOctal = false
    )
  )

  private val errConfig: ErrorConfig = new ErrorConfig {
    override def labelSymbol: Map[String, LabelConfig] = Map(
      "+" -> Label("binary operator"),
      "*" -> Label("binary operator"),
      "/" -> Label("binary operator"),
      "%" -> Label("binary operator"),
      "-" -> Label("binary operator"),
      ">" -> Label("binary operator"),
      ">=" -> Label("binary operator"),
      "<" -> Label("binary operator"),
      "<=" -> Label("binary operator"),
      "==" -> Label("binary operator"),
      "!=" -> Label("binary operator"),
      "&&" -> Label("binary operator"),
      "||" -> Label("binary operator"),
      "chr" -> Label("unary operator"),
      "!" -> Label("unary operator"),
      "len" -> Label("unary operator"),
      "ord" -> Label("unary operator"),
      "true" -> Label("boolean"),
      "false" -> Label("boolean")
    )
    override def labelIntegerSignedNumber: LabelConfig = Label("number")
    override def labelCharAscii: LabelConfig = Label("character")
    override def labelStringAscii(multi: Boolean, raw: Boolean): LabelConfig = Label("string")
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
