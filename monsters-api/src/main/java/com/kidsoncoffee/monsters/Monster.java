package com.kidsoncoffee.monsters;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public @interface Monster {

  interface Setup<T> {
    void setup(final MonsterMember.ValueBinder binder, final T monster);
  }

  interface Archetype {
    String getDescription();
  }

  class DefaultArchetype implements Archetype {

    @Override
    public String getDescription() {
      return "Default monster";
    }
  }

  interface DefaultGenerator {}

  Class<? extends DefaultGenerator>[] defaultGenerators() default {};
}
