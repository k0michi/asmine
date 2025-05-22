package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class PushInsn extends AbstractRegexInsn {
  private final Object operand;

  public PushInsn(Object operand) {
    this.operand = operand;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.push(operand);
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
