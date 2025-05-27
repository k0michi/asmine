package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class BindEndInsn extends AbstractRegexInsn {
  public Object key;

  public BindEndInsn(Object key) {
    this.key = key;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.endBind(processor.getStringPointer());
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
