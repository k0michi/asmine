package com.koyomiji.asmine.regex;

import java.util.List;

public class RegexFunction {
  public int id;
  public List<AbstractRegexInsn> insns;

  public RegexFunction(int id, List<AbstractRegexInsn> insns) {
    this.id = id;
    this.insns = insns;
  }

  public int getId() {
    return id;
  }
}
