package wacc

import wacc.TypedAST.{Call as TypedCall, *}

trait Location

class LocationContext {
  def getNext: Location = ???
  def addLocation(v: Ident, r: Location): Unit = ???
  def moveToLocation(resultLoc: Location, l: LValue): Unit = ???
}
