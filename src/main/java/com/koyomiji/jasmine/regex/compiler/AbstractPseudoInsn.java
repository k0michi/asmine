package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public abstract class AbstractPseudoInsn extends AbstractRegexInsn {
  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    throw new UnsupportedOperationException("PseudoInsn cannot be executed");
  }

  @Override
  public boolean isTransitive() {
    return false;
  }
}
