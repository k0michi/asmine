package com.koyomiji.jasmine.regex.string;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;
import com.koyomiji.jasmine.tuple.Pair;

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
