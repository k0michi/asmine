package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.List;

public class ProgressInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    Integer sp = (Integer) thread.pop();

    // Avoid consuming empty string more than once in the loop
    if (sp == processor.getStringPointer()) {
      return ArrayListHelper.of();
    }

    thread.push(processor.getStringPointer());
    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public int getExecutionType() {
    return TRANSITIVE;
  }
}
