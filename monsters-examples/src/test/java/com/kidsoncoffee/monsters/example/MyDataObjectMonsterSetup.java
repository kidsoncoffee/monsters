package com.kidsoncoffee.monsters.example;

import com.kidsoncoffee.monsters.Monster;
import com.kidsoncoffee.monsters.MonsterMember;
import com.kidsoncoffee.monsters.TypeBasedGenerator;

@Monster(defaultGenerators = TypeBasedGenerator.class)
public class MyDataObjectMonsterSetup implements Monster.Setup<MyDataObject> {
  @Override
  public void setup(final MonsterMember.ValueBinder binder, final MyDataObject monster) {
    // set up your custom generators here
  }
}
