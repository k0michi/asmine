package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class ReturnInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    if (thread.stackSize() > 0) {
      int fp = (Integer)thread.pop();
      thread.setFunctionPointer(fp);
      int pc = (Integer)thread.pop();
      thread.setProgramCounter(pc + 1);

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
