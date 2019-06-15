package com.github.kidsoncoffee.monsters;

import java.util.Optional;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public interface Monster {

  interface DefaultSetup<T> extends MonsterLimb.DefaultSetup<T>, MonsterArchetype.Setup<T> {}

  interface DefaultGenerator {
    Optional<Object> generate(final MonsterLimb.Schema limb);
  }
}
