package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.Monster;

public class MonsterDefaultGeneratorNotInstantiableException extends RuntimeException {
  public MonsterDefaultGeneratorNotInstantiableException(
      final Class<? extends Monster.DefaultGenerator> generator) {
    super(String.format("Unable to instantiate '%s'.", generator));
  }
}
