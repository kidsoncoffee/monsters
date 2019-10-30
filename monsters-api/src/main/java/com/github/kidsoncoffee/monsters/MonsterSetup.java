package com.github.kidsoncoffee.monsters;

import java.util.Optional;

/**
 * The namespace for all <strong>Monster</strong> interfaces and domain.
 *
 * @author fernando.chovich
 * @since 1.0
 */
public interface MonsterSetup<T> extends MonsterLimb.Setup<T>, MonsterArchetype.Setup<T> {

  /** The fallback value generator interface. */
  @FunctionalInterface
  interface FallbackValueGenerator {

    /**
     * Generates, optionally, a value for the given <strong>Limb</strong>.
     *
     * @param limb The <strong>Limb</strong> to generate a value for.
     * @return The optional value.
     */
    Optional<Object> generate(final MonsterLimb.Schema limb);
  }
}
