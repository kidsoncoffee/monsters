package com.github.kidsoncoffee.monsters;

import java.lang.reflect.Method;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterMemberSchemaParser {
  public static MonsterLimb.Schema parse(final Method method) {
    return ImmutableMonsterLimbSchema.builder()
        .name(method.getName())
        .type(method.getReturnType())
        .build();
  }
}
