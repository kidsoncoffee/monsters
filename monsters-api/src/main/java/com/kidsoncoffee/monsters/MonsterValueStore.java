package com.kidsoncoffee.monsters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterValueStore {

  private ValueGeneratorStore valueGeneratorStore = new ValueGeneratorStore();

  private Map<MonsterMember.Schema, Object> values = new HashMap<>();


}
