package com.kidsoncoffee.monsters;

import java.util.Random;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public enum MonsterNoises {
  RAUUUUHH,
  ROAAAARRRRRRR,
  SCREEEECCCHHH,
  SCRAHHHHH,
  BUUWWAARRR,
  RAAWWWR,
  BRRAAAWRR,
  HIIIIISSSSS,
  MEOW;

  public static MonsterNoises random() {
    final MonsterNoises[] monsterNoises = MonsterNoises.values();
    return monsterNoises[new Random().nextInt(monsterNoises.length)];
  }
}
