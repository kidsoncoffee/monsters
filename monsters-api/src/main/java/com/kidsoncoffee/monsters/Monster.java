package com.kidsoncoffee.monsters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monster {

  Class<? extends DefaultGenerator>[] defaultGenerators() default {};

  interface Setup<T> {
    void setup(final MonsterMember.ValueBinder binder, final T monster);
  }

  interface Archetype {
    String getDescription();
  }

  interface DefaultGenerator {
    Optional<Object> generate(final MonsterMember.Schema limb);
  }

  class DefaultArchetype implements Archetype {

    @Override
    public String getDescription() {
      return "Default monster";
    }
  }
}
