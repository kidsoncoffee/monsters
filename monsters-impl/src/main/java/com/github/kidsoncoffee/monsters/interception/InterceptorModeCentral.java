package com.github.kidsoncoffee.monsters.interception;

import net.sf.cglib.proxy.MethodInterceptor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class InterceptorModeCentral {

  private final MethodInterceptor recordingInterceptor;

  private final MethodInterceptor playingInterceptor;

  private AtomicReference<MethodInterceptor> currentInterceptor;

  public InterceptorModeCentral(
      final MethodInterceptor recordingInterceptor, final MethodInterceptor playingInterceptor) {
    this.recordingInterceptor = recordingInterceptor;
    this.playingInterceptor = playingInterceptor;
    this.currentInterceptor = new AtomicReference<>();
  }

  public Optional<MethodInterceptor> getMode() {
    return Optional.ofNullable(this.currentInterceptor.get());
  }

  public void startRecording() {
    this.currentInterceptor.set(this.recordingInterceptor);
  }

  public void startPlaying() {
    this.currentInterceptor.set(this.playingInterceptor);
  }
}
