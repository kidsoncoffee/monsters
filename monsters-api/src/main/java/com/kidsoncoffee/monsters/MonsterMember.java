package com.kidsoncoffee.monsters;

import com.kidsoncoffee.monsters.interceptor.CallHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public @interface MonsterMember {

  // MOVE THIS AND RENAME TO MEMBER
  // THIS IS NOT PUBLIC API
  interface Schema {
    String getName();

    List<String> getParameters();

    boolean isMethod();

    boolean isField();

    boolean isPublic();

    boolean isPackagePrivate();
  }

  @FunctionalInterface
  interface ValueGenerator<T> {
    T generate();
  }

  //TODO fchovich MAKE THIS AN INTERFACE
  class ValueBinder {

    private final Map<Schema, ValueGenerator> generatorsByMethod = new HashMap<>();

    private final CallHistory callHistory;

    public ValueBinder(final CallHistory callHistory) {
      this.callHistory = callHistory;
    }

    public boolean constainsValue(final Schema member) {
      return this.generatorsByMethod.containsKey(member.getName());
    }

    public MonsterMember.ValueGenerator getValue(final Schema member) {
      return this.generatorsByMethod.get(member);
    }

    public final <X> void generate(
        final X nothingtolookhere, final MonsterMember.ValueGenerator<X> valueGenerator) {
      ValueBinder.this.generatorsByMethod.put(
          ValueBinder.this.callHistory.getLastCall(), valueGenerator);
    }

    public final <X> void stub(final X nothingtolookhere, final X fixedValue) {
      this.generate(nothingtolookhere, () -> fixedValue);
    }
  }
}
