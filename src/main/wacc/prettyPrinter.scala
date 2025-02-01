package wacc

val INDENTATION_SIZE = 2

def prettyPrint(prog: Program): String = 
  "begin" + prog._1.map(prettyPrintFunc(_)).mkString("\n\n").indent(INDENTATION_SIZE)
    + "\n" + prettyPrintStmt(prog._2).indent(INDENTATION_SIZE) + "end"


def prettyPrintType(t: Type | PairElemType): String =
  t match
    case BaseType.Int    => "int"
    case BaseType.Bool   => "bool"
    case BaseType.Char   => "char"
    case BaseType.String => "string"

    case ArrayType(t) => prettyPrintType(t) + "[]"
    case PairType(t1, t2) =>
      s"pair(${prettyPrintType(t1)}, ${prettyPrintType(t2)})"
    case ErasedPair => "pair"

def prettyPrintExpr(e: Expr): String =
  e match
    case Not(e)    => "!" + prettyPrintExpr(e)
    case Negate(e) => "-" + prettyPrintExpr(e)
    case Len(e)    => "len " + prettyPrintExpr(e)
    case Ord(e)    => "ord " + prettyPrintExpr(e)
    case Chr(e)    => "chr " + prettyPrintExpr(e)

    case Mult(e1, e2)      => prettyPrintExpr(e1) + " * " + prettyPrintExpr(e2)
    case Div(e1, e2)       => prettyPrintExpr(e1) + " / " + prettyPrintExpr(e2)
    case Mod(e1, e2)       => prettyPrintExpr(e1) + " % " + prettyPrintExpr(e2)
    case Add(e1, e2)       => prettyPrintExpr(e1) + " + " + prettyPrintExpr(e2)
    case Sub(e1, e2)       => prettyPrintExpr(e1) + " - " + prettyPrintExpr(e2)
    case Greater(e1, e2)   => prettyPrintExpr(e1) + " > " + prettyPrintExpr(e2)
    case GreaterEq(e1, e2) => prettyPrintExpr(e1) + " >= " + prettyPrintExpr(e2)
    case Smaller(e1, e2)   => prettyPrintExpr(e1) + " < " + prettyPrintExpr(e2)
    case SmallerEq(e1, e2) => prettyPrintExpr(e1) + " <= " + prettyPrintExpr(e2)
    case Equals(e1, e2)    => prettyPrintExpr(e1) + " == " + prettyPrintExpr(e2)
    case NotEquals(e1, e2) => prettyPrintExpr(e1) + " != " + prettyPrintExpr(e2)
    case And(e1, e2)       => prettyPrintExpr(e1) + " && " + prettyPrintExpr(e2)
    case Or(e1, e2)        => prettyPrintExpr(e1) + " || " + prettyPrintExpr(e2)

    case IntLiter(x)    => x.toString()
    case BoolLiter(b)   => if b then "true" else "false"
    case CharLiter(c)   => "'" + c.toString() + "'"
    case StringLiter(s) => "\"" + s + "\""
    case PairLiter      => "null"
    case Ident(v)       => v
    case ArrayElem(Ident(v), es) =>
      v + es.map(prettyPrintExpr(_)).mkString("[", "][", "]")

def prettyPrintLRValue(r: RValue | LValue): String =
  r match
    case ArrayLiter(es) => es.map(prettyPrintExpr(_)).mkString("[", ", ", "]")
    case NewPair(e1, e2) =>
      "newpair(" + prettyPrintExpr(e1) + ", " + prettyPrintExpr(e2) + ")"
    case Fst(l) => "fst " + prettyPrintLRValue(l)
    case Snd(l) => "snd " + prettyPrintLRValue(l)
    case Call(v, args) =>
      "call " + prettyPrintExpr(v) + args
        .map(prettyPrintExpr(_))
        .mkString("(", ", ", ")")
    case e: Expr => prettyPrintExpr(e)

def prettyPrintStmt(s: Stmt): String =
  s match
    case Skip => "skip"
    case Decl(t, v, r) =>
      prettyPrintType(t) + " " + prettyPrintExpr(v) 
        + " = " + prettyPrintLRValue(r)
    case Asgn(l, r) => prettyPrintLRValue(l) + " = " + prettyPrintLRValue(r)
    case Read(l)    => "read " + prettyPrintLRValue(l)
    case Free(e)    => "free " + prettyPrintExpr(e)
    case Return(e)  => "return " + prettyPrintExpr(e)
    case Exit(e)    => "exit " + prettyPrintExpr(e)
    case Print(e)   => "print " + prettyPrintExpr(e)
    case PrintLn(e) => "println " + prettyPrintExpr(e)
    case If(cond, s1, s2) =>
      "if " + prettyPrintExpr(cond) + " then\n" + prettyPrintStmt(s1).indent(INDENTATION_SIZE) 
        + "else\n" + prettyPrintStmt(s2).indent(INDENTATION_SIZE) + "fi"
    case While(cond, body) =>
      "while " + prettyPrintExpr(cond) + " do\n" + prettyPrintStmt(body).indent(INDENTATION_SIZE) + "done"
    case Begin(body) =>
      "begin\n" + prettyPrintStmt(body).indent(INDENTATION_SIZE) + "end"
    case Semi(s1, s2) => prettyPrintStmt(s1) + ";\n" + prettyPrintStmt(s2)

def prettyPrintFunc(f: Func): String = {
  val Func(t, v, params, body) = f
  prettyPrintType(t) + " " + prettyPrintExpr(v) + params.map(prettyPrintParam(_)).mkString("(", ", ", ")") + " is\n" + prettyPrintStmt(body).indent(INDENTATION_SIZE) + "end"
}

def prettyPrintParam(param: Param): String = 
  prettyPrintType(param._1) + " " + prettyPrintExpr(param._2)
