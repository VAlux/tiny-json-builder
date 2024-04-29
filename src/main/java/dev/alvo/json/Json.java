package dev.alvo.json;

import java.util.List;
import java.util.Map;

public record Json(Map<String, ? extends JsonValue<?>> entries) {
  //@formatter:off
  public record JsonString(String value) implements JsonValue<String> {}
  public record JsonNumber(Number value) implements JsonValue<Number> {}
  public record JsonBoolean(Boolean value) implements JsonValue<Boolean> {}
  public record JsonObject(Json value) implements JsonValue<Json> {}
  public record JsonArray<T>(List<JsonValue<T>> value) implements JsonValue<List<JsonValue<T>>> {}
  //@formatter:on
}

