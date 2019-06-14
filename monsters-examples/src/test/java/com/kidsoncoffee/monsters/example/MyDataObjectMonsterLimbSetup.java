package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.ImmutableMonsterArchetypeSchema;
import com.github.kidsoncoffee.monsters.Monster;
import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.MonsterOptions;
import com.github.kidsoncoffee.monsters.generator.TypeBasedGenerator;

import java.util.Arrays;
import java.util.List;

@MonsterOptions(defaultGenerators = TypeBasedGenerator.class)
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

  public static final List<String> DEFAULT_PROFESSIONS =
      Arrays.asList("Beverage Dissemination Officer", "Chick Sexer", "Digital Overlord");

  public static final String RETAIL_JEDI_PROFESSION = "Retail Jedi";

  public static final MonsterArchetype.Schema RETAIL_JEDI =
      ImmutableMonsterArchetypeSchema.builder().description("The retail Jedi").build();

  public static final MonsterArchetype.Schema OTHERS =
      ImmutableMonsterArchetypeSchema.builder().description("The other").build();

  @Override
  public void setup(final MonsterLimb.Generation generation, final MyDataObject monster) {
    generation.on(monster.getProfession()).pickFrom(DEFAULT_PROFESSIONS);
    generation.on(monster.getNumber()).fix(42);
  }

  @Override
  public void setup(final MonsterArchetype.Binding<MyDataObject> archetype) {
    archetype
        .when(RETAIL_JEDI)
        .setup(
            (generation, monster) -> {
              generation.on(monster.getProfession()).fix(RETAIL_JEDI_PROFESSION);
            });
  }
}
