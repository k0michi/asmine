package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class JumpInsn extends AbstractRegexInsn {
  public int offset;

  public JumpInsn(int offset) {
    this.offset = offset;
  }

  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    RegexThread t = (RegexThread)thread.clone();
    t.advanceProgramCounter(offset);
    return Pair.of(true, ArrayListHelper.of(t));
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
