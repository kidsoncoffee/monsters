package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.CallHistory;
import com.kidsoncoffee.monsters.interceptor.Interceptor;
import com.kidsoncoffee.monsters.interceptor.InterceptorModeCentral;
import com.kidsoncoffee.monsters.interceptor.PlayingInterceptor;
import com.kidsoncoffee.monsters.interceptor.RecordingInterceptor;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Spawner<T> {

  public T spawn(final MonsterBuilder<T> builder) {
    final CallHistory callHistory = new CallHistory();
    final MonsterMember.ValueBinder valueBinder = new MonsterMember.ValueBinder(callHistory);
    final RecordingInterceptor recordingInterceptor = new RecordingInterceptor(callHistory);

    //TODO fchovich USE VALUE STORE
    final PlayingInterceptor playingInterceptor = new PlayingInterceptor(new HashMap<>());

    final InterceptorModeCentral central = new InterceptorModeCentral(recordingInterceptor, playingInterceptor);
    final Interceptor interceptor = new Interceptor(central);

    central.startRecording();

    final Class monsterClass = this.getTargetClass(builder);
    final T monster = this.createMonster(monsterClass, interceptor);

    final Monster.Setup<T> setup = builder.getSetup();
    setup.setup(valueBinder, monster);

    central.startPlaying();

    // COLLECT VALUES

    return monster;
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
