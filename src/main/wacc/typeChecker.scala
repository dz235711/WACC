package wacc

import wacc.renamedast.{?, KnownType, SemType}
import wacc.renamedast.KnownType.*
import scala.collection.mutable

enum Constraint {
  case Is(refTy: SemType)
  case IsReadable
  case IsOrderable
  case IsFreeable
}
object Constraint {
  val Unconstrained: Constraint = Is(?)
  val IsArray: Constraint = Is(Array(?))
  val IsPair: Constraint = Is(Pair(?, ?))
}

sealed class TypeChecker {
  import Constraint.*

  // Map from function UID to list of function parameters
  private val funcTable: mutable.Map[Int, List[renamedast.Ident]] =
    mutable.Map()

  /** Determines whether two types are equal, and if so, what the most specific of them is. */
  extension (ty: SemType)
    private infix def ~(refTy: SemType): Option[SemType] = (ty, refTy) match {
      // Check invariant cases
      case (Array(Array(Char)), Array(String))     => None
      case (Pair(_, Array(Char)), Pair(_, String)) => None
      case (Pair(Array(Char), _), Pair(String, _)) => None
      case (?, refTy)                              => Some(refTy)
      case (ty, ?)                                 => Some(ty)
      case (Array(ty), Array(refTy)) => (ty ~ refTy).map(Array.apply)
      case (Pair(ty1, ty2), Pair(refTy1, refTy2)) =>
        for {
          newTy1 <- ty1 ~ refTy1
          newTy2 <- ty2 ~ refTy2
        } yield Pair(newTy1, newTy2)
      case (Array(Char), String)      => Some(String)
      case (ty, refTy) if ty == refTy => Some(ty)
      case _                          => None
    }

  /** Determines whether a type satisfies a constraint. */
  extension (ty: SemType)
    private def satisfies(c: Constraint): Option[SemType] = (ty, c) match {
      // Check the rest of the cases
      case (ty, Is(refTy)) =>
        (ty ~ refTy).orElse {
          // TODO: Error handling - type mismatch
          println("Type mismatch")
          None
        }
      case (?, _) => Some(?) // Unconstrained types satisfy all constraints
      case (kty @ Array(_), IsArray)        => Some(kty)
      case (kty @ Pair(_, _), IsPair)       => Some(kty)
      case (kty @ (Int | Char), IsReadable) => Some(kty)
      case (_, IsReadable)                  =>
        // TODO: Error handling - tried to read a non-readable type
        println("tried to read a non-readable type")
        None
      case (kty @ (Int | Char), IsOrderable) => Some(kty)
      case (_, IsOrderable)                  =>
        // TODO: Error handling - tried to order (e.g. >=) a non-orderable type
        println("Tried to order a non-orderable type")
        None
      case (kty @ (Array(_) | Pair(_, _)), IsFreeable) => Some(kty)
      case (_, IsFreeable)                             =>
        // TODO: Error handling - tried to free a non-freeable type
        println("Tried to free a non-freeable type")
        None
      case _ => None
    }

