package com.github.kidsoncoffee.monsters.interaction;

import com.github.kidsoncoffee.monsters.MonsterLimb;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class CallHistory {
  private final AtomicReference<MonsterLimb.Schema> lastCall;

  public CallHistory() {
    this.lastCall = new AtomicReference<>();
  }

  public void add(MonsterLimb.Schema memberSchema) {
    this.lastCall.set(memberSchema);
  }

  public MonsterLimb.Schema getLastCall() {
    return this.lastCall.getAndSet(null);
  }
}
