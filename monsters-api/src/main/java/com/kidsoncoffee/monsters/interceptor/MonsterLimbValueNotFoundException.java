package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;

public class MonsterLimbValueNotFoundException extends RuntimeException {
  public MonsterLimbValueNotFoundException(final MonsterMember.Schema member) {
    super(String.format("Value for '%s' not generated.", member));
  }
}
