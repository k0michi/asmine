package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class BoundBeginInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.push(processor.getStringPointer());
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
