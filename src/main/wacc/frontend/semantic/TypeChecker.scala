package wacc

import RenamedAST.{?, KnownType, SemType}
import RenamedAST.KnownType.*
import scala.collection.mutable
import WaccErrorBuilder.constructSpecialised
import RenamedAST.getTypeName

enum Constraint {
  case Is(refTy: SemType)
  case IsReadable
  case IsOrderable
  case IsFreeable
}
object Constraint {
  val Unconstrained: Constraint = Is(?)
  val IsArray: Constraint = Is(ArrayType(?))
  val IsPair: Constraint = Is(PairType(?, ?))
}

sealed class TypeChecker {
  import Constraint.*

  // Map from function UID to list of function parameters
  private val funcTable: mutable.Map[Int, List[RenamedAST.Ident]] =
    mutable.Map()

  /** Determines whether two types are equal, and if so, what the most specific of them is. */
  extension (ty: SemType)
    private infix def ~(refTy: SemType): Option[SemType] = (ty, refTy) match {
      // Check invariant cases
      case (ArrayType(ArrayType(CharType)), ArrayType(StringType))     => None
      case (PairType(_, ArrayType(CharType)), PairType(_, StringType)) => None
      case (PairType(ArrayType(CharType), _), PairType(StringType, _)) => None
      case (?, refTy)                                                  => Some(refTy)
      case (ty, ?)                                                     => Some(ty)
      case (ArrayType(ty), ArrayType(refTy)) =>
        (ty ~ refTy).map(ArrayType.apply)
      case (PairType(ty1, ty2), PairType(refTy1, refTy2)) =>
        for {
          newTy1 <- ty1 ~ refTy1
          newTy2 <- ty2 ~ refTy2
        } yield PairType(newTy1, newTy2)
      case (ArrayType(?), StringType)        => Some(ArrayType(CharType))
      case (ArrayType(CharType), StringType) => Some(StringType)
      case (ty, refTy) if ty == refTy        => Some(ty)
      case _                                 => None
    }

  /** Determines whether a type satisfies a constraint. */
  extension (ty: SemType)
    private def satisfies(
        pos: (Int, Int)
    )(c: Constraint)(using ctx: ListContext[WaccError]): Option[SemType] = (ty, c) match {
      // Check the rest of the cases
      case (ty, Is(refTy)) =>
        (ty ~ refTy).orElse {
          ctx.add(
            constructSpecialised(
              pos,
              1,
              s"Type mismatch between ${getTypeName(ty)} and ${getTypeName(refTy)}"
            )
          )
          None
        }
      case (kty: ArrayType, IsArray)                => Some(kty)
      case (kty: PairType, IsPair)                  => Some(kty)
      case (kty @ (IntType | CharType), IsReadable) => Some(kty)
      case (_, IsReadable) =>
        ctx.add(
          constructSpecialised(pos, 1, "Tried to read a non-readable type")
        )
        None
      case (kty @ (IntType | CharType), IsOrderable) => Some(kty)
      case (_, IsOrderable) =>
        ctx.add(
          constructSpecialised(pos, 1, "Tried to order a non-orderable type")
        )
        None
      case (kty: (ArrayType | PairType), IsFreeable) => Some(kty)
      case (_, IsFreeable) =>
        ctx.add(
          constructSpecialised(pos, 1, "Tried to free a non-freeable type")
        )
        None
      case _ => None
    }

  /** Checks a program and returns a typed program.
   *
   * @param p The renamed program to check
   * @return The typed program
   */
  def checkProg(
      p: RenamedAST.Program
  )(using ctx: ListContext[WaccError]): TypedAST.Program = {
    // Populate funcTable
    p.fs.foreach(f => funcTable += (f.v.v.UID -> f.params))

    val funcs = p.fs.map(checkFunc)
    val body = checkStmt(p.body, Unconstrained)

    TypedAST.Program(funcs, body)
  }

  /** Checks a function and returns a typed function.
   *
   * @param func The function to check
   * @return The typed function
   */
  private def checkFunc(
      func: RenamedAST.Func
  )(using ctx: ListContext[WaccError]): TypedAST.Func = {
    val id = checkIdent(func.v, Unconstrained)._2
    val params = func.params.map(checkIdent(_, Unconstrained)._2)
    val body = checkStmt(func.body, Is(id.getType))
    TypedAST.Func(id, params, body)
  }

