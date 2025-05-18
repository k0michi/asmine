package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.*;

public class RegexCompilerContext {
  public Map<Object, BindNode> bindMap = new HashMap<>();
  public boolean insideBound = false;
  public List<AbstractRegexInsn> insns = new ArrayList<>();

  public void emit(AbstractRegexInsn insn) {
    insns.add(insn);
  }
}
