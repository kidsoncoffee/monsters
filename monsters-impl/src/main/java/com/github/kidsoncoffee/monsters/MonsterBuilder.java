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

  private final Monster.Setup<T> setup;

  private final List<MonsterLimb.Schema> limbs;

  private final List<Class<? extends Monster.FallbackValueGenerator>> defaultGenerators;

  private final Map<MonsterLimb.Schema, Object> values = new HashMap<>();

  public MonsterBuilder(
      final Monster.Setup<T> setup,
      final List<MonsterLimb.Schema> limbs,
      final List<Class<? extends Monster.FallbackValueGenerator>> defaultGenerators) {
    this.setup = setup;
    this.limbs = limbs;
    this.defaultGenerators = defaultGenerators;
  }

  public MonsterBuilder(
      final Class<? extends Monster.Setup<T>> setup,
      final List<MonsterLimb.Schema> limbs,
      final List<Class<? extends Monster.FallbackValueGenerator>> defaultGenerators) {
    this(instantiate(setup), limbs, defaultGenerators);
  }

  private static <X> Monster.Setup<X> instantiate(
      final Class<? extends Monster.Setup<X>> monsterSetup) {
    try {
      return monsterSetup.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(String.format("Unable to instantiate '%s'.", monsterSetup));
    }
  }

  public void setValue(final MonsterLimb.Schema method, final Object value) {
    this.values.put(method, value);
  }

  public T build(final MonsterArchetype.Schema archetype) {
    return new Spawner<T>().spawn(this, Optional.ofNullable(archetype));
  }

  public T build() {
    return this.build(null);
  }

  MonsterLimb.Setup<T> getLimbSetup() {
    return this.setup;
  }

  MonsterArchetype.Setup<T> getArchetypeSetup() {
    return this.setup;
  }

  Map<MonsterLimb.Schema, Object> getValues() {
    return this.values;
  }

  public List<MonsterLimb.Schema> getLimbs() {
    return this.limbs;
  }

  public List<Class<? extends Monster.FallbackValueGenerator>> getDefaultGenerators() {
    return this.defaultGenerators;
  }

  public Optional<Object> getValue(final MonsterLimb.Schema limb) {
    return Optional.ofNullable(this.getValues().get(limb));
  }
}
