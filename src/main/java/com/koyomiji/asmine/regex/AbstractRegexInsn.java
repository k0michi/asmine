package com.koyomiji.asmine.regex;

import java.util.List;

public abstract class AbstractRegexInsn {
  public static final int CONSUMING = 0;
  public static final int TRANSITIVE = 1;
  public static final int BOUNDARY = 2;

  public abstract List<RegexThread> execute(RegexProcessor processor, RegexThread thread);

  public abstract int getExecutionType();
}
