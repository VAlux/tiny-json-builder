package dev.alvo.json;

import static dev.alvo.json.Json.*;

public sealed interface JsonValue<T>
  permits JsonArray, JsonBoolean, JsonNumber, JsonObject, JsonString {

  T value();
}
