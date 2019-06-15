package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.ImmutableMonsterArchetypeSchema;
import com.github.kidsoncoffee.monsters.Monster;
import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.MonsterOptions;
import com.github.kidsoncoffee.monsters.generator.TypeBasedDefaultGenerator;

import java.util.Arrays;
import java.util.List;

@MonsterOptions(defaultGenerators = TypeBasedDefaultGenerator.class)
public class MyDataObjectMonsterLimbDefaultSetup implements Monster.DefaultSetup<MyDataObject> {

  public static final List<String> DEFAULT_PROFESSIONS =
      Arrays.asList("Beverage Dissemination Officer", "Chick Sexer", "Digital Overlord");

  public static final String RETAIL_JEDI_PROFESSION = "Retail Jedi";

  public static final MonsterArchetype.Schema RETAIL_JEDI =
      ImmutableMonsterArchetypeSchema.builder().description("The retail Jedi").build();

  public static final MonsterArchetype.Schema OTHERS =
      ImmutableMonsterArchetypeSchema.builder().description("The other").build();

  @Override
  public void defaultSetup(final MonsterLimb.ValueGeneratorBinding valueGeneratorBinding, final MyDataObject monster) {
    valueGeneratorBinding.on(monster.getProfession()).pickFrom(DEFAULT_PROFESSIONS);
    valueGeneratorBinding.on(monster.getNumber()).fix(42);
  }

  @Override
  public void setup(final MonsterArchetype.Binding<MyDataObject> archetype) {
    archetype
        .when(RETAIL_JEDI)
        .setup(
            (valueGeneratorBinding, monster) -> {
              valueGeneratorBinding.on(monster.getProfession()).fix(RETAIL_JEDI_PROFESSION);
            });
  }
}
