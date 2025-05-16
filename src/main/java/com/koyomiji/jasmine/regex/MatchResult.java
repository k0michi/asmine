package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.regex.compiler.RegexCompiler;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.Map;

public class MatchResult {
  private final RegexThread thread;

  public MatchResult(RegexThread thread) {
    this.thread = thread;
  }

  public Pair<Integer, Integer> getRange() {
    return thread.getBoundRange(RegexCompiler.BOUNDARY_KEY);
  }

  public Pair<Integer, Integer> getBound(Object key) {
    return thread.getBoundRange(key);
  }

  public Map<Object, Pair<Integer, Integer>> getStringBinds() {
    return thread.getStringBinds();
  }
}
