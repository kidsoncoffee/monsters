package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.ImmutableSchema;
import com.kidsoncoffee.monsters.MonsterMember;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValueStore {
  private final Map<MonsterMember.Schema, Object> values = new HashMap<>();

  public Optional<Object> get(final MonsterMember.Schema limb) {
    return Optional.ofNullable(values.get(limb));
  }

  public void set(final MonsterMember.Schema limb, final Object value) {
    this.values.put(ImmutableSchema.copyOf(limb), value);
  }
}
