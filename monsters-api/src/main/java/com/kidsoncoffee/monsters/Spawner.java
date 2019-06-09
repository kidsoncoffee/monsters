package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.*;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Spawner<T> {

  public T spawn(final MonsterBuilder<T> builder) {
    final CallHistory callHistory = new CallHistory();
    final ValueStore valueStore = new ValueStore();

    final MonsterMember.ValueBinder generatorBinder = new MonsterMember.ValueBinder(callHistory);
    final RecordingInterceptor recordingInterceptor = new RecordingInterceptor(callHistory);
    final PlayingInterceptor playingInterceptor = new PlayingInterceptor(valueStore);

    final InterceptorModeCentral central =
        new InterceptorModeCentral(recordingInterceptor, playingInterceptor);
    final Interceptor interceptor = new Interceptor(central);

    final List<? extends Monster.DefaultGenerator> defaultGenerators =
        instantiate(builder.getDefaultGenerators());

    central.startRecording();

    final Class monsterClass = this.getTargetClass(builder);
    final T monster = this.createMonster(monsterClass, interceptor);

    final Monster.Setup<T> setup = builder.getSetup();
    setup.setup(generatorBinder, monster);

    builder
        .getLimbs()
        .forEach(
            limb -> {
              final Object value = generateValue(generatorBinder, defaultGenerators, limb);
              valueStore.set(limb, value);
            });

    central.startPlaying();
    return monster;
  }

  private Object generateValue(
      MonsterMember.ValueBinder generatorBinder,
      List<? extends Monster.DefaultGenerator> defaultGenerators,
      MonsterMember.Schema limb) {
    final Optional<MonsterMember.ValueGenerator> value = generatorBinder.getValue(limb);

    if (value.isPresent()) {
      return value.get();
    }
    return generateDefaultValue(limb, defaultGenerators);
  }

  private Object generateDefaultValue(
      final MonsterMember.Schema limb,
      final List<? extends Monster.DefaultGenerator> defaultGenerators) {
    for (final Monster.DefaultGenerator defaultGenerator : defaultGenerators) {
      final Optional<Object> value = defaultGenerator.generate(limb);

      if (value.isPresent()) {
        return value.get();
      }
    }
    throw new MonsterDefaultGeneratorNotApplicableException(limb);
  }

  private List<? extends Monster.DefaultGenerator> instantiate(
      List<Class<? extends Monster.DefaultGenerator>> defaultGenerators) {
    return defaultGenerators.stream()
        .map(
            d -> {
              try {
                return d.newInstance();
              } catch (InstantiationException | IllegalAccessException e) {
                throw new MonsterDefaultGeneratorNotInstantiableException(d);
              }
            })
        .collect(Collectors.toList());
  }

  private T createMonster(Class monsterClass, final Interceptor methodInterceptor) {
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(monsterClass);
    enhancer.setCallback(methodInterceptor);
    return (T) enhancer.create();
  }

  private Class getTargetClass(final MonsterBuilder builder) {
    return (Class)
        ((ParameterizedType) builder.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }
}
