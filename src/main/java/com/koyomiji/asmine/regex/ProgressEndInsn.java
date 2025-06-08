package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.List;

public class ProgressEndInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.pop();
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