  /** Checks a program and returns a typed program.
   * 
   * @param p The renamed program to check
   * @return The typed program
   */
  def checkProg(p: renamedast.Program): TypedAST.Program = {
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
  private def checkFunc(func: renamedast.Func): TypedAST.Func = {
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
      stmt: renamedast.Stmt,
      retC: Constraint
  ): TypedAST.Stmt = stmt match {
    case renamedast.Skip => TypedAST.Skip
    case renamedast.Decl(v, r) =>
      val (ty, vTyped) = checkIdent(v, Unconstrained)
      // Identifier in declaration *will* have a type
      val (_, rTyped) = checkRVal(r, Is(ty.get))
      TypedAST.Decl(vTyped, rTyped)
    case renamedast.Asgn(l, r) =>
      val (lTy, lTyped) = checkLVal(l, Unconstrained)
      val (rTy, rTyped) = checkRVal(r, Is(lTy.getOrElse(?)))

      // Make sure the assignment has a known type
      rTy match {
        case Some(?) =>
        // TODO: Error handling
        case _ => ()
      }

      TypedAST.Asgn(lTyped, rTyped)
    case renamedast.Read(l)   => TypedAST.Read(checkLVal(l, IsReadable)._2)
    case renamedast.Free(e)   => TypedAST.Free(checkExpr(e, IsFreeable)._2)
    case renamedast.Return(e) => TypedAST.Return(checkExpr(e, retC)._2)
    case renamedast.Exit(e)   => TypedAST.Exit(checkExpr(e, Is(Int))._2)
    case renamedast.Print(e)  => TypedAST.Print(checkExpr(e, Unconstrained)._2)
    case renamedast.PrintLn(e) =>
      TypedAST.PrintLn(checkExpr(e, Unconstrained)._2)
    case renamedast.If(cond, s1, s2) =>
      TypedAST.If(
        checkExpr(cond, Is(Bool))._2,
        checkStmt(s1, retC),
        checkStmt(s2, retC)
      )
    case renamedast.While(cond, body) =>
      TypedAST.While(
        checkExpr(cond, Is(Bool))._2,
        checkStmt(body, retC)
      )
    case renamedast.Begin(body) => TypedAST.Begin(checkStmt(body, retC))
    case renamedast.Semi(s1, s2) =>
      TypedAST.Semi(checkStmt(s1, retC), checkStmt(s2, retC))
  }

  private def checkExpr(
      expr: renamedast.Expr,
      c: Constraint
  ): (Option[SemType], TypedAST.Expr) = expr match {
    case renamedast.Not(e) =>
      (Bool.satisfies(c), TypedAST.Not(checkExpr(e, Is(Bool))._2))
    case renamedast.Negate(e) =>
      (Int.satisfies(c), TypedAST.Negate(checkExpr(e, Is(Int))._2))
    case renamedast.Len(e) =>
      (Int.satisfies(c), TypedAST.Len(checkExpr(e, IsArray)._2))
    case renamedast.Ord(e) =>
      (Int.satisfies(c), TypedAST.Ord(checkExpr(e, Is(Char))._2))
    case renamedast.Chr(e) =>
      (Char.satisfies(c), TypedAST.Chr(checkExpr(e, Is(Int))._2))
    case renamedast.Mult(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mult.apply)
    case renamedast.Div(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Div.apply)
    case renamedast.Mod(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Mod.apply)
    case renamedast.Add(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Add.apply)
    case renamedast.Sub(e1, e2) =>
      checkArithmetic(e1, e2, c)(TypedAST.Sub.apply)
    case renamedast.Greater(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Greater.apply)
    case renamedast.GreaterEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.GreaterEq.apply)
    case renamedast.Smaller(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.Smaller.apply)
    case renamedast.SmallerEq(e1, e2) =>
      checkOrdering(e1, e2, c)(TypedAST.SmallerEq.apply)
    case renamedast.Equals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.Equals.apply)
    case renamedast.NotEquals(e1, e2) =>
      checkEquality(e1, e2, c)(TypedAST.NotEquals.apply)
    case renamedast.And(e1, e2) => checkLogical(e1, e2, c)(TypedAST.And.apply)
    case renamedast.Or(e1, e2)  => checkLogical(e1, e2, c)(TypedAST.Or.apply)
    case renamedast.IntLiter(x) =>
      (Int.satisfies(c), TypedAST.IntLiter(x))
    case renamedast.BoolLiter(b) =>
      (Bool.satisfies(c), TypedAST.BoolLiter(b))
    case renamedast.CharLiter(ch) =>
      (Char.satisfies(c), TypedAST.CharLiter(ch))
    case renamedast.StringLiter(s) =>
      (String.satisfies(c), TypedAST.StringLiter(s))
    case renamedast.PairLiter =>
      (Pair(?, ?).satisfies(c), TypedAST.PairLiter)
    case id @ renamedast.Ident(_)           => checkIdent(id, c)
    case arrEl @ renamedast.ArrayElem(_, _) => checkArrayElem(arrEl, c)
    case renamedast.NestedExpr(e)           => checkExpr(e, c)
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
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    (
      Int.satisfies(c),
      build(checkExpr(e1, Is(Int))._2, checkExpr(e2, Is(Int))._2)
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
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    val (lTy, lTyped) = checkExpr(e1, IsOrderable)
    val (_, rTyped) = checkExpr(e2, lTy.map(Is(_)).getOrElse(IsOrderable))
    (Bool.satisfies(c), build(lTyped, rTyped))

  /** Checks an equality expression and returns a typed equality expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the equality operation's type
   * @param build The function to build the typed equality expression
   * @return The typed equality expression
   */
  private def checkEquality(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    (
      Bool.satisfies(c),
      build(checkExpr(e1, Unconstrained)._2, checkExpr(e2, Unconstrained)._2)
    )

  /** Checks a logical expression and returns a typed logical expression.
   *
   * @param e1 The first expression
   * @param e2 The second expression
   * @param c The constraint on the logical operation's type
   * @param build The function to build the typed logical expression
   * @return The typed logical expression
   */
  private def checkLogical(
      e1: renamedast.Expr,
      e2: renamedast.Expr,
      c: Constraint
  )(
      build: (TypedAST.Expr, TypedAST.Expr) => TypedAST.Expr
  ): (Option[SemType], TypedAST.Expr) =
    (
      Bool.satisfies(c),
      build(checkExpr(e1, Is(Bool))._2, checkExpr(e2, Is(Bool))._2)
    )

  /** Checks an lvalue and returns a typed lvalue.
   *
   * @param lval The lvalue to check
   * @param c The constraint on the lvalue's type
   * @return The typed lvalue
   */
  private def checkLVal(
      lval: renamedast.LValue,
      c: Constraint
  ): (Option[SemType], TypedAST.LValue) = lval match {
    case id @ renamedast.Ident(_)           => checkIdent(id, c)
    case arrEl @ renamedast.ArrayElem(_, _) => checkArrayElem(arrEl, c)
    case fst @ renamedast.Fst(_)            => checkFst(fst, c)
    case snd @ renamedast.Snd(_)            => checkSnd(snd, c)
  }

  /** Checks an rvalue and returns a typed rvalue.
   *
   * @param rval The rvalue to check
   * @param c The constraint on the rvalue's type
   * @return The typed rvalue
   */
  private def checkRVal(
      rval: renamedast.RValue,
      c: Constraint
  ): (Option[SemType], TypedAST.RValue) = rval match {
    case renamedast.ArrayLiter(es) =>
      Array(?).satisfies(c) match {
        case Some(Array(ty)) =>
          val esTyped = es.map(checkExpr(_, Is(ty))._2)
          (Some(Array(ty)), TypedAST.ArrayLiter(esTyped, Array(ty)))
        case _ =>
          (None, TypedAST.ArrayLiter(es.map(checkExpr(_, Unconstrained)._2), ?))
      }
    case renamedast.NewPair(e1, e2) =>
      Pair(?, ?).satisfies(c) match {
        case Some(Pair(ty1, ty2)) =>
          val e1Typed = checkExpr(e1, Is(ty1))._2
          val e2Typed = checkExpr(e2, Is(ty2))._2
          (
            Some(Pair(ty1, ty2)),
            TypedAST.NewPair(e1Typed, e2Typed, Pair(ty1, ty2))
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
    case fst @ renamedast.Fst(_)  => checkFst(fst, c)
    case snd @ renamedast.Snd(_)  => checkSnd(snd, c)
    case renamedast.Call(v, args) =>
      /* Check that the return type of the function is the same as the
       * constraint */
      val (ty, vTyped) = checkIdent(v, c)
      /* Check that the arguments are of the types expected by the function from
       * funcTable */
      val argsTyped = args.zip(funcTable(vTyped.id)).map { (arg, expected) =>
        checkExpr(arg, Is(expected.v.declType))._2
      }
      (ty, TypedAST.Call(vTyped, argsTyped, ty.get))
    case e: renamedast.Expr => checkExpr(e, c)
  }

  /** Checks an identifier and returns a typed identifier.
   *
   * @param ident The identifier to check
   * @param c The constraint on the identifier's type
   * @return The typed identifier
   */
  private def checkIdent(
      ident: renamedast.Ident,
      c: Constraint
  ): (Option[SemType], TypedAST.Ident) =
    val qName = ident.v
    val ty = qName.declType
    val uid = qName.UID
    val ty2 = ty.satisfies(c)
    (ty2, TypedAST.Ident(uid, ty2.getOrElse(?)))

  /** Checks an array element and returns a typed array element.
   *
   * @param arrElem The array element to check
   * @param c The constraint on the array element's type
   * @return The typed array element
   */
  private def checkArrayElem(
      arrElem: renamedast.ArrayElem,
      c: Constraint
  ): (Option[SemType], TypedAST.ArrayElem) =
    val (arrTy, vTyped) = checkIdent(arrElem.v, IsArray)
    val esTyped = arrElem.es.map(checkExpr(_, Is(Int))._2)

    // Calculate the shape of the array based on how it is indexed
    // e.g. y[][] -> Array(Array(?))
    val indexedArrTy = arrElem.es.foldLeft[SemType](?)((acc, _) => Array(acc))

    val result = for {
      ty1 <- arrTy
      // Check that the array type satisfies the constraint
      ty2 <- ty1.satisfies(c)
      // Check that the array type is indexed correctly
      ty3 <- ty2.satisfies(Is(indexedArrTy))
    } yield (Some(ty3), TypedAST.ArrayElem(vTyped, esTyped, ty3))

    result.getOrElse(None, TypedAST.ArrayElem(vTyped, esTyped, ?))

  /** Checks a Fst expression and returns a typed Fst expression.
   *
   * @param fst The Fst expression to check
   * @param c The constraint on the first element of the pair's type
   * @return The typed Fst expression
   */
  private def checkFst(
      fst: renamedast.Fst,
      c: Constraint
  ): (Option[SemType], TypedAST.Fst) = {
    val (ty, lTyped) = checkLVal(fst.l, IsPair)
    ty match {
      case Some(Pair(ty1, _)) =>
        val ty1C = ty1.satisfies(c)
        (ty1C, TypedAST.Fst(lTyped, ty1C.getOrElse(?)))
      case _ => (None, TypedAST.Fst(lTyped, ?))
    }
  }

  /** Checks a Snd expression and returns a typed Snd expression.
   *
   * @param snd The Snd expression to check
   * @param c   The constraint on the second element of the pair's type
   * @return The typed Snd expression
   */
  private def checkSnd(
      snd: renamedast.Snd,
      c: Constraint
  ): (Option[SemType], TypedAST.Snd) = {
    val (ty, lTyped) = checkLVal(snd.l, IsPair)
    ty match {
      case Some(Pair(_, ty2)) =>
        val ty2C = ty2.satisfies(c)
        (ty2C, TypedAST.Snd(lTyped, ty2C.getOrElse(?)))
      case _ => (None, TypedAST.Snd(lTyped, ?))
    }
  }
}
