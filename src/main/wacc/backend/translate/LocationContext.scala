package wacc

import wacc.TypedAST.{Call as TypedCall, *}

type Location = Register | Pointer

class LocationContext {

  /** Get the next location to use, without actually using it
   *
   * @param size The size of the location
   * @return The next location to use
   */
  def getNext(size: Size): Location = ???

  /** Get the location to use and reserve it
   *
   * @param size The size of the location
   * @return The reserved location
   */
  def reserveNext(size: Size): Location = ???

  /** Associate a location with an identifier
   *
   * @param v The identifier to associate with
   * @param r The location to associate
   */
  def addLocation(v: Ident, r: Location): Unit = ???

  /** Move the getNext pointer to the last location
   *
   * @return The unreserved location
   */
  def unreserveLast(): Unit = ???

  /** Push caller-saved registers to the stack */
  def saveCallerRegisters(): Unit = ???

  /** Pop caller-saved registers from the stack */
  def restoreCallerRegisters(): Unit = ???

  /** Pop callee-saved registers from the stack */
  def restoreCalleeRegisters(): Unit = ???

  /** Move a value from one location to another
   *
   * @param dest The destination location
   * @param src The source location
   */
  def movLocLoc(dest: Location, src: Location): Unit = ???

  /** Perform some operation that forces the use of a register.
   * This is useful for operations that require a register as an operand, but you only have 2 locations.
   *
   * @param loc1 The first location
   * @param loc2 The second location
   * @param op The operation to perform on the two locations, where the first location is guaranteed to be a register
   */
  def regInstr(loc1: Location, loc2: Location, op: (Register, Location) => Instruction): Unit = ???

  /** Perform some operation that forces the use of some register(s). These registers are saved and restored after the operation.
   *
   * @param regsToUse The registers modified by the operation
   * @param op The operation to perform
   */
  def withFreeRegisters(regsToUse: List[Register], op: => Unit): Unit = ???

  // TODO: delete this
  def setNextReg(l: Location): Register = ???
}
