package com.github.kidsoncoffee.monsters.interception;

import com.github.kidsoncoffee.monsters.MonsterLimb;
import com.github.kidsoncoffee.monsters.interaction.CallHistory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static com.github.kidsoncoffee.monsters.MonsterMemberSchemaParser.parse;

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
    if (method.getName().equals("toString")) {
      return null;
    }

    final MonsterLimb.Schema memberSchema = parse(method);
    this.callHistory.add(memberSchema);

    // TODO fchovich FAIL OR LOG WHEN METHOD WITH PARAMETERS

    return null;
  }
}
