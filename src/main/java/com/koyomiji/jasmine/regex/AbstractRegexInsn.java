package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public abstract class AbstractRegexInsn {
  public abstract Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread);

  public abstract boolean isTransitive();
}
