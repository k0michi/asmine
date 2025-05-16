package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class AnchorEndInsn extends AbstractRegexInsn {
  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    if (processor.getStringPointer() == processor.getStringLength()) {
      thread.advanceProgramCounter();
      return Pair.of(true, ArrayListHelper.of(thread));
    } else {
      return Pair.of(false, ArrayListHelper.of());
    }
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
