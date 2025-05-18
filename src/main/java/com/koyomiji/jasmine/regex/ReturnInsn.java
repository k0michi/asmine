package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class ReturnInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    if (thread.stackSize() > 0) {
      int fp = (Integer)thread.pop();
      thread.setFunctionPointer(fp);
      int pc = (Integer)thread.pop();
      thread.setProgramCounter(pc);

      return ArrayListHelper.of(thread);
    }

    thread.terminate();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
