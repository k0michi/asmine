package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;

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
