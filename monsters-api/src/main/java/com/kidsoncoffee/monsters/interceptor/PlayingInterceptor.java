package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

import static com.kidsoncoffee.monsters.MonsterMemberSchemaParse.parse;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class PlayingInterceptor implements MethodInterceptor {

  private final Map<MonsterMember.Schema, Object> values;

  public PlayingInterceptor(final Map<MonsterMember.Schema, Object> values) {
    this.values = values;
  }

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    final MonsterMember.Schema member = parse(method);

    return values.getOrDefault(member, null);
  }
}
