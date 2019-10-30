package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.ImmutableMonsterArchetypeSchema;
import com.github.kidsoncoffee.monsters.MonsterArchetype;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.MonsterOptions;
import com.github.kidsoncoffee.monsters.MonsterSetup;
import com.github.kidsoncoffee.monsters.generator.TypeBasedFallbackValueGenerator;

import java.util.Arrays;
import java.util.List;

//@MonsterOptions(defaultGenerators = TypeBasedFallbackValueGenerator.class)
public class MyDataObjectMonsterLabLimbSetup implements MonsterSetup<MyDataObject> {

  public static final List<String> DEFAULT_PROFESSIONS =
      Arrays.asList("Beverage Dissemination Officer", "Chick Sexer", "Digital Overlord");

  public static final String RETAIL_JEDI_PROFESSION = "Retail Jedi";

  public static final MonsterArchetype.Schema RETAIL_JEDI =
      ImmutableMonsterArchetypeSchema.builder().description("The retail Jedi").build();

  public static final MonsterArchetype.Schema OTHERS =
      ImmutableMonsterArchetypeSchema.builder().description("The other").build();

  @Override
  public void setup(
      final MonsterLimb.ValueGeneratorBinding valueGeneratorBinding, final MyDataObject monster) {
    valueGeneratorBinding.on(monster.getProfession()).pickFrom(DEFAULT_PROFESSIONS);
    valueGeneratorBinding.on(monster.getNumber()).fix(42);
  }

  @Override
  public void archetypeSetup(
      final MonsterArchetype.ValueGeneratorBinding<MyDataObject> archetypeBinding) {
    archetypeBinding
        .when(RETAIL_JEDI)
        .setup(
            (valueGeneratorBinding, monster) -> {
              valueGeneratorBinding.on(monster.getProfession()).fix(RETAIL_JEDI_PROFESSION);
            });
  }
}
