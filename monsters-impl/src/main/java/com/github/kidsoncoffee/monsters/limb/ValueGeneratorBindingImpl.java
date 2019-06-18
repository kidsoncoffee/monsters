package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.MonsterLimb;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorBindingImpl implements MonsterLimb.ValueGeneratorBinding {

  private final MonsterLimb.ValueGeneratorSetup valueGeneratorSetup;

  public ValueGeneratorBindingImpl(final MonsterLimb.ValueGeneratorSetup valueGeneratorSetup) {
    this.valueGeneratorSetup = valueGeneratorSetup;
  }

  @Override
  public <T> MonsterLimb.ValueGeneratorSetup<T> on(final T limbCall) {
    return this.valueGeneratorSetup;
  }
}
