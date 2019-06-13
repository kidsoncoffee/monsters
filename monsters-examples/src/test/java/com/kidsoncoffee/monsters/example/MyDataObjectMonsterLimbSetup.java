package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.Monster;
import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.generator.TypeBasedGenerator;

import java.util.Arrays;
import java.util.List;

@Monster(defaultGenerators = TypeBasedGenerator.class)
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

  public static final List<String> DEFAULT_PROFESSIONS =
      Arrays.asList("Beverage Dissemination Officer", "Chick Sexer", "Digital Overlord");

  public static final String RETAIL_JEDI_PROFESSION = "Retail Jedi";

  @Override
  public void setup(final MonsterLimb.Generation generation, final MyDataObject monster) {
    generation.on(monster.getProfession()).pickFrom(DEFAULT_PROFESSIONS);
    generation.on(monster.getNumber()).fix(42);
  }

  @Override
  public void setup(Monster.ArchetypeBinding<MyDataObject> archetype) {
    archetype
        .when(ARCHETYPES.RETAIL_JEDI)
        .setup(
            (generation, monster) -> {
              generation.on(monster.getProfession()).fix(RETAIL_JEDI_PROFESSION);
            });
  }

  enum ARCHETYPES implements Monster.Archetype {
    RETAIL_JEDI("Retail Jedi"),
    OTHER("Other");

    private final String description;

    ARCHETYPES(final String description) {
      this.description = description;
    }

    @Override
    // TODO fchovich DO WE NEED DESCRIPTION?
    public String getDescription() {
      return this.description;
    }
  }
}
