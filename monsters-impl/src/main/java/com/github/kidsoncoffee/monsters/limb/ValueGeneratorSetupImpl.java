package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.ImmutableMonsterLimbSchema;
import com.github.kidsoncoffee.monsters.MonsterLab;
import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.interaction.CallHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The default implementation of {@link
 * com.github.kidsoncoffee.monsters.MonsterLimb.ValueGeneratorSetup}.
 *
 * @author fernando.chovich
 * @since 1.0
 */
public class ValueGeneratorSetupImpl implements MonsterLimb.ValueGeneratorSetup {
  private final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> generatorsByLimb =
      new HashMap<>();

  private final CallHistory callHistory;

  public ValueGeneratorSetupImpl(final CallHistory callHistory) {
    this.callHistory = callHistory;
  }

  private static Object randomPickFrom(List choices) {
    final int randomPick = ThreadLocalRandom.current().nextInt(choices.size());
    return choices.get(randomPick);
  }

  public boolean constainsValue(final MonsterLimb.Schema member) {
    return this.generatorsByLimb.containsKey(member);
  }

  public Optional<MonsterLimb.ValueGenerator> getValue(final MonsterLimb.Schema member) {
    return Optional.ofNullable(
        this.generatorsByLimb.get(ImmutableMonsterLimbSchema.copyOf(member)));
  }

  @Override
  public void generate(final MonsterLimb.ValueGenerator generator) {
    this.generatorsByLimb.put(
        ImmutableMonsterLimbSchema.copyOf(this.callHistory.getLastCall()), generator);
  }

  @Override
  public void fix(final Object value) {
    this.generate(() -> value);
  }

  @Override
  public void pickFrom(final List choices) {
    this.generate(() -> randomPickFrom(choices));
  }

  @Override
  public void monster(final Class dataObject) {
    this.generate(() -> MonsterLab.create(dataObject));
  }

  @Override
  public void monster(final Class dataObject, final MonsterArchetype.Schema archetype) {
    this.generate(() -> MonsterLab.create(dataObject, archetype));
  }
}
