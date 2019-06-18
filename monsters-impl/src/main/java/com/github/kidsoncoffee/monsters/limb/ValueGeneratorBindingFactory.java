package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.MonsterArchetype;

import java.util.function.Supplier;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorBindingFactory implements Supplier<MonsterArchetype.ValueGeneratorBinding> {

  @Override
  public MonsterArchetype.ValueGeneratorBinding get() {
    return new ValueGeneratorBindingImpl();
  }
}
