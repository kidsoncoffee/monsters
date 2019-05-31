package com.kidsoncoffee.monsters.interceptor;

import com.kidsoncoffee.monsters.MonsterMember;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class CallHistory {
  private final AtomicReference<MonsterMember.Schema> lastCall = new AtomicReference<>();

  public void add(MonsterMember.Schema memberSchema) {
    this.lastCall.set(memberSchema);
  }

  public MonsterMember.Schema getLastCall() {
    return this.lastCall.get();
  }
}
