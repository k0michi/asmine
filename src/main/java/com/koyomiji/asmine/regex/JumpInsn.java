package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

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
