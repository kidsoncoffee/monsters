package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.MonsterLab;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kidsoncoffee.monsters.example.MyDataObjectMonsterLabLimbSetup.DEFAULT_PROFESSIONS;
import static com.kidsoncoffee.monsters.example.MyDataObjectMonsterLabLimbSetup.RETAIL_JEDI;
import static com.kidsoncoffee.monsters.example.MyDataObjectMonsterLabLimbSetup.RETAIL_JEDI_PROFESSION;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("UnusedLabel") // USED FOR BDD
@Ignore
public class MyMonsterLabExampleTest {

  @Test
  public void notNullLimbValue() {
    final MyDataObject monster;
    final String name;

    given:
    monster = MonsterLab.create(MyDataObject.class).build();

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
    monster = null; // MyDataObjectMonsterBuilder.MyDataObject().build();

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
    monsterA =  null; //MyDataObjectMonsterBuilder.MyDataObject().build();
    monsterB =  null; //MyDataObjectMonsterBuilder.MyDataObject().build();

    when:
    nameA = monsterA.getName();
    nameB = monsterB.getName();

    then:
    assertThat(nameA)
        .as("The generated value for a limb should not be the same for different monsters.")
        .isNotSameAs(nameB);
  }

  @Test
  public void customGenerateMonsterLimbValue() {
    final List<MyDataObject> monstersOutOfDefaultChoices;

    when:
    monstersOutOfDefaultChoices =
        IntStream.range(0, 100)
            .mapToObj(i ->  new MyDataObject()) //MyDataObjectMonsterBuilder.MyDataObject().build())
            //.filter(monster -> !DEFAULT_PROFESSIONS.contains(monster.getProfession()))
            .collect(Collectors.toList());

    then:
    assertThat(monstersOutOfDefaultChoices)
        .as("There shouldn't be any monster with a profession outside of the default choices.")
        .hasSize(0);
  }

  @Test
  public void archetypeTakesPrecedenceOverDefault() {
    final List<MyDataObject> monstersNotRetailJedi;

    when:
    monstersNotRetailJedi =
        IntStream.range(0, 100)
            .mapToObj(i -> new MyDataObject())//MyDataObjectMonsterBuilder.MyDataObject().build(RETAIL_JEDI))
            //.filter(monster -> !RETAIL_JEDI_PROFESSION.equals(monster.getProfession()))
            .collect(Collectors.toList());

    then:
    assertThat(monstersNotRetailJedi)
        .as("There shouldn't be any monster that is not a 'Retail Jedi' for the archetype.")
        .hasSize(0);
  }
}
