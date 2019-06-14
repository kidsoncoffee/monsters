package com.github.kidsoncoffee.monsters;

import com.github.kidsoncoffee.monsters.interaction.CallHistory;
import com.github.kidsoncoffee.monsters.interaction.DefaultGenerationSetup;
import com.github.kidsoncoffee.monsters.interaction.LimbSetupStore;

import java.util.*;
import java.util.stream.Collectors;

public class MonsterLimbGeneratorGatherer {

  private final CallHistory callHistory;

  public MonsterLimbGeneratorGatherer(final CallHistory callHistory) {
    this.callHistory = callHistory;
  }

  private static List<MonsterLimb.Schema> remaining(
      final List<MonsterLimb.Schema> limbs, final DefaultGenerationSetup generationSetup) {
    return limbs.stream()
        .filter(limb -> !generationSetup.constainsValue(ImmutableMonsterLimbSchema.copyOf(limb)))
        .collect(Collectors.toList());
  }

  public <T> Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> gather(
      final LimbSetupStore<T> archetypesSetupStore,
      final MonsterArchetype.Schema archetype,
      final List<MonsterLimb.Schema> limbs,
      final T monster) {
    final DefaultGenerationSetup generationSetup = new DefaultGenerationSetup(this.callHistory);
    final MonsterLimb.Generation generation = new MonsterLimb.Generation(generationSetup);

    if (!archetypesSetupStore.get(archetype).isPresent()) {
      return Collections.emptyMap();
    }

    final MonsterLimb.Setup<T> setup = archetypesSetupStore.get(archetype).get();
    setup.setup(generation, monster);

    final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> generators = new HashMap<>();
    for (final MonsterLimb.Schema limb : limbs) {
      final Optional<MonsterLimb.ValueGenerator> valueGenerator = generationSetup.getValue(limb);
      valueGenerator.ifPresent(generator -> generators.put(limb, generator));
    }

    archetype
        .extendsFrom()
        .map(
            parentArchetype ->
                gather(
                    archetypesSetupStore,
                    parentArchetype,
                    remaining(limbs, generationSetup),
                    monster))
        .ifPresent(generators::putAll);

    return generators;
  }
}
