package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.*;

public class RegexCompilerContext {
  private Map<Object, BindNode> bindMap = new HashMap<>();
  private int insideBound = 0;
  private List<AbstractRegexInsn> insns = new ArrayList<>();

  public void emit(AbstractRegexInsn insn) {
    insns.add(insn);
  }

  public void pushBound() {
    insideBound++;
  }

  public void popBound() {
    insideBound--;
  }

  public boolean isInsideBound() {
    return insideBound > 0;
  }

  public void setBindNode(Object key, BindNode node) {
    if (bindMap.containsKey(key)) {
      throw new RegexCompilerException("Duplicate key: " + key);
    }

    bindMap.put(key, node);
  }

  public BindNode getBindNode(Object key) {
    if (!bindMap.containsKey(key)) {
      throw new RegexCompilerException("Key not found: " + key);
    }

    return bindMap.get(key);
  }

  public List<AbstractRegexInsn> getInsns() {
    return insns;
  }
}
