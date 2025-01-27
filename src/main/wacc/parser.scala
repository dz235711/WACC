package wacc

import parsley.{Parsley, Result}
import parsley.quick.*
import parsley.expr.{chain, precedence, Ops, InfixL, Prefix, InfixR, InfixN}

import lexer.implicits.implicitSymbol
import lexer.{int, bool, char, str, pair, ident, fully}

object parser {
  def parse(input: String): Result[String, Program] = parser.parse(input)
  private val parser = fully(prog)

  private lazy val expr: Parsley[Expr] =
    precedence(
      IntLiter(int),
      BoolLiter(bool),
      CharLiter(char),
      StringLiter(str),
      pair ~> pure(PairLiter),
      Ident(ident),
      "(" ~> expr <~ ")"
    )(
      Ops(Prefix)(
        Not <# "!",
        Negate <# "-",
        Len <# "len",
        Ord <# "ord",
        Chr <# "chr"
      ),
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
  private lazy val arrayElem: Parsley[ArrayElem] = ArrayElem(Ident(ident), some("[" ~> expr <~ "]"))
  private lazy val _type: Parsley[Type] = baseType
    | arrayType
    | pairType
  private lazy val baseType: Parsley[BaseType] = ("int" as BaseType.Int)
    | ("bool" as BaseType.Bool)
    | ("char" as BaseType.Char)
    | "string" as BaseType.String
  private lazy val arrayType: Parsley[ArrayType] = ArrayType(_type) <~ "[]"
  private lazy val pairType: Parsley[PairType] =
    PairType("pair(" ~> pairElemType <~ ",", pairElemType <~ ")")
  private lazy val pairElemType: Parsley[PairElemType] = baseType
    | arrayType
    | "pair" as ErasedPair

  private lazy val prog: Parsley[Program] =
    Program("begin" ~> many(func), stmt <~ "end")
  private lazy val func: Parsley[Func] = Func(
    _type,
    Ident(ident),
    "(" ~> paramList <~ ")",
    "is" ~> stmt <~ "end"
  )
  private lazy val paramList: Parsley[List[(Type, Ident)]] =
    (param <::> many("," ~> param))
  private lazy val param: Parsley[(Type, Ident)] =
    _type <~> Ident(ident)
  private lazy val stmt: Parsley[Stmt] = chain.left1(
    ("skip" as Skip)
      | Decl(_type, Ident(ident), "=" ~> rvalue)
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
  )(";" as Semi.apply)
  private lazy val lvalue: Parsley[LValue] = Ident(ident)
    | arrayElem
    | pairElem
  private lazy val rvalue: Parsley[RValue] = expr
    | arrayLiter
    | "newpair(" ~> expr <~ "," ~> expr <~ ")"
    | "call" ~> Ident(ident) <~ "(" ~> argList <~ ")"
  private lazy val argList: Parsley[List[Expr]] = (expr <::> many("," ~> expr))
  private lazy val pairElem: Parsley[LValue] = ("fst" ~> Fst(lvalue))
    | ("snd" ~> Snd(lvalue))
  private lazy val arrayLiter: Parsley[ArrayLiter] =
    "[" ~> (ArrayLiter(expr <::> many("," ~> expr))
      | ("" as ArrayLiter(List()))) <~ "]"
}
