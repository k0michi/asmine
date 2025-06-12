package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class BoundBeginInsn extends AbstractRegexInsn {
  public Object key;

  public BoundBeginInsn(Object key) {
    this.key = key;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.push(key);
    thread.push(processor.getStringPointer());
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public int getExecutionType() {
    return TRANSITIVE;
  }
}
