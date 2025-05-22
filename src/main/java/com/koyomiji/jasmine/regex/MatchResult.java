package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.regex.compiler.RegexCompiler;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;
import java.util.Map;

public class MatchResult {
  private final RegexThread thread;

  public MatchResult(RegexThread thread) {
    this.thread = thread;
  }

  public List<Pair<Integer, Integer>> getBounds(Object key) {
    return thread.getBounds().get(key);
  }

  public Map<Object, List<Pair<Integer, Integer>>> getBounds() {
    return thread.getBounds();
  }
}
