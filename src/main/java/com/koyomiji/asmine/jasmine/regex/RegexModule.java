package com.koyomiji.asmine.regex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegexModule {
  private Map<Integer, RegexFunction> functionMap = new HashMap<>();

  public RegexModule(List<RegexFunction> functions) {
    for (RegexFunction f : functions) {
      functionMap.put(f.getId(), f);
    }
  }

  public RegexFunction getFunction(int id) {
    return functionMap.get(id);
  }

  public boolean hasFunction(int id) {
    return functionMap.containsKey(id);
  }
}
