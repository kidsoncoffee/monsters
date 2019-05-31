package com.kidsoncoffee.monsters.example;

import example.ClassWithPublicAccessorsMonsterBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class SimplePOJOGenerationTest {
  @Test
  public void fixedValues() {
    final ClassWithPublicAccessorsMonsterBuilder monsterBuilder =
        ClassWithPublicAccessorsMonsterBuilder.ClassWithPublicAccessors()
            .getFirstValue("FIXED A")
            .getSecondValue(123);

    final ClassWithPublicAccessors monster = monsterBuilder.build();

    Assert.assertEquals("FIXED A", monster.getFirstValue());
    Assert.assertEquals(123, monster.getSecondValue());
  }

  @Test
  public void generatedValues() {
    final ClassWithPublicAccessorsMonsterBuilder monsterSpawner =
        ClassWithPublicAccessorsMonsterBuilder.ClassWithPublicAccessors();

    final ClassWithPublicAccessors monster = monsterSpawner.build();

    Assert.assertEquals("FIXED A", monster.getFirstValue());
    Assert.assertEquals(123, monster.getSecondValue());
  }
}
