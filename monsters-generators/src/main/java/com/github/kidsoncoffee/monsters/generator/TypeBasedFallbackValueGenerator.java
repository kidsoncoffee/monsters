package com.github.kidsoncoffee.monsters.generator;

import com.github.javafaker.Faker;
import com.github.kidsoncoffee.monsters.Monster;
import com.github.kidsoncoffee.monsters.MonsterLimb;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TypeBasedFallbackValueGenerator implements Monster.FallbackValueGenerator {
  private static final Faker FAKER = new Faker();

  @Override
  public Optional<Object> generate(final MonsterLimb.Schema limbSchema) {
    if (String.class.isAssignableFrom(limbSchema.getType())) {
      return Optional.of(
          FAKER.letterify(IntStream.range(0, 32).mapToObj(i -> "?").collect(Collectors.joining())));
    }

    if (Integer.class.isAssignableFrom(limbSchema.getType())
        || int.class.isAssignableFrom(limbSchema.getType())) {
      return Optional.of(FAKER.number().randomDigit());
    }

    if (Long.class.isAssignableFrom(limbSchema.getType())
        || long.class.isAssignableFrom(limbSchema.getType())) {
      return Optional.of(FAKER.number().randomNumber());
    }

    if (Double.class.isAssignableFrom(limbSchema.getType())
        || double.class.isAssignableFrom(limbSchema.getType())) {
      return Optional.of(FAKER.number().randomDouble(10, 0, 100000000));
    }

    return Optional.empty();
  }
}
