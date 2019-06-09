package com.kidsoncoffee.monsters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterBuilder<T> {

  private final Monster.Setup<T> setup;

  private final List<MonsterMember.Schema> limbs;

  private final List<Class<? extends Monster.DefaultGenerator>> defaultGenerators;

  private final Map<MonsterMember.Schema, Object> values = new HashMap<>();

  public MonsterBuilder(
      final Monster.Setup<T> setup,
      final List<MonsterMember.Schema> limbs,
      final List<Class<? extends Monster.DefaultGenerator>> defaultGenerators) {
    this.setup = setup;
    this.limbs = limbs;
    this.defaultGenerators = defaultGenerators;
  }

  public MonsterBuilder(
      final Class<? extends Monster.Setup<T>> setup,
      final List<MonsterMember.Schema> limbs,
      final List<Class<? extends Monster.DefaultGenerator>> defaultGenerators) {
    this(instantiate(setup), limbs, defaultGenerators);
  }

  private static <X> Monster.Setup<X> instantiate(
      final Class<? extends Monster.Setup<X>> monsterSetup) {
    try {
      return monsterSetup.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // TODO fchovich CREATE MESSAGE
      throw new MonsterException();
    }
  }

  public void setValue(final MonsterMember.Schema method, final Object value) {
    this.values.put(method, value);
  }

  public T build() {
    return new Spawner<T>().spawn(this);
  }

  Monster.Setup<T> getSetup() {
    return this.setup;
  }

  Map<MonsterMember.Schema, Object> getValues() {
    return this.values;
  }

  public List<MonsterMember.Schema> getLimbs() {
    return this.limbs;
  }

  public List<Class<? extends Monster.DefaultGenerator>> getDefaultGenerators() {
    return this.defaultGenerators;
  }
}
