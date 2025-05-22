package com.koyomiji.asmine.regex.string;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.RegexProcessor;
import com.koyomiji.asmine.regex.RegexThread;
import com.koyomiji.asmine.tuple.Pair;

import java.util.List;

public class CharLiteralInsn extends AbstractRegexInsn {
  public char literal;

  public CharLiteralInsn(char literal) {
    this.literal = literal;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    if (!processor.compareCurrentCharToLiteral(literal)) {
      return ArrayListHelper.of();
    }

    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return false;
  }
}
