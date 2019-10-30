package com.github.kidsoncoffee.monsters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterBuilder<T> {

  private final MonsterArchetype.Setup<T> archetypeSetup;

  private final MonsterLimb.Setup<T> limbSetup;

  private final List<MonsterLimb.Schema> limbs;

  private final List<Class<? extends MonsterSetup.FallbackValueGenerator>> defaultGenerators;

  private final Map<MonsterLimb.Schema, Object> values = new HashMap<>();

  public MonsterBuilder(
      final MonsterArchetype.Setup<T> archetypeSetup,
      final MonsterLimb.Setup<T> limbSetup,
      final List<MonsterLimb.Schema> limbs,
      final List<Class<? extends MonsterSetup.FallbackValueGenerator>> defaultGenerators) {
    this.archetypeSetup = archetypeSetup;
    this.limbSetup = limbSetup;
    this.limbs = limbs;
    this.defaultGenerators = defaultGenerators;
  }

  public MonsterBuilder(
      final Class<? extends MonsterArchetype.Setup<T>> archetypeSetupClass,
      final Class<? extends MonsterLimb.Setup<T>> limpSetupClass,
      final List<MonsterLimb.Schema> limbs,
      final List<Class<? extends MonsterSetup.FallbackValueGenerator>> defaultGenerators) {
    this(
        instantiateArchetypeSetup(archetypeSetupClass),
        instantiateLimbSetup(limpSetupClass),
        limbs,
        defaultGenerators);
  }

  private static <X> MonsterArchetype.Setup<X> instantiateArchetypeSetup(
      final Class<? extends MonsterArchetype.Setup<X>> monsterSetup) {
    try {
      return monsterSetup.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(
          String.format("Unable to instantiante archetype setup '%s'.", monsterSetup));
    }
  }

  private static <X> MonsterLimb.Setup<X> instantiateLimbSetup(
      final Class<? extends MonsterLimb.Setup<X>> limbSetup) {
    try {
      return limbSetup.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(
          String.format("Unable to instantiate limb setup '%s'.", limbSetup));
    }
  }

  public void setValue(final MonsterLimb.Schema method, final Object value) {
    this.values.put(method, value);
  }

  // TODO fchovich CREATE AN ABSTRACTION FOR VISIBILITY MANAGEMENT
  MonsterLimb.Setup<T> getLimbSetup() {
    return this.limbSetup;
  }

  MonsterArchetype.Setup<T> getArchetypeSetup() {
    return this.archetypeSetup;
  }

  Map<MonsterLimb.Schema, Object> getValues() {
    return this.values;
  }

  public List<MonsterLimb.Schema> getLimbs() {
    return this.limbs;
  }

  public List<Class<? extends MonsterSetup.FallbackValueGenerator>> getDefaultGenerators() {
    return this.defaultGenerators;
  }

  public Optional<Object> getValue(final MonsterLimb.Schema limb) {
    return Optional.ofNullable(this.getValues().get(limb));
  }

  public T build(final MonsterArchetype.Schema archetype) {
    return new Spawner<T>().spawn(this, Optional.ofNullable(archetype));
  }

  public T build() {
    return this.build(null);
  }
}
