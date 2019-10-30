package com.github.kidsoncoffee.monsters;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public interface Monster {
  interface Setup<T> {
    void setup(final ArchetypeSetup<T> archetypeSetup);
  }

  Archetype DEFAULT = () -> "Default archetype";

  interface Archetype {
    String archetypeName();
  }

  interface ArchetypeSetup<T> {
    default void when(final ValueGeneration<T> valueGeneration) {
      this.when(DEFAULT, valueGeneration);
    }

    default void when(final Archetype archetype, final ValueGeneration valueGeneration) {
      this.when(archetype, DEFAULT, valueGeneration);
    }

    void when(
        final Archetype archetype,
        final Archetype superArchetype,
        final ValueGeneration<T> valueGeneration);
  }

  @FunctionalInterface
  interface ValueGeneration<T> {
    void generate(final ValueBinding valueBinding, T dataObject);
  }

  @FunctionalInterface
  interface ValueBinding {
    <X> ValueGenerationSetup on(X limb);
  }

  interface ValueGenerationSetup {}

  @interface ByConvention {
    @interface Archetype {
      String value();
    }

    @ByConvention.Archetype("Default")
    @interface DefaultArchetype {}
  }

  @interface Options {}
}
