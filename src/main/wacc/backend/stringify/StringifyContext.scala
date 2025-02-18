package wacc

import scala.collection.mutable.ListBuffer

class StringifyContext(instrs: ListBuffer[String] = new ListBuffer) {
  val INDENTATION_SIZE = 2

  /** Adds an instruction to the output string
   *
   * @param instr The instruction string to add
   */
  def addInstr(instr: String): StringifyContext = {
    instrs += instr
    this
  }

  /** Returns the list of instruction strings in the context
   */
  def getList: List[String] = instrs.toList

  def get: String =
    instrs
      .map(instr => {
        if (instr.startsWith(".") || instr.endsWith(":")) instr
        else " " * INDENTATION_SIZE + instr
      })
      .mkString("\n")
}
