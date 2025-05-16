package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class BindEndInsn extends AbstractRegexInsn {
  public Object key;

  public BindEndInsn(Object key) {
    this.key = key;
  }

  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    if (thread.stackSize() == 0) {
      return Pair.of(false, ArrayListHelper.of());
    }

    Pair<Integer, Integer> range = Pair.of((Integer) thread.pop(), processor.getStringPointer());

    thread.bind(key, range);
    thread.advanceProgramCounter();
    return Pair.of(true, ArrayListHelper.of(thread));
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
