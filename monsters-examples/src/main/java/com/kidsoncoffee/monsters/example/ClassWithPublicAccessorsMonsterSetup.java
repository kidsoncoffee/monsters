package com.kidsoncoffee.monsters.example;

import com.kidsoncoffee.monsters.Monster;
import com.kidsoncoffee.monsters.MonsterMember;

import java.util.Random;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@Monster
public class ClassWithPublicAccessorsMonsterSetup
    implements Monster.Setup<ClassWithPublicAccessors> {

  private static String generateFirstValue() {
    return Integer.toString(new Random().nextInt());
  }

  @Override
  public void setup(
      final MonsterMember.ValueBinder binder, final ClassWithPublicAccessors monster) {

    binder.generate(
        monster.getFirstValue(), ClassWithPublicAccessorsMonsterSetup::generateFirstValue);
    binder.stub(monster.getSecondValue(), 666);
  }
}
