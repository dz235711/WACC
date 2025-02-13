package wacc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class BackendValidFunctionSimple_functionsTests extends AnyFlatSpec {
  val dir = "src/test/examples/valid/function/simple_functions/"

  it should "execute argScopeCanBeShadowed.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + argScopeCanBeShadowed.wacc, "") shouldBe Some("true\n")
  }*/

  it should "execute asciiTable.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + asciiTable.wacc, "") shouldBe Some("Ascii character lookup table:\n-------------\n|   32 =    |\n|   33 = !  |\n|   34 = "  |\n|   35 = #  |\n|   36 = $  |\n|   37 = %  |\n|   38 = &  |\n|   39 = '  |\n|   40 = (  |\n|   41 = )  |\n|   42 = *  |\n|   43 = +  |\n|   44 = ,  |\n|   45 = -  |\n|   46 = .  |\n|   47 = /  |\n|   48 = 0  |\n|   49 = 1  |\n|   50 = 2  |\n|   51 = 3  |\n|   52 = 4  |\n|   53 = 5  |\n|   54 = 6  |\n|   55 = 7  |\n|   56 = 8  |\n|   57 = 9  |\n|   58 = :  |\n|   59 = ;  |\n|   60 = <  |\n|   61 = =  |\n|   62 = >  |\n|   63 = ?  |\n|   64 = @  |\n|   65 = A  |\n|   66 = B  |\n|   67 = C  |\n|   68 = D  |\n|   69 = E  |\n|   70 = F  |\n|   71 = G  |\n|   72 = H  |\n|   73 = I  |\n|   74 = J  |\n|   75 = K  |\n|   76 = L  |\n|   77 = M  |\n|   78 = N  |\n|   79 = O  |\n|   80 = P  |\n|   81 = Q  |\n|   82 = R  |\n|   83 = S  |\n|   84 = T  |\n|   85 = U  |\n|   86 = V  |\n|   87 = W  |\n|   88 = X  |\n|   89 = Y  |\n|   90 = Z  |\n|   91 = [  |\n|   92 = \  |\n|   93 = ]  |\n|   94 = ^  |\n|   95 = _  |\n|   96 = `  |\n|   97 = a  |\n|   98 = b  |\n|   99 = c  |\n|  100 = d  |\n|  101 = e  |\n|  102 = f  |\n|  103 = g  |\n|  104 = h  |\n|  105 = i  |\n|  106 = j  |\n|  107 = k  |\n|  108 = l  |\n|  109 = m  |\n|  110 = n  |\n|  111 = o  |\n|  112 = p  |\n|  113 = q  |\n|  114 = r  |\n|  115 = s  |\n|  116 = t  |\n|  117 = u  |\n|  118 = v  |\n|  119 = w  |\n|  120 = x  |\n|  121 = y  |\n|  122 = z  |\n|  123 = {  |\n|  124 = |  |\n|  125 = }  |\n|  126 = ~  |\n-------------\n")
  }*/

  it should "execute functionDeclaration.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionDeclaration.wacc, "") shouldBe Some("")
  }*/

  it should "execute functionDoubleReturn.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionDoubleReturn.wacc, "") shouldBe Some("3\n")
  }*/

  it should "execute functionIfReturns.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionIfReturns.wacc, "") shouldBe Some("go\n1\n")
  }*/

  it should "execute functionManyArguments.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionManyArguments.wacc, "") shouldBe Some("a is 42\nb is true\nc is u\nd is hello\ne is #addrs#\nf is #addrs#\nanswer is g\n")
  }*/

  it should "execute functionMultiReturns.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionMultiReturns.wacc, "") shouldBe Some("1\n")
  }*/

  it should "execute functionReturnPair.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionReturnPair.wacc, "") shouldBe Some("10\n")
  }*/

  it should "execute functionSimple.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionSimple.wacc, "") shouldBe Some("0\n")
  }*/

  it should "execute functionSimpleLoop.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionSimpleLoop.wacc, "") shouldBe Some("10\n")
  }*/

  it should "execute functionUpdateParameter.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + functionUpdateParameter.wacc, "") shouldBe Some("y is 1\nx is 1\nx is now 5\ny is still 1\n")
  }*/

  it should "execute incFunction.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + incFunction.wacc, "") shouldBe Some("1\n4\n")
  }*/

  it should "execute lotsOfLocals.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + lotsOfLocals.wacc, "") shouldBe Some("5\n8\n")
  }*/

  it should "execute manyArgumentsChar.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + manyArgumentsChar.wacc, "") shouldBe Some("A\nb\n")
  }*/

  it should "execute manyArgumentsInt.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + manyArgumentsInt.wacc, "") shouldBe Some("23\n")
  }*/

  it should "execute negFunction.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + negFunction.wacc, "") shouldBe Some("true\nfalse\ntrue\n")
  }*/

  it should "execute punning.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + punning.wacc, "") shouldBe Some("0\n")
  }*/

  it should "execute sameArgName.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + sameArgName.wacc, "") shouldBe Some("99\n")
  }*/

  it should "execute sameArgName2.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + sameArgName2.wacc, "") shouldBe Some("99\n")
  }*/

  it should "execute sameNameAsVar.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + sameNameAsVar.wacc, "") shouldBe Some("5\n")
  }*/

  it should "execute usesArgumentWhilstMakingArgument.wacc" taggedAs (Backend) in pending /*{
    fullExec(dir + usesArgumentWhilstMakingArgument.wacc, "") shouldBe Some("12\n-4\n32\n")
  }*/

}
