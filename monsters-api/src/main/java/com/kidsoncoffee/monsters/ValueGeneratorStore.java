package com.kidsoncoffee.monsters;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorStore {
  private final Table<
          Class,
          Class<? extends Monster.Archetype>,
          Map<MonsterMember.Schema, MonsterMember.ValueGenerator>>
      store = HashBasedTable.create();

  public void create(
      final Class monsterClass,
      final Class<? extends Monster.Archetype> archetypeClass,
      final MonsterMember.Schema memberSchema,
      final MonsterMember.ValueGenerator valueGenerator) {
    if (!this.store.contains(memberSchema, archetypeClass)) {
      this.store.put(monsterClass, archetypeClass, new HashMap<>());
    }
    this.store.get(monsterClass, archetypeClass).put(memberSchema, valueGenerator);
  }

  public void retrieve() {}

  public boolean existsForMonster(final Class monsterClass) {
    return this.store.containsRow(monsterClass);
  }
}
