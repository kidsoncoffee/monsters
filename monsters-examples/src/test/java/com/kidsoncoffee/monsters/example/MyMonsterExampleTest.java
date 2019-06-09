package com.kidsoncoffee.monsters.example;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyMonsterExampleTest {

  @Test
  public void notNullLimbValue() {
    final MyDataObject monster;
    final String name;

    given:
    monster = MyDataObjectMonsterBuilder.MyDataObject().build();

    when:
    name = monster.getName();

    then:
    assertThat(name)
        .as("The generated value of a limb should not be null.")
        .isNotNull()
        .as("The generated value of a limb should not be the default value of the data object.")
        .isNotEqualTo(new MyDataObject().getName());
  }

  @Test
  public void sameLimbValue() {
    final MyDataObject monster;
    final String name;
    final String nameAgain;

    given:
    monster = MyDataObjectMonsterBuilder.MyDataObject().build();

    when:
    name = monster.getName();
    nameAgain = monster.getName();

    then:
    assertThat(name)
        .as("The generated value for a limb should be the same for every monster call.")
        .isSameAs(nameAgain);
  }

  @Test
  public void differentMonsterDifferentLimbValue() {
    final MyDataObject monsterA;
    final MyDataObject monsterB;
    final String nameA;
    final String nameB;

    given:
    monsterA = MyDataObjectMonsterBuilder.MyDataObject().build();
    monsterB = MyDataObjectMonsterBuilder.MyDataObject().build();

    when:
    nameA = monsterA.getName();
    nameB = monsterB.getName();

    then:
    assertThat(nameA)
        .as("The generated value for a limb should not be the same for different monsters.")
        .isNotSameAs(nameB);
  }

  @Test
  public void customGenerateMonsterLimbValue() {}
}
