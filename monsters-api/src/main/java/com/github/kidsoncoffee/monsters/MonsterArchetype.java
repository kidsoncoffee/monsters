package com.github.kidsoncoffee.monsters;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * The namespace for all <strong>Monster Archetype</strong> interfaces and domain.
 *
 * @author fernando.chovich
 * @since 1.0
 */
public interface MonsterArchetype {

  /** The default archetype which does not extend any other archetype. */
  MonsterArchetype.Schema DEFAULT_ARCHETYPE =
      ImmutableMonsterArchetypeSchema.builder()
          .description("Default Archetype")
          .extendsFrom(Optional.empty())
          .build();

  /** A monster archetype. */
  @Value.Immutable
  @Value.Style(typeImmutable = "ImmutableMonsterArchetype*")
  interface Schema {

    /**
     * Returns the description of the archetype.
     *
     * @return The description of the archetype.
     */
    String getDescription();

    /**
     * Returns the parent archetype. By default is the {@link MonsterArchetype#DEFAULT_ARCHETYPE}.
     *
     * @return The parent archetype.
     */
    @Value.Default
    default Optional<MonsterArchetype.Schema> extendsFrom() {
      return Optional.of(DEFAULT_ARCHETYPE);
    }
  }

  /**
   * The <strong>Monster Archetype</strong> limb value generation binding.
   *
   * @param <T>
   */
  @FunctionalInterface
  interface Setup<T> {

    /**
     * Provides a {@link ValueGeneratorBinding} used to setup the bindings of an <strong>Monster
     * Archetype</strong>.
     *
     * @param archetypeBinding The archetype binding.
     */
    void archetypeSetup(final ValueGeneratorBinding<T> archetypeBinding);
  }

  /**
   * The value generator binding interface.
   *
   * @param <T> The type of the <sstrong>Monster</sstrong>.
   */
  interface ValueGeneratorBinding<T> {

    /**
     * Binds the returning {@link LimbValueGeneratorSetup} to the given <strong>Archetype</strong>.
     *
     * @param archetype The archetype to bind the value generation against.
     * @return The value generation setup for the given archetype.
     */
    LimbValueGeneratorSetup<T, ?> when(final MonsterArchetype.Schema archetype);
  }

  /**
   * Sets up how values will be generated for <strong>Archetype</strong>.
   *
   * @param <T> The type of the <strong>Monster</strong>.
   */
  interface LimbValueGeneratorSetup<T, SELF extends LimbValueGeneratorSetup<T, SELF>> {

    /**
     * Sets up limb value generation for a <strong>Archetype</strong>.
     *
     * @param limbSetup The <strong>Monster Limb</strong>setup.
     * @return The setup instance for chaining.
     */
    SELF setup(final MonsterLimb.Setup<T> limbSetup);
  }
}
