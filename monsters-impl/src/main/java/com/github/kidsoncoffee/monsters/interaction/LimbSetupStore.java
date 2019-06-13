package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.Monster;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class LimbSetupStore<T> {

  private final Map<Monster.Archetype, Monster.LimbSetup<T>> indexed = new HashMap<>();

  private final AtomicReference<Monster.Archetype> current = new AtomicReference<>();

  public void setCurrent(final Monster.Archetype archetype) {
    current.set(archetype);
  }

  public void addToCurrent(final Monster.LimbSetup<T> generation) {
    indexed.put(current.getAndSet(null), generation);
  }

  public Optional<Monster.LimbSetup<T>> get(final Monster.Archetype archetype) {
    return Optional.ofNullable(indexed.get(archetype));
  }
}
