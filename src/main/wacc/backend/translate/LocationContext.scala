package wacc

import wacc.TypedAST._

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

  /** Get the location associated with an identifier
   *
   * @param v The identifier to get the location of
   * @return The location associated with the identifier
   */
  def getLocation(v: Ident): Location = ???

  /** Move the getNext pointer to the last location */
  def unreserveLast(): Unit = ???

  /** Set up stack frame, assign parameters a location and push callee-saved registers onto the stack.
   * Run this at the start of a function.
   * 
   * @param params The parameters of the function
  */
  def setUpFunc(params: List[Ident]): Unit = ???

  /** Reset stack pointer and pop callee-saved registers from the stack, and set up the return value.
   * Run this at the end of a function just before returning.
   * 
   * @param retVal The location of the return value
   */
  def cleanUpFunc(retVal: Location): Unit = ???

  /** Saves caller registers and moves arguments to their intended registers/on the stack.
   * Run this just before calling a function.
   *
   * @param argLocations The temporary locations of the arguments
   */
  def setUpCall(argLocations: List[Location]): Unit = ???

  /** Restore caller registers and save result to a location
   * Run this just after calling a function.
   *
   * @return The location of the result
   */
  def cleanUpCall(): Location = ???

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
   * @param loc2 The second location/operand
   * @param op The operation to perform on the two locations, where the first location is guaranteed to be a register
   */
  def regInstr[Op2](loc1: Location, loc2: Op2, op: (Register, Op2) => Instruction): Unit = ???

  /** Perform some operation that forces the use of n registers. 
   * 
   * @param locs The locations to use
   * @param op The operation to perform on the locations as registers
  */
  def regInstrN(locs: List[Location], op: List[Register] => Instruction): Unit = ???

  /** Perform some operation that forces the use of some register(s). These registers are saved and restored after the operation.
   *
   * @param regsToUse The registers modified by the operation
   * @param op The operation to perform
   */
  def withFreeRegisters(regsToUse: List[Register], op: => Unit): Unit = ???
}
