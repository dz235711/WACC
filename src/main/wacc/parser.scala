package wacc

import parsley.{Parsley, Result}
import parsley.quick.*
import parsley.expr.chain
import parsley.syntax.zipped.*

import lexer.implicits.implicitSymbol
import lexer.{int, bool, char, str, pair, ident, fully}

object parser {
  def parse(input: String): Result[String, Program] = parser.parse(input)
  private val parser = fully(prog)

  private lazy val expr: Parsley[Expr] = ???
  private lazy val atom: Parsley[Expr] = int.map(IntLiter.apply) 
                                         | bool.map(BoolLiter.apply) 
                                         | char.map(CharLiter.apply) 
                                         | str.map(StringLiter.apply) 
                                         | (pair as PairLiter)
                                         | ident.map(Ident.apply) 
                                         | arrayElem 
                                         | "(" ~> expr <~ ")"
  private lazy val arrayElem: Parsley[ArrayElem] = (ident.map(Ident.apply), some(expr)).zipped(ArrayElem(_, _))
  private lazy val unaryOper = ???
  private lazy val binaryOper = ???
  
  private lazy val _type: Parsley[Type] = baseType 
                                          | arrayType 
                                          | pairType
  private lazy val baseType: Parsley[BaseType] = ("int" as BaseType.Int) 
                                                 | ("bool" as BaseType.Bool) 
                                                 | ("char" as BaseType.Char) 
                                                 | "string" as BaseType.String
  private lazy val arrayType: Parsley[ArrayType] = ???
  private lazy val pairType: Parsley[PairType] = ("pair(" ~> pairElemType <~ ",", pairElemType <~ ")").zipped(PairType(_, _))
  private lazy val pairElemType: Parsley[PairElemType] = baseType 
                                                         | arrayType 
                                                         | "pair" as ErasedPair

  private lazy val prog: Parsley[Program] = ("begin" ~> many(func), stmt <~ "end").zipped(Program(_, _))
  private lazy val func: Parsley[Func] = (_type, ident.map(Ident.apply), "(" ~> paramList <~ ")", "is" ~> stmt <~ "end").zipped(Func(_, _, _, _))
  private lazy val paramList: Parsley[List[(Type, Ident)]] = (param <::> many("," ~> param))
  private lazy val param: Parsley[(Type, Ident)] = _type <~> ident.map(Ident.apply)
  // private def stmt(body: String): Result[String, Stmt] = ???
  private lazy val stmt: Parsley[Stmt] = ("skip" as Skip)
                                         | (_type, ident.map(Ident.apply), "=" ~> rvalue).zipped(Decl(_, _, _))
                                         | (lvalue, "=" ~> rvalue).zipped(Asgn(_, _))
                                         | "read" ~> lvalue.map(Read.apply)
                                         | "free" ~> expr.map(Free.apply)
                                         | "return" ~> expr.map(Return.apply)
                                         | "exit" ~> expr.map(Exit.apply)
                                         | "print" ~> expr.map(Print.apply)
                                         | "println" ~> expr.map(PrintLn.apply)
                                         | ("if" ~> expr, "then" ~> stmt, "else" ~> stmt <~ "fi").zipped(If(_, _, _))
                                         | ("while" ~> expr, "do" ~> stmt <~ "done").zipped(While(_, _))
                                         | "begin" ~> stmt <~ "end"
                                         | chain.left1(stmt)(";" as Semi.apply)
  private lazy val lvalue: Parsley[LValue] = ident.map(Ident.apply) 
                                           | arrayElem 
                                           | pairElem
  private lazy val rvalue: Parsley[RValue] = expr 
                                             | arrayLiter 
                                             | "newpair(" ~> expr <~ "," ~> expr <~ ")"
                                             | "call" ~> ident.map(Ident.apply) <~ "(" ~> argList <~ ")"
  private lazy val argList: Parsley[List[Expr]] = (expr <::> many("," ~> expr))
  private lazy val pairElem: Parsley[LValue] = ("fst" ~> lvalue).map(Fst.apply)
                                               | ("snd" ~> lvalue).map(Snd.apply)
  private lazy val arrayLiter: Parsley[ArrayLiter] = ("[]" as ArrayLiter(List()))
                                                     | ("[" ~> expr <::> many("," ~> expr)).map(ArrayLiter(_))
  
  // private val add = (x: BigInt, y: BigInt) => x + y
  // private val sub = (x: BigInt, y: BigInt) => x - y

  // private lazy val expr: Parsley[BigInt] =
  //   chain.left1(int | "(" ~> expr <~ ")")(
  //     ("+" as add) | ("-" as sub)
  //   )
}
