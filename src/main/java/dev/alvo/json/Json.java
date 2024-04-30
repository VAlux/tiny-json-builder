package dev.alvo.json;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public record Json(Map<String, ? extends JsonValue<?>> entries) {
  //@formatter:off
  public record JsonString(String value) implements JsonValue<String> {}
  public record JsonNumber(Number value) implements JsonValue<Number> {}
  public record JsonBoolean(Boolean value) implements JsonValue<Boolean> {}
  public record JsonObject(Json value) implements JsonValue<Json> {}
  public record JsonArray(List<JsonValue<?>> value) implements JsonValue<List<JsonValue<?>>> {}
  //@formatter:on

  public record GuardedJsonValue<T>(JsonValue<T> jsonValue, Predicate<T> predicate) implements JsonValue<Optional<T>> {
    @Override
    public Optional<T> value() {
      final T value = this.jsonValue.value();
      if (this.predicate.test(value)) {
        return Optional.of(value);
      }

      return Optional.empty();
    }
  }
}

