package com.kidsoncoffee.monsters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterMemberSchemaParser {
  public static MonsterMember.Schema parse(final Method method) {
    return ImmutableSchema.builder()
        .name(method.getName())
        .type(method.getReturnType())
        .parameters(
            Arrays.stream(method.getParameters())
                .map(Parameter::getName)
                .collect(Collectors.toList()))
        .build();
  }
}
