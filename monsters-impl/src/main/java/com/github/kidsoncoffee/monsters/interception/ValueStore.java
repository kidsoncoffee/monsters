package com.github.kidsoncoffee.monsters.interception;

import com.github.kidsoncoffee.monsters.ImmutableSchema;
import com.github.kidsoncoffee.monsters.MonsterLimb;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValueStore {
  private final Map<MonsterLimb.Schema, Object> values = new HashMap<>();

  public Optional<Object> get(final MonsterLimb.Schema limb) {
    return Optional.ofNullable(values.get(limb));
  }

  public void set(final MonsterLimb.Schema limb, final Object value) {
    this.values.put(ImmutableSchema.copyOf(limb), value);
  }
}
