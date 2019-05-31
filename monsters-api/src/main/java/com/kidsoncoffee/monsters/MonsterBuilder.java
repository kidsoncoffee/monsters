package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.InterceptorModeCentral;
import com.kidsoncoffee.monsters.interceptor.PlayingInterceptor;
import com.kidsoncoffee.monsters.interceptor.RecordingInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterBuilder<T> {

  private final Monster.Setup<T> setup;

  private final Map<MonsterMember.Schema, Object> values = new HashMap<>();

  public MonsterBuilder(final Monster.Setup<T> setup) {
    this.setup = setup;
  }

  public MonsterBuilder(final Class<? extends Monster.Setup<T>> setup) {
    this(instantiate(setup));
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
    return values;
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
}
