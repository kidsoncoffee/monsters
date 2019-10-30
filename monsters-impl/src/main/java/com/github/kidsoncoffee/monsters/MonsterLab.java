package com.github.kidsoncoffee.monsters;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public final class MonsterLab {
  private static final Map<Class<?>, Class<? extends MonsterBuilder>> BUILDERS = new HashMap<>();

  public static final <T> MonsterBuilder<T> create(final Class<T> dataObjectClass) {
    if (!BUILDERS.containsKey(dataObjectClass)) {
      final Reflections reflections = new Reflections(dataObjectClass.getPackage().getName());
      final Set<Class<? extends MonsterBuilder>> monsters =
          reflections.getSubTypesOf(MonsterBuilder.class);
      monsters.forEach(monster -> BUILDERS.put(monster.getClass(), monster));
    }

    try {
      return (MonsterBuilder<T>) BUILDERS.get(dataObjectClass).newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  public static final <T> MonsterBuilder<T> monster(final Class<T> dataObjectClass) {
    return MonsterLab.create(dataObjectClass);
  }

  public static final <T> MonsterBuilder<T> create(
      final Class<T> dataObjectClass, final MonsterArchetype.Schema archetype) {
    throw new UnsupportedOperationException("THIS DONT MAKE SENSE?");
  }

  public static final <T> MonsterBuilder<T> monster(
      final Class<T> dataObjectClass, final MonsterArchetype.Schema archetype) {
    return MonsterLab.create(dataObjectClass, archetype);
  }
}
