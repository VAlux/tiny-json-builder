package dev.alvo.json.interpreter;

import dev.alvo.json.Json;

import java.util.Optional;

public interface Interpreter<T> {
  Optional<T> interpret(Json json);
}
