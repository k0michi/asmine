package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.List;

public class ProgressBeginInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    // Ensure that the child regex is executed at least once, by subtracting 1
    thread.push(processor.getStringPointer() - 1);
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public int getExecutionType() {
    return TRANSITIVE;
  }
}