  /** Checks a statement and returns a typed statement.
   *
   * @param stmt The statement to check
   * @param retC The constraint on the return type of the statement
   * @return The typed statement
   */
  private def checkStmt(
      stmt: RenamedAST.Stmt,
      retC: Constraint
  )(using ctx: ListContext[WaccError]): TypedAST.Stmt = stmt match {
    case RenamedAST.Skip() => TypedAST.Skip
    case RenamedAST.Decl(v, r) =>
      val (ty, vTyped) = checkIdent(v, Unconstrained)
      // Identifier in declaration *will* have a type
      val (_, rTyped) = checkRVal(r, Is(ty.get))
      TypedAST.Decl(vTyped, rTyped)
    case RenamedAST.Asgn(l, r) =>
      val (lTy, lTyped) = checkLVal(l, Unconstrained)
      val (rTy, rTyped) = checkRVal(r, Is(lTy.getOrElse(?)))

      // Make sure the assignment has a known type
      rTy match {
        case Some(?) =>
          ctx.add(
            constructSpecialised(
              r.pos,
              1,
              "Assignment has unknown type"
            )
          )
        case _ => ()
      }

      TypedAST.Asgn(lTyped, rTyped)
    case RenamedAST.Read(l)   => TypedAST.Read(checkLVal(l, IsReadable)._2)
    case RenamedAST.Free(e)   => TypedAST.Free(checkExpr(e, IsFreeable)._2)
    case RenamedAST.Return(e) => TypedAST.Return(checkExpr(e, retC)._2)
    case RenamedAST.Exit(e)   => TypedAST.Exit(checkExpr(e, Is(IntType))._2)
    case RenamedAST.Print(e)  => TypedAST.Print(checkExpr(e, Unconstrained)._2)
    case RenamedAST.PrintLn(e) =>
      TypedAST.PrintLn(checkExpr(e, Unconstrained)._2)
    case RenamedAST.If(cond, s1, s2) =>
      TypedAST.If(
        checkExpr(cond, Is(BoolType))._2,
        checkStmt(s1, retC),
        checkStmt(s2, retC)
      )
    case RenamedAST.While(cond, body) =>
      TypedAST.While(
        checkExpr(cond, Is(BoolType))._2,
        checkStmt(body, retC)
      )
    case RenamedAST.Begin(body) => TypedAST.Begin(checkStmt(body, retC))
    case RenamedAST.Semi(s1, s2) =>
      TypedAST.Semi(checkStmt(s1, retC), checkStmt(s2, retC))
  }

