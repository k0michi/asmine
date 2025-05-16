package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ForkInsn extends AbstractRegexInsn {
  public List<Integer> offsets;

  public ForkInsn(int offset0, int offset1) {
    offsets = ArrayListHelper.of(offset0, offset1);
  }

  public ForkInsn(List<Integer> offsets) {
    this.offsets = offsets;
  }

  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    List<RegexThread> threads = new ArrayList<>();

    for(int i = 0; i < offsets.size(); i++) {
      RegexThread cloned = (RegexThread)thread.clone();
      cloned.advanceProgramCounter(offsets.get(i));
      threads.add(cloned);
    }

    return Pair.of(true, threads);
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
