package com.kidsoncoffee.monsters;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterMemberSchemaParse {
  public static MonsterMember.Schema parse(final Method method) {
    final String memberName = method.getName();
    final boolean publicAccess = Modifier.isPublic(method.getModifiers());
    final boolean packagePrivateAccess =
        !Modifier.isPrivate(method.getModifiers())
            && !Modifier.isPublic(method.getModifiers())
            && !Modifier.isProtected(method.getModifiers());
    return new ImmutableMonsterMemberSchema(
        memberName, memberParameters(method), true, false, publicAccess, packagePrivateAccess);
  }

  private static List<String> memberParameters(final Method method) {
    return Arrays.stream(method.getParameters())
        .map(Parameter::getName)
        .collect(Collectors.toList());
  }
}
