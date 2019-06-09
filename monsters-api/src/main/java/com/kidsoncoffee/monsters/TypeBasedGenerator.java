package com.kidsoncoffee.monsters;

import com.github.javafaker.Faker;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TypeBasedGenerator implements Monster.DefaultGenerator {
  private static final Faker FAKER = new Faker();

  @Override
  public Optional<Object> generate(final MonsterMember.Schema limb) {
    if (String.class.isAssignableFrom(limb.getType())) {
      return Optional.of(
          FAKER.letterify(IntStream.range(0, 32).mapToObj(i -> "?").collect(Collectors.joining())));
    }

    if (Integer.class.isAssignableFrom(limb.getType())) {
      return Optional.of(FAKER.number().randomDigit());
    }

    if (Long.class.isAssignableFrom(limb.getType())) {
      return Optional.of(FAKER.number().randomNumber());
    }

    if (Double.class.isAssignableFrom(limb.getType())) {
      return Optional.of(FAKER.number().randomDouble(10, 0, 100000000));
    }

    return Optional.empty();
  }
}
