package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.archetype.ArchetypeValueGeneratorBindingImpl;
import com.github.kidsoncoffee.monsters.archetype.LimbsSetupsByArchetype;

import java.util.function.Supplier;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorBindingFactory
    implements Supplier<MonsterArchetype.ValueGeneratorBinding> {

  private final LimbsSetupsByArchetype limbsSetupsByArchetype;

  private final MonsterArchetype.LimbValueGeneratorSetup limbValueGeneratorSetup;

  public ValueGeneratorBindingFactory(
      LimbsSetupsByArchetype limbsSetupsByArchetype,
      MonsterArchetype.LimbValueGeneratorSetup limbValueGeneratorSetup) {
    this.limbsSetupsByArchetype = limbsSetupsByArchetype;
    this.limbValueGeneratorSetup = limbValueGeneratorSetup;
  }

  @Override
  public MonsterArchetype.ValueGeneratorBinding get() {
    return new ArchetypeValueGeneratorBindingImpl(
        this.limbsSetupsByArchetype, this.limbValueGeneratorSetup);
  }
}
