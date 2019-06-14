package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.MonsterArchetype;

public class DefaultArchetypeLimbSetup<T> implements MonsterArchetype.Binding<T> {

  private final LimbSetupStore store;

  private final MonsterArchetype.LimbSetup<T> builder;

  public DefaultArchetypeLimbSetup(
      final LimbSetupStore store, final MonsterArchetype.LimbSetup<T> builder) {
    this.store = store;
    this.builder = builder;
  }

  @Override
  public MonsterArchetype.LimbSetup<T> when(final MonsterArchetype.Schema archetype) {
    this.store.setCurrent(archetype);
    return this.builder;
  }
}
