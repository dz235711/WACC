`int getNumerator(pair(int, int) frac)` gets the numerator of a fraction

`int getDenominator(pair(int, int) frac)` gets the denominator of a fraction

`bool setNumerator(pair(int, int) frac, int num)` sets the numerator of a fraction

`bool setDenominator(pair(int, int) frac, int den)` sets the denominator of a fraction

`pair(int, int) toFraction(int num, int den)` creates a fraction from a numerator and a denominator

`bool simplify(pair(int, int) frac)` simplifies a fraction

`int _roundedDiv(pair(int, int) frac, bool flag, int parity)` helper function to refactor out the logic for rounding up or down

`int ceil(pair(int, int) frac)` rounds fraction up to the nearest integer

`int floor(pair(int, int) frac)` rounds fraction down to the nearest integer

`bool fractionPrint(pair(int, int) frac)` prints a fraction

`bool fractionPrintAsFloat(pair(int, int) frac)` prints a fraction as a float

`pair(int, int) fractionCopy(pair(int, int) frac)` copies a fraction

`bool fractionInvert(pair(int, int) frac)` inverts the numerator and denominator of a fraction

`bool fractionNegate(pair(int, int) frac)` negates the fraction, e.g. 1/2 -> -1/2

`bool fractionIsNegative(pair(int, int) frac)` checks if a fraction is negative

`bool fractionIsPositive(pair(int, int) frac)` checks if a fraction is positive

`bool fractionIsZero(pair(int, int) frac)` checks if a fraction is zero

`pair(int, int) fractionMult(pair(int, int) frac1, pair(int, int) frac2)` multiplies two fractions

`pair(int, int) fractionDiv(pair(int, int) frac1, pair(int, int) frac2)` divides two fractions

`pair(int, int) fractionAdd(pair(int, int) frac1, pair(int, int) frac2)` adds two fractions

`pair(int, int) fractionSub(pair(int, int) frac1, pair(int, int) frac2)` subtracts two fractions

`pair(int, int) floatToFraction(int digits, int decimals)` rounds a float to a fraction

`pair(int, int) piIterative(int iteration)` generates π through the summing of an infinite sequence

`pair(int, int) pi()` constant estimate for pi to compensate for int size constraint limiting the sequence sum function

`pair(int, int) eIterative(int iteration)` generates e through the summing of an infinite sequence

`pair(int, int) e()` constant estimate for e to compensate for int size constraint limiting the sequence sum function

`pair(int, int) fractionPow(pair(int, int) frac, int n)` exponentiates a fraction

`pair(int, int) _sinApprox(int deg)` approximates 0-90 degrees of the sine function

`pair(int, int) sin(int deg)` approximates sine of a degree

`pair(int, int) cos(int deg)` approximates cosine of a degree

`pair(int, int) tan(int deg)` approximates tangent of a degree

`pair(int, int) root(pair(int, int) frac, int n, int iterations)` approximates the nth root of a fraction

`pair(int, int) intToFraction(int n)` converts an integer to a fraction
