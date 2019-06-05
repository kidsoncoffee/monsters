package com.kidsoncoffee.monsters.example;

import static java.util.Arrays.asList;

import com.kidsoncoffee.monsters.ImmutableMonsterMemberSchema;
import com.kidsoncoffee.monsters.MonsterMember;
import java.lang.String;
import java.util.List;

enum MyDataObjectMonsterSchema {
  GETNAME("getName", asList(), false, true, true, false);

  final MonsterMember.Schema member;

  MyDataObjectMonsterSchema(final String name, final List<String> parameters, final boolean isField,
      final boolean isMethod, final boolean isPublic, final boolean isPackagePrivate) {
    this.member = new ImmutableMonsterMemberSchema(name, parameters, isField, isMethod, isPublic, isPackagePrivate);
  }

  public MonsterMember.Schema getMember() {
    return this.member;
  }
}
