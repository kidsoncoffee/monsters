package com.kidsoncoffee.monsters;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public final class MonsterMemberCallHistory {
  private MonsterMember.Schema lastCalledMember;

  public MonsterMember.Schema getLastCalledMember() {
    return this.lastCalledMember;
  }

  public void setLastCalledMember(final MonsterMember.Schema member) {
    this.lastCalledMember = member;
  }
}
