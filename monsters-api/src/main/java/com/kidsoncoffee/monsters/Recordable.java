package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.UnderlyingInterceptor;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Recordable implements UnderlyingInterceptor {

  private final Class monsterTargetClass;
  private final ValueGeneratorStore store;

  public Recordable(final Class monsterTargetClass, final ValueGeneratorStore store) {
    this.monsterTargetClass = monsterTargetClass;
    this.store = store;
  }

  public void intercept(
      final Class<Monster.Archetype> archetypeClass,
      final MonsterMember.Schema schema,
      final MonsterMember.ValueGenerator valueGenerator) {
    this.store.create(this.monsterTargetClass, archetypeClass, schema, valueGenerator);
  }
}
