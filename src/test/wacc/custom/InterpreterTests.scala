package wacc

import TypedAST.*
import wacc.RenamedAST.KnownType._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import scala.collection.mutable.Map as MMap
import scala.collection.mutable.ListBuffer

class InterpreterTests extends AnyFlatSpec {
  behavior of "SKIP"

  "skip" should "not do anything" taggedAs Repl in {
    Interpreter.interpret(Program(List(), Skip)) shouldBe (MapContext(), MapContext())
  }

  behavior of "DECLARATION"

  "bool declarations" should "be in scope" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, BoolType),
          BoolLiter(true)
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> true
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  "int declarations" should "be in scope" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, IntType),
          IntLiter(0)
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> 0
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  "char declarations" should "be in scope" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, CharType),
          CharLiter('a')
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> 'a'
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  "string declarations" should "be in scope" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, StringType),
          StringLiter("Hello, World!")
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> "Hello, World!"
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  it should "be able to use char arrays" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, StringType),
          ArrayLiter(List(CharLiter('a'), CharLiter('b'), CharLiter('c')), ArrayType(CharType))
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> ArrayValue(ListBuffer('a', 'b', 'c'))
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  "array declarations" should "be in scope" taggedAs Repl in {
    val prog =
      Program(
        List(),
        Decl(
          Ident(0, ArrayType(BoolType)),
          ArrayLiter(List(BoolLiter(true)), ArrayType(BoolType))
        )
      )

    val resultScope = MapContext(
      MMap(
        0 -> ArrayValue(ListBuffer(true))
      )
    )

    Interpreter.interpret(prog)._1 shouldBe resultScope
  }

  behavior of "ASSIGNMENT"

  "variables" should "be able to be reassigned" taggedAs Repl in {
    val varId = 0

    val initScope: VariableScope = MapContext(
      MMap(
        varId -> 0
      )
    )

    val prog2 =
      Program(
        List(),
        Decl(
          Ident(varId, CharType),
          CharLiter('a')
        )
      )

    val resultScope = MapContext(
      MMap(
        varId -> 'a'
      )
    )

    Interpreter.interpret(prog2, Some(initScope))._1 shouldBe resultScope
  }

  "array elements" should "be able to be reassigned" taggedAs Repl in {
    val arrayIdent = Ident(0, ArrayType(IntType))

    val initScope: VariableScope = MapContext(
      MMap(
        arrayIdent.id -> ArrayValue(ListBuffer(0))
      )
    )

    val prog2 =
      Program(
        List(),
        Asgn(
          ArrayElem(arrayIdent, List(IntLiter(0)), IntType),
          IntLiter(123)
        )
      )

    val resultScope = MapContext(
      MMap(
        arrayIdent.id -> ArrayValue(ListBuffer(123))
      )
    )

    Interpreter.interpret(prog2, Some(initScope))._1 shouldBe resultScope
  }

  "nested array elements" should "be able to be reassigned" taggedAs Repl in {
    val arrayIdent = Ident(0, ArrayType(ArrayType(ArrayType(IntType))))

    val initScope: VariableScope =
      MapContext(MMap(arrayIdent.id -> ArrayValue(ListBuffer(ArrayValue(ListBuffer(ArrayValue(ListBuffer(0))))))))

    val prog =
      Program(
        List(),
        Asgn(
          ArrayElem(
            arrayIdent,
            List(IntLiter(0), IntLiter(0), IntLiter(0)),
            IntType
          ),
          IntLiter(123)
        )
      )

    val resultScope =
      MapContext(MMap(arrayIdent.id -> ArrayValue(ListBuffer(ArrayValue(ListBuffer(ArrayValue(ListBuffer(123))))))))

    Interpreter.interpret(prog, Some(initScope))._1 shouldBe resultScope
  }

  "arrays passed by reference" should "carry over reassignments" taggedAs Repl in {
    val arrIdent0 = Ident(0, ArrayType(CharType))
    val arrIdent1 = Ident(1, ArrayType(ArrayType(CharType)))

    val initScope: VariableScope = MapContext(
      MMap(
        arrIdent0.id -> ArrayValue(ListBuffer('a', 'b'))
      )
    )

    val prog1 = Program(
      List(),
      Decl(arrIdent1, ArrayLiter(List(arrIdent0), ArrayType(ArrayType(CharType))))
    )

    val scope1 = Interpreter.interpret(prog1, Some(initScope))._1

    val prog2 = Program(
      List(),
      Asgn(
        ArrayElem(arrIdent0, List(IntLiter(0)), CharType),
        CharLiter('7')
      )
    )

    val resultScope: VariableScope = MapContext(
      MMap(
        arrIdent0.id -> ArrayValue(ListBuffer('7', 'b')),
        arrIdent1.id -> ArrayValue(ListBuffer(ArrayValue(ListBuffer('7', 'b'))))
      )
    )

    Interpreter.interpret(prog2, Some(scope1))._1 shouldBe resultScope
  }
}
