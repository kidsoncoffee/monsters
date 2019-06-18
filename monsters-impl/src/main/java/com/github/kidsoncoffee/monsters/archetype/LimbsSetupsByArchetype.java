package com.github.kidsoncoffee.monsters.archetype;

import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class LimbsSetupsByArchetype<T> {

  private final Map<MonsterArchetype.Schema, MonsterLimb.Setup<T>> indexed = new HashMap<>();

  private final AtomicReference<MonsterArchetype.Schema> current = new AtomicReference<>();

  public void setCurrent(final MonsterArchetype.Schema archetype) {
    current.set(archetype);
  }

  public void addToCurrent(final MonsterLimb.Setup<T> generation) {
    indexed.put(current.getAndSet(null), generation);
  }

  public Optional<MonsterLimb.Setup<T>> get(final MonsterArchetype.Schema archetype) {
    return Optional.ofNullable(indexed.get(archetype));
  }
}
