package com.koyomiji.asmine.regex;

import java.util.List;

public abstract class AbstractRegexInsn {
  public abstract List<RegexThread> execute(RegexProcessor processor, RegexThread thread);

  public abstract boolean isTransitive();
}
