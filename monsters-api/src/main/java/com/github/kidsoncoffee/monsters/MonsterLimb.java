package com.github.kidsoncoffee.monsters;

import org.immutables.value.Value;

import java.util.List;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public @interface MonsterLimb {

  // MOVE THIS AND RENAME TO MEMBER
  // THIS IS NOT PUBLIC API
  @Value.Immutable
  interface Schema {
    String getName();

    Class getType();
  }

  @FunctionalInterface
  interface ValueGenerator<T> {
    T generate();
  }

  interface GenerationSetup<T> {
    void generate(final ValueGenerator<T> generator);

    void fix(final T value);

    void pickFrom(final List<T> choices);
  }

  // TODO does this need to be here?
  class Generation {
    private GenerationSetup setup;

    public Generation(final GenerationSetup setup) {
      this.setup = setup;
    }

    public <T> GenerationSetup<T> on(final T nothingtolookhere) {
      return this.setup;
    }
  }
}
