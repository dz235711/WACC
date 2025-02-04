package wacc

import parsley.expr.*
import parsley.quick.*
import parsley.{Parsley, Result}
import parsley.errors.combinator.*
import wacc.lexer.implicits.implicitSymbol
import wacc.lexer.{char, *}
import parsley.errors.ErrorBuilder

object parser {
  def parse[Err: ErrorBuilder](input: String): Result[Err, Program] =
    parser.parse(input)
  private val parser = fully(prog)

  // Helpers
  private def endsInReturn(s: Stmt): Boolean = s match {
    case Exit(_)     => true
    case Return(_)   => true
    case Semi(_, b)  => endsInReturn(b)
    case Begin(b)    => endsInReturn(b)
    case If(_, t, e) => endsInReturn(t) && endsInReturn(e)
    case _           => false
  }

  private lazy val arrayElem: Parsley[ArrayElem] =
    atomic(ArrayElem(Ident(ident), some("[" ~> expr <~ "]")))

  // Expressions
  private lazy val expr: Parsley[Expr] =
    precedence(
      // Atoms
      IntLiter(int),
      BoolLiter(bool),
      CharLiter(char),
      StringLiter(str),
      PairLiter <# pair,
      arrayElem,
      chain.prefix(Ident(ident))(Negate <# "-"),
      "(" ~> expr <~ ")"
    )(
      // Unary operators
      Ops(Prefix)(
        Not <# "!",
        Len <# "len",
        Ord <# "ord",
        Chr <# "chr"
      ),
      // Binary operators
      Ops(InfixL)(Mult <# "*", Mod <# "%", Div <# "/"),
      Ops(InfixL)(Add <# "+", Sub <# "-"),
      Ops(InfixN)(
        Greater <# ">",
        GreaterEq <# ">=",
        Smaller <# "<",
        SmallerEq <# "<="
      ),
      Ops(InfixN)(Equals <# "==", NotEquals <# "!="),
      Ops(InfixR)(And <# "&&"),
      Ops(InfixR)(Or <# "||")
    ).label("expression").explain("Expressions are phrases like \"1 + 2 * 3\" that evaluate to a final value")

  // Types
  private lazy val typeParser: Parsley[Type] =
    chain.postfix(
      atomic(baseType | pairType)
    )(ArrayType <# ("[".label("array type") <~> "]"))
  private lazy val baseType: Parsley[BaseType] = choice(
    IntType <# string("int"),
    BoolType <# string("bool"),
    CharType <# string("char"),
    StringType <# string("string")
  ) <~ notFollowedBy(ident) <~ many(whitespace)
  private lazy val pairType: Parsley[PairType] =
    PairType("pair" ~> "(" ~> pairElemType <~ ",", pairElemType <~ ")")
  private lazy val pairElemType: Parsley[PairElemType] = choice(
    atomic(chain.postfix1(baseType | pairType)(ArrayType <# ("[" <~> "]"))),
    baseType,
    ErasedPair <# "pair"
  )

  // Statements
  private lazy val prog: Parsley[Program] =
    Program(
      "begin".explain("Programs must start with begin") ~> many(func).label(
        "function"
      ),
      stmt
        .label("statement")
        .explain(
          "Programs can lead with function declarations however they must have atleast one statement as its body"
        ) <~ "end".explain("Programs must end with end")
    )
  private lazy val func: Parsley[Func] = {
    Func(
      atomic(typeParser <~> Ident(ident) <~ "("),
      sepBy(typeParser.label("parameters") <~> Ident(ident), ",") <~ ")",
      "is" ~> stmt <~ "end"
    )
      .guardAgainst {
        case Func(_, _, b) if !endsInReturn(b) =>
          Seq(
            "Functions must either end directly with return or with a returning block"
          )
      }
  }
  private lazy val stmt: Parsley[Stmt] = chain
    .left1(
      choice(
        Skip <# "skip",
        Decl(typeParser, Ident(ident), "=" ~> rvalue),
        Asgn(lvalue, "=" ~> rvalue),
        Read("read" ~> lvalue),
        Free("free" ~> expr),
        Return("return" ~> expr),
        Exit("exit" ~> expr),
        Print("print" ~> expr),
        PrintLn("println" ~> expr),
        If("if" ~> expr, "then" ~> stmt, "else" ~> stmt <~ "fi"),
        While("while" ~> expr, "do" ~> stmt <~ "done"),
        Begin("begin" ~> stmt <~ "end")
      ).label("statement").explain("Unnecessary semicolon at the end of the block")
    )(Semi <# ";")
  private lazy val lvalue: Parsley[LValue] = choice(
    arrayElem,
    Ident(ident),
    pairElem
  )
  private lazy val rvalue: Parsley[RValue] = choice(
    expr,
    arrayLiter,
    NewPair("newpair" ~> "(" ~> expr <~ ",", expr <~ ")").hide,
    pairElem,
    Call("call" ~> Ident(ident) <~ "(", sepBy(expr, ",") <~ ")").label("function call")
  )
  private lazy val pairElem: Parsley[LValue & RValue] = choice(
    Fst("fst" ~> lvalue),
    Snd("snd" ~> lvalue)
  ).label("pair element").explain("Pair elements can be extracted by calling fst or snd on a pair corresponding to the first or second element respectively")
  private lazy val arrayLiter: Parsley[ArrayLiter] =
    ArrayLiter("[".label("array literal") ~> sepBy(expr, ",") <~ "]")
}
