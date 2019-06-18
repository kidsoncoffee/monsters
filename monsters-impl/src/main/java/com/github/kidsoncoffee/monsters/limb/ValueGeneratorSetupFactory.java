package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.MonsterLimb;

import java.util.function.Supplier;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorSetupFactory implements Supplier<MonsterLimb.ValueGeneratorSetup> {
  @Override
  public MonsterLimb.ValueGeneratorSetup get() {
    return new ValueGeneratorSetupImpl();
  }
}
