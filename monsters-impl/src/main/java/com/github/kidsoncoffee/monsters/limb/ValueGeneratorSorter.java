package com.github.kidsoncoffee.monsters.limb;

import com.github.kidsoncoffee.monsters.ImmutableMonsterLimbSchema;
import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.archetype.LimbsSetupsByArchetype;
import com.github.kidsoncoffee.monsters.interaction.CallHistory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ValueGeneratorSorter {

  private final CallHistory callHistory;

  public ValueGeneratorSorter(final CallHistory callHistory) {
    this.callHistory = callHistory;
  }

  private static List<MonsterLimb.Schema> remaining(
      final List<MonsterLimb.Schema> limbs, final ValueGeneratorSetupImpl generationSetup) {
    return limbs.stream()
        .filter(limb -> !generationSetup.constainsValue(ImmutableMonsterLimbSchema.copyOf(limb)))
        .collect(Collectors.toList());
  }

  public <T> Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> sort(
      final LimbsSetupsByArchetype<T> archetypesSetupStore,
      final MonsterArchetype.Schema archetype,
      final List<MonsterLimb.Schema> limbs,
      final T monster) {
    final ValueGeneratorSetupImpl valueGeneratorSetup =
        new ValueGeneratorSetupImpl(this.callHistory);
    final MonsterLimb.ValueGeneratorBinding valueGeneratorBinding =
        new ValueGeneratorBindingImpl(valueGeneratorSetup);

    if (!archetypesSetupStore.get(archetype).isPresent()) {
      return Collections.emptyMap();
    }

    final MonsterLimb.Setup<T> defaultSetup = archetypesSetupStore.get(archetype).get();
    defaultSetup.setup(valueGeneratorBinding, monster);

    final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> generators = new HashMap<>();
    for (final MonsterLimb.Schema limb : limbs) {
      final Optional<MonsterLimb.ValueGenerator> valueGenerator =
          valueGeneratorSetup.getValue(limb);
      valueGenerator.ifPresent(generator -> generators.put(limb, generator));
    }

    archetype
        .extendsFrom()
        .map(
            parentArchetype ->
                sort(
                    archetypesSetupStore,
                    parentArchetype,
                    remaining(limbs, valueGeneratorSetup),
                    monster))
        .ifPresent(generators::putAll);

    return generators;
  }
}
