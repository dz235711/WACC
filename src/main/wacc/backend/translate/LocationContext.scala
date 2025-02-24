package wacc

import wacc.TypedAST.{Call as TypedCall, *}

type Location = Register | Pointer

class LocationContext {
  def getNext: Location = ???
  def addLocation(v: Ident, r: Location): Unit = ???
  def moveToLocation(resultLoc: Location, l: LValue): Unit = ???
  def saveCallerRegisters(): Unit = ???
  def restoreCallerRegisters(): Unit = ???
  def restoreCalleeRegisters(): Unit = ???
  def setNextReg(l: Location): Register = ???
}
