package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.Monster;

public class DefaultArchetypeBindingBuilder<T> implements Monster.ArchetypeBindingBuilder<T> {
  private final LimbSetupStore setupStore;

  public DefaultArchetypeBindingBuilder(LimbSetupStore setupStore) {
    this.setupStore = setupStore;
  }

  @Override
  public Monster.ArchetypeBindingBuilder setup(final Monster.LimbSetup generation) {
    this.setupStore.addToCurrent(generation);
    return this;
  }
}
