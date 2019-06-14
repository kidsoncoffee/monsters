package com.github.kidsoncoffee.monsters;

import com.github.kidsoncoffee.monsters.interaction.CallHistory;
import com.github.kidsoncoffee.monsters.interaction.DefaultArchetypeBindingBuilder;
import com.github.kidsoncoffee.monsters.interaction.DefaultArchetypeLimbSetup;
import com.github.kidsoncoffee.monsters.interaction.LimbSetupStore;
import com.github.kidsoncoffee.monsters.interception.Interceptor;
import com.github.kidsoncoffee.monsters.interception.InterceptorModeCentral;
import com.github.kidsoncoffee.monsters.interception.PlayingInterceptor;
import com.github.kidsoncoffee.monsters.interception.RecordingInterceptor;
import com.github.kidsoncoffee.monsters.interception.ValueStore;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Spawner<T> {

  private static Class getTargetClass(final MonsterBuilder builder) {
    return (Class)
        ((ParameterizedType) builder.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  private static List<Monster.DefaultGenerator> instantiate(
      List<Class<? extends Monster.DefaultGenerator>> defaultGenerators) {
    return defaultGenerators.stream()
        .map(
            d -> {
              try {
                return d.newInstance();
              } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(
                    String.format(
                        "Unable to instantiate the default generator '%s'. A public zero arguments constructors is mandatory.",
                        d));
              }
            })
        .map(Monster.DefaultGenerator.class::cast)
        .collect(Collectors.toList());
  }

  private static <V> V createMonster(
      final Class<V> monsterClass, final Interceptor methodInterceptor) {
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(monsterClass);
    enhancer.setCallback(methodInterceptor);
    return (V) enhancer.create();
  }

  public T spawn(
      final MonsterBuilder<T> builder, final Optional<MonsterArchetype.Schema> archetype) {
    final ValueStore valueStore = new ValueStore();
    final LimbSetupStore<T> limbSetupStore = new LimbSetupStore<>();

    final MonsterArchetype.LimbSetup<T> defaultArchetypeBindingBuilder =
        new DefaultArchetypeBindingBuilder<>(limbSetupStore);
    final MonsterArchetype.Binding<T> archetypeBinding =
        new DefaultArchetypeLimbSetup<>(limbSetupStore, defaultArchetypeBindingBuilder);

    final CallHistory callHistory = new CallHistory();
    final RecordingInterceptor recordingInterceptor = new RecordingInterceptor(callHistory);
    final PlayingInterceptor playingInterceptor = new PlayingInterceptor(valueStore);

    final InterceptorModeCentral central =
        new InterceptorModeCentral(recordingInterceptor, playingInterceptor);
    final Interceptor interceptor = new Interceptor(central);

    final MonsterLimbGeneratorGatherer generatorGatherer =
        new MonsterLimbGeneratorGatherer(callHistory);
    final List<Monster.DefaultGenerator> defaultGenerators =
        instantiate(builder.getDefaultGenerators());

    final MonsterLimbValueResolver valueResolver = new MonsterLimbValueResolver();

    central.startRecording();

    final Class monsterClass = getTargetClass(builder);
    final T monster = (T) createMonster(monsterClass, interceptor);

    limbSetupStore.setCurrent(MonsterArchetype.DEFAULT);
    limbSetupStore.addToCurrent(builder.getLimbSetup());

    if (archetype.isPresent()) {
      builder.getArchetypeSetup().setup(archetypeBinding);
    }

    final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> limbValueGenerators =
        generatorGatherer.gather(
            limbSetupStore,
            archetype.orElse(MonsterArchetype.DEFAULT),
            builder.getLimbs(),
            monster);

    builder
        .getLimbs()
        .forEach(
            limb ->
                valueStore.set(
                    limb,
                    valueResolver.resolve(builder, limbValueGenerators, defaultGenerators, limb)));

    central.startPlaying();
    return monster;
  }
}
