package com.kidsoncoffee.monsters.example;

import com.github.kidsoncoffee.monsters.Monster;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@Monster.Options
public class NewDataObjectMonsterSetup implements Monster.Setup<NewDataObject> {

  @Override
  public void setup(final Monster.ArchetypeSetup<NewDataObject> archetypeSetup) {
    archetypeSetup.when((binding, dataObject) -> {});
    archetypeSetup.when(Archetypes.WHATEVER, (binding, dataObject) -> {});
    archetypeSetup.when(
        Archetypes.SOMETHING,
        Archetypes.WHATEVER,
        (valueBinding, dataObject) -> {});
  }

  public enum Archetypes implements Monster.Archetype {
    WHATEVER("Whatever"),
    SOMETHING("Something");

    private final String archetypeName;

    Archetypes(String archetypeName) {
      this.archetypeName = archetypeName;
    }

    @Override
    public String archetypeName() {
      return this.archetypeName;
    }
  }
}
