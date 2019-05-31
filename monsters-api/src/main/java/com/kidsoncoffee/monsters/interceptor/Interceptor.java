package com.kidsoncoffee.monsters.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class Interceptor implements MethodInterceptor {

  private final InterceptorModeCentral central;

  public Interceptor(final InterceptorModeCentral central) {
    this.central = central;
  }

  @Override
  public Object intercept(
      final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy)
      throws Throwable {
    final Optional<MethodInterceptor> interceptor = central.getMode();

    if (interceptor.isPresent()) {
      return interceptor.get().intercept(o, method, objects, methodProxy);
    }

    // LOG THIS SHIT
    return null;
  }
}
