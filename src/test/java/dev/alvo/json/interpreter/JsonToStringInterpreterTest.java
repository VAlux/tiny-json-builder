package dev.alvo.json.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static dev.alvo.json.JsonBuildingDSL.*;

class JsonToStringInterpreterTest {

  @Test
  void testInterpretationOfComplexObject() {
    byte byteValue = 1;
    short shortValue = 15;
    int integerValue = 5;
    long longValue = 10L;
    float floatValue = 5.5f;
    double doubleValue = 5.5d;
    char charValue = 'a';
    boolean booleanValue = true;

    var json =
      json(
        field("array",
          array(
            string("one"),
            string("two"),
            string("three"))),
        field("byte", number(byteValue)),
        field("short", number(shortValue)),
        field("integer", number(integerValue)),
        field("long", number(longValue)),
        field("float", number(floatValue)),
        field("double", number(doubleValue)),
        field("char", string(charValue)),
        field("bool", bool(booleanValue)),
        field("child",
          object("boolean", bool(false))));

    var actual = new JsonToStringInterpreter().interpret(json);
    var expected = """
      {"bool":true,"array":["one","two","three"],"byte":1,"double":5.5,"char":"a","short":15,"integer":5,"float":5.5,"long":10,"child":{"boolean":false}}""";

    Assertions.assertEquals(expected, actual);
  }
}
