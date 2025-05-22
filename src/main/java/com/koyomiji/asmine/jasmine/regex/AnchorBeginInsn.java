package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class AnchorBeginInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    if (processor.getStringPointer() == 0) {
      thread.advanceProgramCounter();
      return ArrayListHelper.of(thread);
    } else {
      return  ArrayListHelper.of();
    }
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
