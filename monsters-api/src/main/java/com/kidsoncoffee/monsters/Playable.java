package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.UnderlyingInterceptor;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Playable implements UnderlyingInterceptor {

  private final Class monsterTargetClass;
  private final ValueGeneratorStore store;

  public Playable(Class monsterTargetClass, ValueGeneratorStore store) {
    this.monsterTargetClass = monsterTargetClass;
    this.store = store;
  }
}
