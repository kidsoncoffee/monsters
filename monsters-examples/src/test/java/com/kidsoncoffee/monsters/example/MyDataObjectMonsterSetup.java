package com.kidsoncoffee.monsters.example;

import com.kidsoncoffee.monsters.Monster;
import com.kidsoncoffee.monsters.MonsterMember;

@Monster
public class MyDataObjectMonsterSetup implements Monster.Setup<MyDataObject> {
  @Override
  public void setup(final MonsterMember.ValueBinder binder, final MyDataObject monster) {}
}
