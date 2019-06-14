package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.MonsterArchetype;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ArchetypeSetupStore<T> {

  private final Map<MonsterArchetype.Schema, MonsterArchetype.Setup<T>> indexed = new HashMap<>();

  private final AtomicReference<MonsterArchetype.Schema> current = new AtomicReference<>();

  public void setCurrent(final MonsterArchetype.Schema archetype) {
    current.set(archetype);
  }

  public void addToCurrent(final MonsterArchetype.Setup<T> generation) {
    indexed.put(current.getAndSet(null), generation);
  }

  public Optional<MonsterArchetype.Setup<T>> get(final MonsterArchetype.Schema archetype) {
    return Optional.ofNullable(indexed.get(archetype));
  }
}
