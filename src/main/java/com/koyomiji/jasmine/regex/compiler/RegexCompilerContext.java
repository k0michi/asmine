package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.*;

public class RegexCompilerContext {
  public Map<Object, BindNode> bindMap = new HashMap<>();
  public int insideBound = 0;
  public List<AbstractRegexInsn> insns = new ArrayList<>();

  public void emit(AbstractRegexInsn insn) {
    insns.add(insn);
  }
}
