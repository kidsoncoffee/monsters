package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static com.kidsoncoffee.monsters.MonsterMemberSchemaParse.parse;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class RecordingInterceptor implements MethodInterceptor {

  private final CallHistory callHistory;

  // CREATE ABSTRACT CLASS FOR THESE DETAILS
  public RecordingInterceptor(final CallHistory callHistory) {
    this.callHistory = callHistory;
  }

  @Override
  public Object intercept(
      final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy)
      throws Throwable {
    final MonsterMember.Schema memberSchema = parse(method);
    this.callHistory.add(memberSchema);

    // TODO fchovich FAIL OR LOG WHEN METHOD WITH PARAMETERS

    return null;
  }
}
