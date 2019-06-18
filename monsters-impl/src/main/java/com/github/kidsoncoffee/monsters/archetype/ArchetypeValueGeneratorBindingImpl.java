package com.github.kidsoncoffee.monsters.archetype;

import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.archetype.LimbsSetupsByArchetype;

/**
 * The default implementation of {@link
 * com.github.kidsoncoffee.monsters.MonsterArchetype.ValueGeneratorBinding}.
 *
 * @param <T> The type of the <strong>Monster</strong>.
 */
public class ArchetypeValueGeneratorBindingImpl<T>
    implements MonsterArchetype.ValueGeneratorBinding<T> {

  private final LimbsSetupsByArchetype limbsSetupsByArchetype;

  private final MonsterArchetype.LimbValueGeneratorSetup<T, ?> builder;

  public ArchetypeValueGeneratorBindingImpl(
      final LimbsSetupsByArchetype limbsSetupsByArchetype,
      final MonsterArchetype.LimbValueGeneratorSetup<T, ?> builder) {
    this.limbsSetupsByArchetype = limbsSetupsByArchetype;
    this.builder = builder;
  }

  @Override
  public MonsterArchetype.LimbValueGeneratorSetup<T, ?> when(
      final MonsterArchetype.Schema archetype) {
    this.limbsSetupsByArchetype.setCurrent(archetype);
    return this.builder;
  }
}
