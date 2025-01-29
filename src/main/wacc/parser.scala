package wacc

import parsley.{Parsley, Result}
import parsley.quick.*
import parsley.expr.{chain, precedence, Ops, InfixL, Prefix, InfixR, InfixN}

import lexer.implicits.implicitSymbol
import lexer.{int, bool, char, str, pair, ident, fully}

object parser {
  def parse(input: String): Result[String, Program] = parser.parse(input)
  private val parser = fully(prog)

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
      pair ~> pure(PairLiter),
      arrayElem,
      Ident(ident),
      "(" ~> expr <~ ")"
    )(
      // Unary operators
      Ops(Prefix)(
        Not <# "!",
        Negate <# "-",
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
    chain.postfix(baseType | pairType)(ArrayType <# "[]")
  private lazy val baseType: Parsley[BaseType] = ("int" as BaseType.Int)
    | ("bool" as BaseType.Bool)
    | ("char" as BaseType.Char)
    | ("string" as BaseType.String)
  private lazy val pairType: Parsley[PairType] =
    PairType("pair(" ~> pairElemType <~ ",", pairElemType <~ ")")
  private lazy val pairElemType: Parsley[PairElemType] =
    chain.postfix(baseType)(ArrayType <# "[]")
      | ErasedPair <# "pair"

  // Statements
  private lazy val prog: Parsley[Program] =
    Program("begin" ~> many(func), stmt <~ "end")
  private lazy val func: Parsley[Func] =
    lift3[(Type, Ident), List[(Type, Ident)], Stmt, Func](
      (a, b, c) => Func(a._1, a._2, b, c),
      atomic(typeParser <~> Ident(ident) <~ "("),
      sepBy(typeParser <~> Ident(ident), ",") <~ ")",
      "is" ~> stmt <~ "end"
    )
  private lazy val stmt: Parsley[Stmt] = chain
    .left1(
      ("skip" as Skip)
        | Decl(typeParser, Ident(ident), "=" ~> rvalue)
        | Asgn(lvalue, "=" ~> rvalue)
        | "read" ~> Read(lvalue)
        | "free" ~> Free(expr)
        | "return" ~> Return(expr)
        | "exit" ~> Exit(expr)
        | "print" ~> Print(expr)
        | "println" ~> PrintLn(expr)
        | If("if" ~> expr, "then" ~> stmt, "else" ~> stmt <~ "fi")
        | While("while" ~> expr, "do" ~> stmt <~ "done")
        | "begin" ~> stmt <~ "end"
    )(Semi <# ";")
  private lazy val lvalue: Parsley[LValue] = arrayElem
    | Ident(ident)
    | pairElem
  private lazy val rvalue: Parsley[RValue] = expr
    | arrayLiter
    | "newpair(" ~> expr <~ "," ~> expr <~ ")"
    | pairElem
    | "call" ~> Ident(ident) <~ "(" ~> sepBy(expr, ",") <~ ")"
  private lazy val pairElem: Parsley[LValue & RValue] = ("fst" ~> Fst(lvalue))
    | ("snd" ~> Snd(lvalue))
  private lazy val arrayLiter: Parsley[ArrayLiter] =
    "[" ~> ArrayLiter(sepBy(expr, ",")) <~ "]"
}
