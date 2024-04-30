package dev.alvo.json.interpreter;

import dev.alvo.json.Json;
import dev.alvo.json.JsonValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.alvo.json.Json.*;

public class JsonToStringInterpreter implements Interpreter<String> {

  @Override
  public Optional<String> interpret(Json json) {
    var value = json.entries().entrySet().stream()
      .flatMap(entry -> interpret(entry).stream())
      .collect(Collectors.joining(","));

    return Optional.of("{" + value + "}");
  }

  private Optional<String> interpret(Map.Entry<String, ? extends JsonValue<?>> entry) {
    return interpret(entry.getValue()).map(value -> "\"%s\":%s".formatted(entry.getKey(), value));
  }

  private String interpret(List<? extends JsonValue<?>> values) {
    return values.stream()
      .flatMap(value -> interpret(value).stream())
      .collect(Collectors.joining(","));
  }

  private <T extends JsonValue<?>> Optional<String> interpret(T value) {
    if (value instanceof JsonString string) {
      return Optional.of("\"" + string.value() + "\"");
    }

    if (value instanceof JsonNumber number) {
      return Optional.of(number.value().toString());
    }

    if (value instanceof JsonBoolean bool) {
      return Optional.of(bool.value().toString());
    }

    if (value instanceof JsonArray array) {
      return Optional.of("[" + interpret(array.value()) + "]");
    }

    if (value instanceof JsonObject object) {
      return interpret(object.value());
    }

    if (value instanceof GuardedJsonValue<?> guardedValue) {
      return guardedValue.value().flatMap(__ -> interpret(guardedValue.jsonValue()));
    }

    throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
  }
}
