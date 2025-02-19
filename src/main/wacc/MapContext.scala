package wacc

import scala.collection.mutable.Map as MMap
import scala.collection.immutable.Map as IMap

class MapContext[K, V](xs: MMap[K, V] = MMap.empty[K, V]) {

  /** Adds an object to the context list
    *
    * @param k The key of the object to add
    * @param v The value of the object to add
    */
  def add(k: K, v: V): MapContext[K, V] = {
    xs += k -> v
    this
  }

  /** Returns the map of objects in the context
    */
  def get: IMap[K, V] = xs.toMap

}
