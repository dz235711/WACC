package wacc

import RenamedAST.*
import scalax.collection.generic.AnyDiEdge
import scalax.collection.mutable.Graph
import scalax.collection.edges.DiEdgeImplicits

class DeadCodeRemover {
  // Node is function's ident's UID
  // Edge is a call from one function to another
  private val g = Graph[Int, AnyDiEdge]()
  private val MainNode = -1

  def removeDeadCode(prog: Program): Program = {
    // Populate the graph with the functions
    g.add(MainNode)
    for (func <- prog.fs) {
      g.add(func.v.v.UID)
    }

    // Add edges to the graph
    for (callee <- callsFrom(prog.body)) {
      g.add(MainNode ~> callee)
    }
    for (func <- prog.fs) {
      val caller = func.v.v.UID
      for (callee <- callsFrom(func.body)) {
        g.add(caller ~> callee)
      }
    }

    // Remove unreachable functions
    val reachable = g.get(MainNode).diSuccessors
    val reachableFuncs = prog.fs.filter(func => reachable.contains(g.get(func.v.v.UID)))
    Program(reachableFuncs, prog.body)(prog.pos)
  }

  private def callsFrom(funcBody: Stmt): List[Int] = funcBody match {
    // Recursive cases
    case Semi(s1, s2)   => callsFrom(s1) ++ callsFrom(s2)
    case If(_, s1, s2)  => callsFrom(s1) ++ callsFrom(s2)
    case While(_, body) => callsFrom(body)
    case Begin(body)    => callsFrom(body)
    case TryCatchFinally(tryBody, _, catchBody, finallyBody) =>
      callsFrom(tryBody) ++ callsFrom(catchBody) ++ callsFrom(finallyBody)
    // Base cases
    case Decl(_, Call(v, _)) => List(v.v.UID)
    case Asgn(_, Call(v, _)) => List(v.v.UID)
    case _                   => List()
  }
}
