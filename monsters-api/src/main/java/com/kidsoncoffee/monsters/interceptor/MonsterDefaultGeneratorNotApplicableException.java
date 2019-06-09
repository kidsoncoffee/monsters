package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;

public class MonsterDefaultGeneratorNotApplicableException extends RuntimeException {
  public MonsterDefaultGeneratorNotApplicableException(final MonsterMember.Schema limb) {
    super(String.format("No default generator was applicable for '%s'.", limb.getName()));
  }
}
