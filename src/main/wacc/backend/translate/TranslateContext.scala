package wacc

import scala.collection.mutable.ListBuffer

class TranslateContext(instrs: ListBuffer[Instruction] = new ListBuffer) {

  /** Adds an instruction to the instruction context
   *
   * @param instr The instruction to add
   */
  def addInstr(instr: Instruction): TranslateContext = {
    instrs += instr
    this
  }

  /** Returns the list of instructions in the instruction context
   */
  def get: List[Instruction] = instrs.toList

}
