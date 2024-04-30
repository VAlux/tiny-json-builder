# tiny-json-builder

Tiny JSON building DSL written in Java

## Usage example

1. construct the json structure using the dsl combinators:

```java
import dev.alvo.json.Json;

import static dev.alvo.json.JsonBuildingDSL.*;

byte byteValue = 1;
short shortValue = 15;
int integerValue = 5;
long longValue = 10L;
float floatValue = 5.5f;
double doubleValue = 5.5d;
char charValue = 'a';
boolean booleanValue = true;
String stringValue = "hello";

Json json =
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
```

2. interpret the structure with one of the interpreters:

```java
String result = new JsonToStringInterpreter().interpret(json);
```

3. the following content will be provided as a result:

```json
{
  "bool": true,
  "string": "hello",
  "array": [
    "one",
    "two",
    "three"
  ],
  "byte": 1,
  "double": 5.5,
  "char": "a",
  "short": 15,
  "integer": 5,
  "float": 5.5,
  "long": 10,
  "child": {
    "boolean": false
  }
}
```
## Features

### `forEach` combinator:
```java

var json =
    json(
        field("numbers",
            array(
                forEach(IntStream.iterate(0, i -> i + 1).limit(10).boxed().toList(), JsonBuildingDSL::number))));
```
will result into:
```json
{
  "numbers": [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
}
```

### `guard` combinator

```java

var json = 
    json(
        field("regular", string("displayed")),
        field("guarded", guard(string("hidden"), str -> str.equalsIgnoreCase("GUARDED"))),
        field("more regular", string("displayed")
    )
);
```
will result into (guarded predicate removes field from the resulting json):
```json
{
  "more regular": "displayed",
  "regular": "displayed"
}
```