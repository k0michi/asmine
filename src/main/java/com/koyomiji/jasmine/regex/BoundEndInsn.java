package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.List;

public class BoundEndInsn extends AbstractRegexInsn {
  public Object key;

  public BoundEndInsn(Object key) {
    this.key = key;
  }

  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    if (thread.stackSize() == 0) {
      return Pair.of(false, ArrayListHelper.of());
    }

    Pair<Integer, Integer> range = Pair.of((Integer) thread.pop(), processor.getStringPointer());
    Pair<Integer, Integer> bound = thread.getBoundRange(key);

    if(bound != null && processor.compareSubstrings(bound, range)) {
      // Succeeded to match the previous appearance
      thread.advanceProgramCounter();
      return Pair.of(true, ArrayListHelper.of(thread));
    } else {
      // Failed to match
      return Pair.of(false, ArrayListHelper.of());
    }
  }

  @Override
  public boolean isTransitive() {
    return true;
  }
}
