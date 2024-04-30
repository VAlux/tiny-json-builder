package dev.alvo.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dev.alvo.json.Json.*;

public final class JsonBuildingDSL {

  //@formatter:off
  public record JsonField<T extends JsonValue<?>>(String key, T value) {}
  //@formatter:on

  public static <T extends JsonValue<?>> JsonField<T> field(String key, T value) {
    return new JsonField<>(key, value);
  }

  public static JsonArray array(JsonValue<?>... items) {
    return array(Arrays.asList(items));
  }

  public static JsonArray array(Collection<JsonValue<?>> items) {
    return new JsonArray(items.stream().toList());
  }

  public static JsonString string(String value) {
    return new JsonString(value);
  }

  public static JsonString string(char value) {
    return new JsonString(String.valueOf(value));
  }

  public static JsonNumber number(Number value) {
    return new JsonNumber(value);
  }

  public static JsonBoolean bool(Boolean value) {
    return new JsonBoolean(value);
  }

  public static <T extends JsonValue<?>> JsonObject object(String key, T value) {
    return new JsonObject(json(field(key, value)));
  }

  public static Json json(JsonField<?>... fields) {
    return new Json(Arrays.stream(fields).collect(Collectors.toMap(JsonField::key, JsonField::value)));
  }

  public static Json json(Collection<JsonField<?>> fields) {
    return new Json(fields.stream().collect(Collectors.toMap(JsonField::key, JsonField::value)));
  }

  public static <T extends JsonValue<?>, R> List<T> forEach(Collection<R> items, Function<R, T> supplier) {
    return items.stream().map(supplier).collect(Collectors.toList());
  }

  public static <V, T extends JsonValue<V>> GuardedJsonValue<V, T> guard(T value, Predicate<V> predicate) {
    return new GuardedJsonValue<>(value, predicate);
  }
}
