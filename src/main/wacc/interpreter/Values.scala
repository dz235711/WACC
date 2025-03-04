package wacc

type BaseValue = Int | Boolean | Char | String
type Value = BaseValue | PairValue | ArrayValue

case class PairValue(fst: Value, snd: Value)
case class ArrayValue(es: List[Value])
