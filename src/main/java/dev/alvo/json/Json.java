package dev.alvo.json;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record Json(Map<String, ? extends JsonValue<?>> entries) {
  //@formatter:off
  public record JsonString(String value) implements JsonValue<String> {}
  public record JsonNumber(Number value) implements JsonValue<Number> {}
  public record JsonBoolean(Boolean value) implements JsonValue<Boolean> {}
  public record JsonObject(Json value) implements JsonValue<Json> {}
  public record JsonArray(List<JsonValue<?>> value) implements JsonValue<List<JsonValue<?>>> {}
  //@formatter:on

  public record GuardedJsonValue<V, T extends JsonValue<V>>(T jsonValue, Predicate<V> predicate)
    implements JsonValue<Optional<V>> {

    @Override
    public Optional<V> value() {
      final V value = this.jsonValue.value();
      if (this.predicate.test(value)) {
        return Optional.of(value);
      }

      return Optional.empty();
    }

    public <R> R map(Function<? super V, ? extends R> mapper) {
      return mapper.apply(this.jsonValue.value());
    }

    public <R> Optional<R> flatMap(Function<? super T, ? extends Optional<R>> mapper) {
      if (this.predicate.test(this.jsonValue.value())) {
        return mapper.apply(this.jsonValue);
      }

      return Optional.empty();
    }
  }
}

