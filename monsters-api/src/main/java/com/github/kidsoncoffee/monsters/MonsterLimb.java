package com.github.kidsoncoffee.monsters;

import org.immutables.value.Value;

import java.util.List;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public interface MonsterLimb {

  @Value.Immutable
  @Value.Style(typeImmutable = "ImmutableMonsterLimb*")
  interface Schema {
    String getName();

    Class getType();
  }

  @FunctionalInterface
  interface Setup<T> {
    void setup(final Binding binding, final T monster);
  }

  class Binding {
    private ValueGenerationSetup setup;

    public Binding(final ValueGenerationSetup setup) {
      this.setup = setup;
    }

    public <T> ValueGenerationSetup<T> on(final T nothingtolookhere) {
      return this.setup;
    }
  }

  @FunctionalInterface
  interface ValueGenerator<T> {
    T generate();

  }
  interface ValueGenerationSetup<T> {

    void generate(final ValueGenerator<T> generator);

    void fix(final T value);
    void pickFrom(final List<T> choices);

  }
  // TODO does this need to be here?
}
