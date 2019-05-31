package com.kidsoncoffee.monsters;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterMethodInterceptor implements MethodInterceptor {

  private final Class monsterClass;

  private final Class<? extends Monster.Archetype> archetypeClass;

  private final MonsterValueStore valueStore;

  private final MonsterMember.ValueBinder valueBinder;

  private final MonsterMemberCallHistory methodMonsterMemberCallHistory;

  private final ValueGeneratorStore valueGeneratorStore;

  private final AtomicBoolean recording;

  public MonsterMethodInterceptor(
      final Class monsterClass,
      final Class<? extends Monster.Archetype> archetypeClass,
      final MonsterValueStore valueStore,
      final MonsterMember.ValueBinder valueBinder,
      final MonsterMemberCallHistory methodMonsterMemberCallHistory,
      ValueGeneratorStore valueGeneratorStore) {
    this.monsterClass = monsterClass;
    this.archetypeClass = archetypeClass;
    this.valueStore = valueStore;
    this.valueBinder = valueBinder;
    this.methodMonsterMemberCallHistory = methodMonsterMemberCallHistory;
    this.valueGeneratorStore = valueGeneratorStore;
    this.recording = new AtomicBoolean(false);
  }

  @Override
  public synchronized Object intercept(
      final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy) {
    final MonsterMember.Schema member = parseMonsterMemberSchema(method);

    if (recording.get()) {
      // send to the store if
      this.valueGeneratorStore.create(
          monsterClass, archetypeClass, member, this.valueBinder.getValue(member));
    } else {
      // retrieve from values
      //return this.values.get(member);
      return null;
    }

    /*if (this.values.containsKey(member)) {
      return this.values.get(member);
    } else if (this.recording.get()) {
      if (this.valueBinder.constainsValue(member)) {
        final Object value = this.valueBinder.getValue(member);
        this.values.put(member, value);
        return value;
      }

      this.methodMonsterMemberCallHistory.setLastCalledMember(member);
    }*/

    if (String.class.isAssignableFrom(method.getReturnType())) {
      return MonsterNoises.random().toString();
    }
    return null;
  }

  private static MonsterMember.Schema parseMonsterMemberSchema(final Method method) {
    final String memberName = method.getName();
    final boolean publicAccess = Modifier.isPublic(method.getModifiers());
    final boolean packagePrivateAccess =
        !Modifier.isPrivate(method.getModifiers())
            && !Modifier.isPublic(method.getModifiers())
            && !Modifier.isProtected(method.getModifiers());
    return new ImmutableMonsterMemberSchema(
        memberName, memberParameters(method), true, false, publicAccess, packagePrivateAccess);
  }

  private static List<String> memberParameters(final Method method) {
    return Arrays.stream(method.getParameters())
        .map(Parameter::getName)
        .collect(Collectors.toList());
  }

  public void startRecording() {
    this.recording.set(true);
  }

  public void stopRecording() {
    this.recording.set(false);
  }
}