  private def checkExpr(
      expr: RenamedAST.Expr,
      c: Constraint
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Expr) = expr match {
    case RenamedAST.Not(e) =>
      (
        BoolType.satisfies(e.pos)(c),
        TypedAST.Not(checkExpr(e, Is(BoolType))._2)
      )
    case RenamedAST.Negate(e) =>
      (
        IntType.satisfies(e.pos)(c),
        TypedAST.Negate(checkExpr(e, Is(IntType))._2)
      )
    case RenamedAST.Len(e) =>
      (IntType.satisfies(e.pos)(c), TypedAST.Len(checkExpr(e, IsArray)._2))
    case RenamedAST.Ord(e) =>
      (
        IntType.satisfies(e.pos)(c),
        TypedAST.Ord(checkExpr(e, Is(CharType))._2)
      )
    case RenamedAST.Chr(e) =>
      (
        CharType.satisfies(e.pos)(c),
        TypedAST.Chr(checkExpr(e, Is(IntType))._2)
      )
    case RenamedAST.Mult(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mult.apply)
    case RenamedAST.Div(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Div.apply)
    case RenamedAST.Mod(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mod.apply)
    case RenamedAST.Add(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Add.apply)
    case RenamedAST.Sub(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Sub.apply)
    case RenamedAST.Greater(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Greater.apply)
    case RenamedAST.GreaterEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.GreaterEq.apply)
    case RenamedAST.Smaller(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Smaller.apply)
    case RenamedAST.SmallerEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.SmallerEq.apply)
    case RenamedAST.Equals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.Equals.apply)
    case RenamedAST.NotEquals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.NotEquals.apply)
    case RenamedAST.And(e1, e2) => checkLogical(e1, e2, c)(TypedAST.And.apply)
    case RenamedAST.Or(e1, e2)  => checkLogical(e1, e2, c)(TypedAST.Or.apply)
    case RenamedAST.IntLiter(x) =>
      (IntType.satisfies(expr.pos)(c), TypedAST.IntLiter(x))
    case RenamedAST.BoolLiter(b) =>
      (BoolType.satisfies(expr.pos)(c), TypedAST.BoolLiter(b))
    case RenamedAST.CharLiter(ch) =>
      (CharType.satisfies(expr.pos)(c), TypedAST.CharLiter(ch))
    case RenamedAST.StringLiter(s) =>
      (StringType.satisfies(expr.pos)(c), TypedAST.StringLiter(s))
    case RenamedAST.PairLiter() =>
      (PairType(?, ?).satisfies(expr.pos)(c), TypedAST.PairLiter)
    case id: RenamedAST.Ident        => checkIdent(id, c)
    case arrEl: RenamedAST.ArrayElem => checkArrayElem(arrEl, c)
    case RenamedAST.NestedExpr(e)    => checkExpr(e, c)
  }

  /** Checks an arithmetic expression and returns a typed arithmetic expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the arithmetic operation's type
   * @param build The function to build the typed arithmetic expression
   * @return The typed arithmetic expression
   */
  private def checkArithmetic(
      e1: RenamedAST.Expr,
      e2: RenamedAST.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Expr) =
    (
      IntType.satisfies(e1.pos)(c),
      build(checkExpr(e1, Is(IntType))._2, checkExpr(e2, Is(IntType))._2)
    )

  /** Checks a comparison expression and returns a typed comparison expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the ordering operation's type
   * @param build The function to build the typed comparison expression
   * @return The typed comparison expression
   */
  private def checkOrdering(
      e1: RenamedAST.Expr,
      e2: RenamedAST.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Expr) =
    val (lTy, lTyped) = checkExpr(e1, IsOrderable)
    val (_, rTyped) = checkExpr(e2, lTy.map(Is(_)).getOrElse(IsOrderable))
    (BoolType.satisfies(e1.pos)(c), build(lTyped, rTyped))

  /** Checks an equality expression and returns a typed equality expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the equality operation's type
   * @param build The function to build the typed equality expression
   * @return The typed equality expression
   */
  private def checkEquality(
      e1: RenamedAST.Expr,
      e2: RenamedAST.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Expr) =
    val (lTy, lTyped) = checkExpr(e1, Unconstrained)
    val (_, rTyped) = checkExpr(e2, lTy.map(Is(_)).getOrElse(Unconstrained))
    (BoolType.satisfies(e1.pos)(c), build(lTyped, rTyped))

  /** Checks a logical expression and returns a typed logical expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the logical operation's type
   * @param build The function to build the typed logical expression
   * @return The typed logical expression
   */
  private def checkLogical(
      e1: RenamedAST.Expr,
      e2: RenamedAST.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Expr) =
    (
      BoolType.satisfies(e1.pos)(c),
      build(checkExpr(e1, Is(BoolType))._2, checkExpr(e2, Is(BoolType))._2)
    )

  /** Checks an lvalue and returns a typed lvalue.
   *
   * @param lval The lvalue to check
   * @param c The constraint on the lvalue's type
   * @return The typed lvalue
   */
  private def checkLVal(
      lval: RenamedAST.LValue,
      c: Constraint
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.LValue) = lval match {
    case id: RenamedAST.Ident        => checkIdent(id, c)
    case arrEl: RenamedAST.ArrayElem => checkArrayElem(arrEl, c)
    case RenamedAST.Fst(l)           => checkPairElem(l, c, true)
    case RenamedAST.Snd(l)           => checkPairElem(l, c, false)
  }

  /** Checks an rvalue and returns a typed rvalue.
   *
   * @param rval The rvalue to check
   * @param c The constraint on the rvalue's type
   * @return The typed rvalue
   */
  private def checkRVal(
      rval: RenamedAST.RValue,
      c: Constraint
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.RValue) = rval match {
    case RenamedAST.ArrayLiter(es) =>
      // Try to unify the types of the array elements
      val elTy = es
        .foldLeft[Option[SemType]](Some(?)) { (ty, expr) =>
          ty.flatMap { ty =>
            val eTy = checkExpr(expr, Unconstrained)._1
            // Try unifying either direction
            eTy.flatMap(et => (ty ~ et).orElse(et ~ ty))
          }
        }
        .getOrElse {
          ctx.add(
            constructSpecialised(
              rval.pos,
              1,
              s"Literal contains mix of ${es.map(e => getTypeName(checkExpr(e, Unconstrained)._1.getOrElse(?))).mkString(", ")}"
            )
          )
          ?
        }

      ArrayType(elTy).satisfies(rval.pos)(c) match {
        case Some(ArrayType(ty)) =>
          val esTyped = es.map(checkExpr(_, Is(ty))._2)
          (Some(ArrayType(ty)), TypedAST.ArrayLiter(esTyped, ArrayType(ty)))
        case _ =>
          (None, TypedAST.ArrayLiter(es.map(checkExpr(_, Unconstrained)._2), ?))
      }
    case RenamedAST.NewPair(e1, e2) =>
      PairType(?, ?).satisfies(rval.pos)(c) match {
        case Some(PairType(ty1, ty2)) =>
          val e1Typed = checkExpr(e1, Is(ty1))._2
          val e2Typed = checkExpr(e2, Is(ty2))._2
          (
            Some(PairType(ty1, ty2)),
            TypedAST.NewPair(e1Typed, e2Typed, PairType(ty1, ty2))
          )
        case _ =>
          (
            None,
            TypedAST.NewPair(
              checkExpr(e1, Unconstrained)._2,
              checkExpr(e2, Unconstrained)._2,
              ?
            )
          )
      }
    case RenamedAST.Fst(l)        => checkPairElem(l, c, true)
    case RenamedAST.Snd(l)        => checkPairElem(l, c, false)
    case RenamedAST.Call(v, args) =>
      /* Check that the return type of the function is the same as the constraint */
      val (ty, vTyped) = checkIdent(v, c)
      /* Check that the arguments are of the types expected by the function from funcTable */
      val argsTyped = args.zip(funcTable.getOrElse(vTyped.id, List())).map { (arg, expected) =>
        checkExpr(arg, Is(expected.v.declType))._2
      }
      (ty, TypedAST.Call(vTyped, argsTyped, ty.getOrElse(?)))
    case e: RenamedAST.Expr => checkExpr(e, c)
  }

  /** Checks an identifier and returns a typed identifier.
   *
   * @param ident The identifier to check
   * @param c The constraint on the identifier's type
   * @return The typed identifier
   */
  private def checkIdent(
      ident: RenamedAST.Ident,
      c: Constraint
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Ident) =
    val qName = ident.v
    val ty = qName.declType
    val uid = qName.UID
    val ty2 = ty.satisfies(ident.pos)(c)
    (ty2, TypedAST.Ident(uid, ty2.getOrElse(?)))

  /** Checks an array element and returns a typed array element.
   *
   * @param arrElem The array element to check
   * @param c The constraint on the array element's type
   * @return The typed array element
   */
  private def checkArrayElem(
      arrElem: RenamedAST.ArrayElem,
      c: Constraint
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.ArrayElem) =
    val (arrTy, vTyped) = checkIdent(arrElem.v, IsArray)
    val esTyped = arrElem.es.map(checkExpr(_, Is(IntType))._2)

    // Unwrap the array type once for each index
    val arrElemTy = esTyped.foldLeft(arrTy)((acc, _) =>
      acc match {
        case Some(ArrayType(ty)) => Some(ty)
        case _ =>
          ctx.add(
            constructSpecialised(
              arrElem.pos,
              1,
              "Tried to index a non-array type"
            )
          )
          None
      }
    )

    // Check that the array element type satisfies the constraint
    val arrElemTyC = arrElemTy.flatMap(_.satisfies(arrElem.pos)(c))

    (arrElemTyC, TypedAST.ArrayElem(vTyped, esTyped, arrElemTyC.getOrElse(?)))

  /** Checks a pair element and returns a typed pair element.
   *
   * @param l The lvalue of the pair element
   * @param c The constraint on the pair element's type
   * @param isFirst Whether the element is the first element of the pair
   * @return The typed pair element
   */
  private def checkPairElem(
      l: RenamedAST.LValue,
      c: Constraint,
      isFirst: Boolean
  )(using ctx: ListContext[WaccError]): (Option[SemType], TypedAST.Fst | TypedAST.Snd) = {
    val (ty, lTyped) = checkLVal(l, IsPair)
    ty match {
      case Some(PairType(ty1, ty2)) =>
        val elemTy = if (isFirst) ty1 else ty2
        val elemTyC = elemTy.satisfies(l.pos)(c)
        (
          elemTyC,
          if (isFirst) TypedAST.Fst(lTyped, elemTyC.getOrElse(?))
          else TypedAST.Snd(lTyped, elemTyC.getOrElse(?))
        )
      case _ =>
        (
          None,
          if (isFirst) TypedAST.Fst(lTyped, ?) else TypedAST.Snd(lTyped, ?)
        )
    }
  }
}

object TypeChecker extends TypeChecker
