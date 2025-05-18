package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class ReturnInsn extends AbstractRegexInsn {
  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    if (thread.stackSize() > 0) {
      int fp = (Integer)thread.pop();
      thread.setFunctionPointer(fp);
      int pc = (Integer)thread.pop();
      thread.setProgramCounter(pc);

      return Pair.of(true, ArrayListHelper.of(thread));
    }

    thread.terminate();
    return Pair.of(true, ArrayListHelper.of(thread));
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
