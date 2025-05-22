package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.regex.compiler.RegexCompiler;
import com.koyomiji.asmine.tuple.Pair;

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
