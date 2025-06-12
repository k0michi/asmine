package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class ReturnInsn extends AbstractRegexInsn {
  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    CallFrame popped = thread.popCall(processor.getStringPointer());
    thread.setFunctionPointer(popped.returnFP);
    thread.setProgramCounter(popped.returnPC + 1);

    if (popped.returnFP == -1) {
      thread.terminate();
    }

    return ArrayListHelper.of(thread);
  }

  @Override
  public int getExecutionType() {
    return TRANSITIVE;
  }
}
