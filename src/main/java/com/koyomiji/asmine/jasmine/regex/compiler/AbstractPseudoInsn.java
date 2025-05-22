package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.RegexProcessor;
import com.koyomiji.asmine.regex.RegexThread;

import java.util.List;

public abstract class AbstractPseudoInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    throw new UnsupportedOperationException("PseudoInsn cannot be executed");
  }

  @Override
  public boolean isTransitive() {
    return false;
  }
}
