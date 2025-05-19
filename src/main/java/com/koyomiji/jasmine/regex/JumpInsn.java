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
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.advanceProgramCounter(offset);
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
