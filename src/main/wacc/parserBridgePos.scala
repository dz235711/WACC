package wacc

import parsley.Parsley
import parsley.position.pos
import parsley.ap._

trait ParserBridgePos0[+B] {
    def apply()(pos: (Int, Int)): B

    private def applyPos(pos: (Int, Int)): B = this.apply()(pos)

    infix def from(op: Parsley[Any]): Parsley[B] = pos.map(applyPos) <~ op
    infix def <#(op: Parsley[Any]): Parsley[B] = this from op
}

/**
 * Similar to PraserBridge1, except it is combined with the pos combinator for data types that require a position.
 */
trait ParserBridgePos1[-A, +B] {
    def apply(a: A)(pos: (Int, Int)): B

    private def applyPos(pos: (Int, Int)): A => B = this.apply(_)(pos)

    def apply(a: Parsley[A]): Parsley[B] = ap1(pos.map(applyPos), a)

    infix def from(op: Parsley[Any]): Parsley[A => B] = pos.map(applyPos) <~ op
    infix def <#(op: Parsley[Any]): Parsley[A => B] = this from op

}

trait ParserBridgePos2[-A, -B, +C] {
    def apply(a: A, b: B)(pos: (Int, Int)): C

    private def applyPos(pos: (Int, Int)): (A, B) => C = this.apply(_, _)(pos)

    def apply(a: Parsley[A], b: =>Parsley[B]): Parsley[C] =
        ap2(pos.map(applyPos), a, b)

    infix def from(op: Parsley[Any]): Parsley[(A, B) => C] = pos.map(applyPos) <~ op
    infix def <#(op: Parsley[Any]): Parsley[(A, B) => C] = this from op
}

trait ParserBridgePos3[-A, -B, -C, +D] {
    def apply(a: A, b: B, c: C)(pos: (Int, Int)): D

    private def applyPos(pos: (Int, Int)): (A, B, C) => D = this.apply(_, _, _)(pos)

    def apply(a: Parsley[A], b: =>Parsley[B], c: =>Parsley[C]): Parsley[D] =
        ap3(pos.map(applyPos), a, b, c)

    infix def from(op: Parsley[Any]): Parsley[(A, B, C) => D] = pos.map(applyPos) <~ op
    infix def <#(op: Parsley[Any]): Parsley[(A, B, C) => D] = this from op
}
trait ParserBridgePos4[-A, -B, -C, -D, +E] {
    def apply(a: A, b: B, c: C, d: D)(pos: (Int, Int)): E

    private def applyPos(pos: (Int, Int)): (A, B, C, D) => E = this.apply(_, _, _, _)(pos)

    def apply(a: Parsley[A], b: =>Parsley[B], c: =>Parsley[C], d: =>Parsley[D]): Parsley[E] =
        ap4(pos.map(applyPos), a, b, c, d)
    
    infix def from(op: Parsley[Any]): Parsley[(A, B, C, D) => E] = pos.map(applyPos) <~ op
    infix def <#(op: Parsley[Any]): Parsley[(A, B, C, D) => E] = this from op
}