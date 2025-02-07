package wacc

import parsley.Parsley
import parsley.position.pos
import parsley.ap._
import parsley.generic.ErrorBridge

trait ParserSingletonBridgePos[+A] extends ErrorBridge {
  protected def applyPos(pos: (Int, Int)): A

  infix def from(op: Parsley[Any]): Parsley[A] = error(pos.map(applyPos) <~ op)
  final def <#(op: Parsley[Any]): Parsley[A] = this from op
}

trait ParserBridgePos0[+A] extends ParserSingletonBridgePos[A] {
  def apply()(pos: (Int, Int)): A

  override final def applyPos(pos: (Int, Int)): A = this.apply()(pos)
}

trait ParserBridgePos1[-A, +B] extends ParserSingletonBridgePos[A => B] {
  def apply(a: A)(pos: (Int, Int)): B

  override final def applyPos(pos: (Int, Int)): A => B = this.apply(_)(pos)

  def apply(p: Parsley[A]): Parsley[B] = error(ap1(pos.map(applyPos), p))
}

trait ParserBridgePos2[-A, -B, +C] extends ParserSingletonBridgePos[(A, B) => C] {
  def apply(a: A, b: B)(pos: (Int, Int)): C

  override final def applyPos(pos: (Int, Int)): (A, B) => C =
    this.apply(_, _)(pos)

  def apply(p1: Parsley[A], p2: => Parsley[B]): Parsley[C] =
    error(ap2(pos.map(applyPos), p1, p2))
}

trait ParserBridgePos3[-A, -B, -C, +D] extends ParserSingletonBridgePos[(A, B, C) => D] {
  def apply(a: A, b: B, c: C)(pos: (Int, Int)): D

  override final def applyPos(pos: (Int, Int)): (A, B, C) => D =
    this.apply(_, _, _)(pos)

  def apply(p1: Parsley[A], p2: => Parsley[B], p3: => Parsley[C]): Parsley[D] =
    error(ap3(pos.map(applyPos), p1, p2, p3))
}

trait ParserBridgePos4[-A, -B, -C, -D, +E] extends ParserSingletonBridgePos[(A, B, C, D) => E] {
  def apply(a: A, b: B, c: C, d: D)(pos: (Int, Int)): E

  override final def applyPos(pos: (Int, Int)): (A, B, C, D) => E =
    this.apply(_, _, _, _)(pos)

  def apply(
      p1: Parsley[A],
      p2: => Parsley[B],
      p3: => Parsley[C],
      p4: => Parsley[D]
  ): Parsley[E] =
    error(ap4(pos.map(applyPos), p1, p2, p3, p4))
}
