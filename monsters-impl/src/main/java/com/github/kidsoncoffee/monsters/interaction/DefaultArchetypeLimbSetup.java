package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.Monster;

public class DefaultArchetypeLimbSetup<T> implements Monster.ArchetypeBinding<T> {

  private final LimbSetupStore store;

  private final Monster.ArchetypeBindingBuilder<T> builder;

  public DefaultArchetypeLimbSetup(
      final LimbSetupStore store, final Monster.ArchetypeBindingBuilder<T> builder) {
    this.store = store;
    this.builder = builder;
  }

  @Override
  public Monster.ArchetypeBindingBuilder<T> when(Monster.Archetype archetype) {
    this.store.setCurrent(archetype);
    return this.builder;
  }
}
