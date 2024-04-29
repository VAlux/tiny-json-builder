package dev.alvo.json.interpreter;

import dev.alvo.json.Json;
import dev.alvo.json.JsonValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.alvo.json.Json.*;

public class JsonToStringInterpreter {

  public String interpret(Json json) {
    var value = json.entries().entrySet().stream()
      .map(this::interpret)
      .collect(Collectors.joining(","));

    return "{" + value + "}";
  }

  private String interpret(Map.Entry<String, ? extends JsonValue<?>> entry) {
    return "\"%s\":%s".formatted(entry.getKey(), interpret(entry.getValue()));
  }

  private String interpret(List<? extends JsonValue<?>> values) {
    return values.stream().map(this::interpret).collect(Collectors.joining(","));
  }

  private <T extends JsonValue<?>> String interpret(T value) {
    if (value instanceof JsonString string) {
      return "\"" + string.value() + "\"";
    }

    if (value instanceof JsonNumber number) {
      return number.value().toString();
    }

    if (value instanceof JsonBoolean bool) {
      return bool.value().toString();
    }

    if (value instanceof JsonArray<?> array) {
      return "[" + interpret(array.value()) + "]";
    }

    if (value instanceof JsonObject object) {
      return interpret(object.value());
    }

    throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
  }
}
