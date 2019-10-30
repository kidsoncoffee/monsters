package com.github.kidsoncoffee.monsters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MonsterLimbValueResolver {

  public <T> Object resolve(
      final MonsterBuilder<T> builder,
      final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> generators,
      final List<MonsterSetup.FallbackValueGenerator> fallbackValueGenerators,
      final MonsterLimb.Schema limb) {
    return builder
        .getValue(limb)
        .orElseGet(() -> resolve(generators, fallbackValueGenerators, limb));
  }

  private Object resolve(
      final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> generators,
      final List<MonsterSetup.FallbackValueGenerator> fallbackValueGenerators,
      final MonsterLimb.Schema limb) {
    if (generators.containsKey(limb)) {
      return generators.get(limb).generate();
    }
    return fallbackValueGenerators.stream()
        .sequential()
        .map(g -> g.generate(limb))
        .filter(Optional::isPresent)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalStateException(
                    String.format("Unable to find value for '%s'.", limb.getName())))
        .get();
  }
}
