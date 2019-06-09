package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;

public class SimpleValueGenerator implements MonsterMember.ValueGenerator {

  private final Object value;

  private SimpleValueGenerator(final Object value) {
    this.value = value;
  }

  public static final SimpleValueGenerator returnValue(final Object value) {
    return new SimpleValueGenerator(value);
  }

  @Override
  public Object generate() {
    return this.value;
  }
}
