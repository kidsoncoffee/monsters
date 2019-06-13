package com.github.kidsoncoffee.monsters.interception;

import com.github.kidsoncoffee.monsters.MonsterLimb;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static com.github.kidsoncoffee.monsters.MonsterMemberSchemaParser.parse;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class PlayingInterceptor implements MethodInterceptor {

  private final ValueStore valueStore;

  public PlayingInterceptor(ValueStore valueStore) {
    this.valueStore = valueStore;
  }

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    final MonsterLimb.Schema member = parse(method);

    return this.valueStore
        .get(member)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    String.format("Unable to find value for '%s'.", member.getName())));
  }
}
