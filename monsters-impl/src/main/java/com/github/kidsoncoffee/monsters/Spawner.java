package com.github.kidsoncoffee.monsters;

import com.github.kidsoncoffee.monsters.interaction.CallHistory;
import com.github.kidsoncoffee.monsters.archetype.ArchetypeLimbValueGeneratorSetupImpl;
import com.github.kidsoncoffee.monsters.archetype.ArchetypeValueGeneratorBindingImpl;
import com.github.kidsoncoffee.monsters.archetype.LimbsSetupsByArchetype;
import com.github.kidsoncoffee.monsters.interception.Interceptor;
import com.github.kidsoncoffee.monsters.interception.InterceptorModeCentral;
import com.github.kidsoncoffee.monsters.interception.PlayingInterceptor;
import com.github.kidsoncoffee.monsters.interception.RecordingInterceptor;
import com.github.kidsoncoffee.monsters.interception.ValueStore;
import com.github.kidsoncoffee.monsters.limb.ValueGeneratorSorter;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spawns a <strong>Monster</strong>.
 *
 * @author fernando.chovich
 * @since 1.0
 */
public class Spawner<T> {

  /**
   * Retrieves the type of the <strong>Monster</strong> by looking into the {@link MonsterBuilder}
   * type parameter.
   *
   * @param builder The builder to look into the type parameters.
   * @return The type of the <strong>Monster</strong>.
   */
  private static Class retrieveTargetClass(final MonsterBuilder builder) {
    return (Class)
        ((ParameterizedType) builder.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * Instantiates and returns the {@link
   * com.github.kidsoncoffee.monsters.Monster.FallbackValueGenerator} in order.
   *
   * @param fallbackValueGenerators The fallback value generators classes.
   * @return The instantiated fallback value generators.
   */
  private static List<Monster.FallbackValueGenerator> instantiate(
      List<Class<? extends Monster.FallbackValueGenerator>> fallbackValueGenerators) {
    return fallbackValueGenerators.stream()
        .map(Spawner::instantiate)
        .map(Monster.FallbackValueGenerator.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Instantiate and return the {@link
   * com.github.kidsoncoffee.monsters.Monster.FallbackValueGenerator}. It is expected that the class
   * has a public zero arguments constructor.
   *
   * @param fallbackValueGeneratorClass The fallback value generator class to instantiate.
   * @return The instantiated fallback value generator.
   */
  private static Monster.FallbackValueGenerator instantiate(
      Class<? extends Monster.FallbackValueGenerator> fallbackValueGeneratorClass) {
    try {
      return fallbackValueGeneratorClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(
          String.format(
              "Unable to instantiate the default generator '%s'. A public zero arguments constructors is mandatory.",
              fallbackValueGeneratorClass));
    }
  }

  /**
   * Creates a proxy of the given type.
   *
   * @param monsterClass The superclass of the proxy.
   * @param methodInterceptor The method interceptor.
   * @param <V> The type of the proxy.
   * @return The instantiated proxy.
   */
  private static <V> V createMonster(
      final Class<V> monsterClass, final Interceptor methodInterceptor) {
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(monsterClass);
    enhancer.setCallback(methodInterceptor);
    return (V) enhancer.create();
  }

  /**
   * Index {@link com.github.kidsoncoffee.monsters.MonsterLimb.ValueGenerator} by its limb. It will
   * set the {@link MonsterBuilder#getLimbSetup()} as the {@link
   * com.github.kidsoncoffee.monsters.MonsterLimb.ValueGenerator} for the {@link
   * MonsterArchetype#DEFAULT} <strong>Archetype</strong>. Then it will invoke the {@link
   * ValueGeneratorSorter} to go through all set scopes.
   *
   * @param builder The <strong>Monster</strong> builder.
   * @param archetype The archetype to create the <strong>Monster</strong> from.
   * @param limbsSetupsByArchetype The
   * @param archetypeValueGeneratorBinding
   * @param generatorGatherer
   * @param monster
   * @param <T>
   * @return
   */
  private static <T> Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> indexValueGenerators(
      final MonsterBuilder<T> builder,
      final MonsterArchetype.Schema archetype,
      final LimbsSetupsByArchetype<T> limbsSetupsByArchetype,
      final MonsterArchetype.ValueGeneratorBinding<T> archetypeValueGeneratorBinding,
      final ValueGeneratorSorter generatorGatherer,
      final T monster) {
    limbsSetupsByArchetype.setCurrent(MonsterArchetype.DEFAULT);
    limbsSetupsByArchetype.addToCurrent(builder.getLimbSetup());

    if (archetype != null) {
      builder.getArchetypeSetup().archetypeSetup(archetypeValueGeneratorBinding);
    }

    return generatorGatherer.sort(
        limbsSetupsByArchetype,
        archetype != null ? archetype : MonsterArchetype.DEFAULT,
        builder.getLimbs(),
        monster);
  }

  /**
   * Spawns the <strong>Monster</strong> that matches the given <strong>Archetype</strong> using the
   * information from the builder.
   *
   * <p><@param builder The <strong>Monster</strong> builder.
   *
   * @param archetype The <strong>Archetype</strong> to spawn.
   * @return The <strong>Monster</strong> ready to be used.
   */
  T spawn(final MonsterBuilder<T> builder, final MonsterArchetype.Schema archetype) {
    final ValueStore valueStore = new ValueStore();
    final LimbsSetupsByArchetype<T> limbsSetupsByArchetype = new LimbsSetupsByArchetype<>();

    final MonsterArchetype.LimbValueGeneratorSetup<T> defaultArchetypeBindingBuilder =
        new ArchetypeLimbValueGeneratorSetupImpl<>(limbsSetupsByArchetype);
    final MonsterArchetype.ValueGeneratorBinding<T> archetypeValueGeneratorBinding =
        new ArchetypeValueGeneratorBindingImpl<>(limbsSetupsByArchetype, defaultArchetypeBindingBuilder);

    final CallHistory callHistory = new CallHistory();
    final RecordingInterceptor recordingInterceptor = new RecordingInterceptor(callHistory);
    final PlayingInterceptor playingInterceptor = new PlayingInterceptor(valueStore);

    final InterceptorModeCentral central =
        new InterceptorModeCentral(recordingInterceptor, playingInterceptor);
    final Interceptor interceptor = new Interceptor(central);

    final ValueGeneratorSorter generatorGatherer =
        new ValueGeneratorSorter(callHistory);
    final List<Monster.FallbackValueGenerator> fallbackValueGenerators =
        instantiate(builder.getDefaultGenerators());

    final MonsterLimbValueResolver valueResolver = new MonsterLimbValueResolver();

    central.startRecording();

    final Class monsterClass = retrieveTargetClass(builder);
    final T monster = (T) createMonster(monsterClass, interceptor);

    final Map<MonsterLimb.Schema, MonsterLimb.ValueGenerator> limbValueGenerators =
        indexValueGenerators(
            builder,
            archetype,
            limbsSetupsByArchetype,
            archetypeValueGeneratorBinding,
            generatorGatherer,
            monster);

    builder
        .getLimbs()
        .forEach(
            limb ->
                valueStore.set(
                    limb,
                    valueResolver.resolve(
                        builder, limbValueGenerators, fallbackValueGenerators, limb)));

    central.startPlaying();
    return monster;
  }
}
