package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class AnyInsn extends AbstractRegexInsn {
  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    thread.advanceProgramCounter();
    return Pair.of(true, ArrayListHelper.of(thread));
  }

  @Override
  public boolean isTransitive() {
    return false;
  }
}
