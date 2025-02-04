package wacc

import parsley.expr.*
import parsley.quick.*
import parsley.{Parsley, Result}
import parsley.errors.combinator.*
import wacc.lexer.implicits.implicitSymbol
import wacc.lexer.{char, *}
import parsley.errors.ErrorBuilder

object parser {
  def parse[Err: ErrorBuilder](input: String): Result[Err, Program] = parser.parse(input)
  private val parser = fully(prog)

  // Helpers
  private def endsInReturn(s: Stmt): Boolean = s match {
    case Exit(_) => true
    case Return(_) => true
    case Semi(_, b) => endsInReturn(b)
    case Begin(b) => endsInReturn(b)
    case If(_, t, e) => endsInReturn(t) && endsInReturn(e)
    case _ => false
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
    )

  // Types
  private lazy val typeParser: Parsley[Type] =
    chain.postfix(
      atomic(baseType | pairType)
    )(ArrayType <# ("[" <~> "]"))
  private lazy val baseType: Parsley[BaseType] = choice(
    string("int") as BaseType.Int,
    string("bool") as BaseType.Bool,
    string("char") as BaseType.Char,
    string("string") as BaseType.String
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
    Program("begin".explain("Programs must be nested start with begin") ~> many(func).label("functions"), stmt.label("statement").explain("Programs can, but doesn't have to, lead with function declarations however they must have atleast one statement as its body") <~ "end".explain("Programs must end with end"))
  private lazy val func: Parsley[Func] =
    lift3(
      (a: (Type, Ident), b: List[(Type, Ident)], c: Stmt) =>
        Func(a._1, a._2, b, c),
      atomic(typeParser <~> Ident(ident) <~ "("),
      sepBy(typeParser <~> Ident(ident), ",") <~ ")",
      "is" ~> stmt <~ "end"
    ).guardAgainst{ case Func(_, _, _, b) if !endsInReturn(b) => Seq("Functions must either end directly with return or with a returning block") }
  private lazy val stmt: Parsley[Stmt] = chain
    .left1(
      choice(
        "skip" as Skip,
        Decl(typeParser, Ident(ident), "=" ~> rvalue),
        Asgn(lvalue, "=" ~> rvalue),
        "read" ~> Read(lvalue),
        "free" ~> Free(expr),
        "return" ~> Return(expr),
        "exit" ~> Exit(expr),
        "print" ~> Print(expr),
        "println" ~> PrintLn(expr),
        If("if" ~> expr, "then" ~> stmt, "else" ~> stmt <~ "fi"),
        While("while" ~> expr, "do" ~> stmt <~ "done"),
        "begin" ~> Begin(stmt) <~ "end"
      )
    )(Semi <# ";")
  private lazy val lvalue: Parsley[LValue] = choice(
    arrayElem,
    Ident(ident),
    pairElem
  )
  private lazy val rvalue: Parsley[RValue] = choice(
    expr,
    arrayLiter,
    "newpair" ~> "(" ~> NewPair(expr <~ ",", expr) <~ ")",
    pairElem,
    "call" ~> Call(Ident(ident) <~ "(", sepBy(expr, ",")) <~ ")"
  )
  private lazy val pairElem: Parsley[LValue & RValue] = choice(
    "fst" ~> Fst(lvalue),
    "snd" ~> Snd(lvalue)
  )
  private lazy val arrayLiter: Parsley[ArrayLiter] =
    "[" ~> ArrayLiter(sepBy(expr, ",")) <~ "]"
}
