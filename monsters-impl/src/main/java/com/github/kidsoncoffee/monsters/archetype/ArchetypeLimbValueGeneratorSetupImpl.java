package com.github.kidsoncoffee.monsters.archetype;

import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;

/**
 * The default implementation of {@link
 * com.github.kidsoncoffee.monsters.MonsterArchetype.LimbValueGeneratorSetup}.
 *
 * @author fernando.chovich
 * @since 1.0
 */
public class ArchetypeLimbValueGeneratorSetupImpl<T>
    implements MonsterArchetype.LimbValueGeneratorSetup<
        T, ArchetypeLimbValueGeneratorSetupImpl<T>> {

  /** The <strong>Archetype Limb</strong> setups. */
  private final LimbsSetupsByArchetype<T> limbSetups;

  /**
   * Constructor.
   *
   * @param limbSetups The <strong>Archetype Limb</strong> setups.
   */
  public ArchetypeLimbValueGeneratorSetupImpl(final LimbsSetupsByArchetype<T> limbSetups) {
    this.limbSetups = limbSetups;
  }

  /**
   * Adds the given <strong>Monster Limb</strong> setup as the current setup.
   *
   * @param limbSetup The <strong>Monster Limb</strong> setup.
   * @return The value generator setup for chaining.
   */
  @Override
  public ArchetypeLimbValueGeneratorSetupImpl<T> setup(final MonsterLimb.Setup<T> limbSetup) {
    this.limbSetups.addToCurrent(limbSetup);
    return this;
  }
}
