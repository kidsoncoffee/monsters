package com.github.kidsoncoffee.monsters;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monster {

  Class<? extends DefaultGenerator>[] defaultGenerators() default {};

  @FunctionalInterface
  interface LimbSetup<T> {
    void setup(final MonsterLimb.Generation generation, final T monster);
  }

  // TODO create MonsterArchetype.*
  @FunctionalInterface
  interface ArchetypeSetup<T> {
    void setup(final ArchetypeBinding<T> archetype);
  }

  interface Setup<T> extends LimbSetup<T>, ArchetypeSetup<T> {}

  interface DefaultGenerator {
    Optional<Object> generate(final MonsterLimb.Schema limb);
  }

  interface ArchetypeBindingBuilder<T> {
    ArchetypeBindingBuilder setup(final LimbSetup<T> generation);
  }

  @Value.Immutable
  interface Archetype {
    Archetype DEFAULT =
        ImmutableArchetype.builder()
            .description("Default Archetype")
            .extendsFrom(Optional.empty())
            .build();

    String getDescription();

    @Value.Default
    default Optional<Archetype> extendsFrom() {
      return Optional.of(DEFAULT);
    }
  }

  // TODO fchovich does this need to be here?
  interface ArchetypeBinding<T> {
    ArchetypeBindingBuilder<T> when(final Archetype archetype);
  }

  class Default implements Archetype {

    private Default() {}

    @Override
    public String getDescription() {
      return "Default monster";
    }
  }
}
