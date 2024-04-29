package dev.alvo.json.interpreter;

import dev.alvo.json.JsonBuildingDSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static dev.alvo.json.Json.JsonObject;
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
    String stringValue = "hello";

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
        field("string", string(stringValue)),
        field("child",
          object("boolean", bool(false))));

    var actual = new JsonToStringInterpreter().interpret(json);
    var expected = """
      {"bool":true,"string":"hello","array":["one","two","three"],"byte":1,"double":5.5,"char":"a","short":15,"integer":5,"float":5.5,"long":10,"child":{"boolean":false}}""";

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testForEachGeneratesStringJsonValuesCorrectly() {
    var json =
      json(
        field("strings",
          array(
            forEach(List.of("one", "two", "three"), JsonBuildingDSL::string))));

    var actual = new JsonToStringInterpreter().interpret(json);
    var expected = """
      {"strings":["one","two","three"]}""";

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testForEachGeneratesIntegerJsonValuesCorrectly() {
    var json =
      json(
        field("numbers",
          array(
            forEach(IntStream.iterate(0, i -> i + 1).limit(10).boxed().toList(), JsonBuildingDSL::number))));

    var actual = new JsonToStringInterpreter().interpret(json);
    var expected = """
      {"numbers":[0,1,2,3,4,5,6,7,8,9]}""";

    Assertions.assertEquals(expected, actual);
  }

  private JsonObject generateRecursive(JsonObject current, int level, int limit) {
    if (level > limit) {
      return current;
    }

    return generateRecursive(object(String.valueOf(level), current), level + 1, limit);
  }

  @Test
  void testDeepNestedObjectGeneratedCorrectly() {
    var json = json(field("start", generateRecursive(object("finish", bool(true)), 0, 10)));
    var actual = new JsonToStringInterpreter().interpret(json);
    var expected = """
      {"start":{"10":{"9":{"8":{"7":{"6":{"5":{"4":{"3":{"2":{"1":{"0":{"finish":true}}}}}}}}}}}}}""";

    Assertions.assertEquals(expected, actual);
  }
}
