package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;

public class DefaultArchetypeBindingBuilder<T> implements MonsterArchetype.LimbSetup<T> {
  private final LimbSetupStore<T> setupStore;

  public DefaultArchetypeBindingBuilder(final LimbSetupStore<T> setupStore) {
    this.setupStore = setupStore;
  }

  @Override
  public MonsterArchetype.LimbSetup setup(final MonsterLimb.DefaultSetup<T> generation) {
    this.setupStore.addToCurrent(generation);
    return this;
  }
}
