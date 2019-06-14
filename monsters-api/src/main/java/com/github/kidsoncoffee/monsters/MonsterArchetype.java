package com.github.kidsoncoffee.monsters;

import org.immutables.value.Value;

import java.util.Optional;

public interface MonsterArchetype {
  MonsterArchetype.Schema DEFAULT =
      ImmutableMonsterArchetypeSchema.builder()
          .description("Default Archetype")
          .extendsFrom(Optional.empty())
          .build();

  @Value.Immutable
  @Value.Style(typeImmutable = "ImmutableMonsterArchetype*")
  interface Schema {
    String getDescription();

    @Value.Default
    default Optional<MonsterArchetype.Schema> extendsFrom() {
      return Optional.of(DEFAULT);
    }
  }

  @FunctionalInterface
  interface Setup<T> {
    void setup(final Binding<T> archetype);
  }

  interface Binding<T> {
    LimbSetup<T> when(final MonsterArchetype.Schema archetype);
  }

  interface LimbSetup<T> {
    LimbSetup setup(final MonsterLimb.Setup<T> generation);
  }
}