package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.List;

public class InvokeInsn extends AbstractRegexInsn {
  public int functionPointer;

  public InvokeInsn(int functionPointer) {
    this.functionPointer = functionPointer;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    thread.push(thread.getProgramCounter());
    thread.push(thread.getFunctionPointer());
    thread.setFunctionPointer(functionPointer);
    thread.setProgramCounter(0);
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
