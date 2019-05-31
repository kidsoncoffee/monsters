package com.kidsoncoffee.monsters;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.ParameterizedType;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterSpawner<T> {
  private static final ValueGeneratorStore VALUE_GENERATOR_STORE = new ValueGeneratorStore();

  public T spawn(final MonsterBuilder<T> builder) {
    final Class monsterClass = this.getTargetClass(builder.getClass());

    if (!VALUE_GENERATOR_STORE.existsForMonster(monsterClass)) {
      // SETUP VALUE GENERATORS
    } else {
      // GENERATE VALUES FOR MONSTER
      // STORE VALUES FOR MONSTER
    }

    //
    //
    //
    //
    //

    final MonsterMemberCallHistory callHistory = new MonsterMemberCallHistory();
    final MonsterMember.ValueBinder valueBinder = new MonsterMember.ValueBinder(callHistory, callHistory);

    final MonsterMethodInterceptor methodInterceptor =
        new MonsterMethodInterceptor(
            monsterClass,
            Monster.DefaultArchetype.class,
            new MonsterValueStore(),
            valueBinder,
            callHistory,
            VALUE_GENERATOR_STORE);

    final Monster.Setup<T> setup = builder.getSetup();
    final T monster = this.createMonster(monsterClass, methodInterceptor);

    methodInterceptor.startRecording();
    try {
      setup.setup(valueBinder, monster);
    } finally {
      methodInterceptor.stopRecording();
    }

    return monster;
  }

  private T createMonster(Class monsterClass, final MonsterMethodInterceptor methodInterceptor) {
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(monsterClass);
    enhancer.setCallback(methodInterceptor);
    return (T) enhancer.create();
  }

  private Class getTargetClass(final Class<? extends MonsterBuilder> monsterClass) {
    return (Class)
        ((ParameterizedType) monsterClass.getGenericSuperclass()).getActualTypeArguments()[0];
  }
}
