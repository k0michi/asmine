package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.ForkInsn;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.ArrayList;
import java.util.List;

public class PlusNode extends AbstractQuantifierNode {
  public PlusNode(AbstractRegexNode child) {
    super(child);
  }

  public PlusNode(AbstractRegexNode child, QuantifierType type) {
    super(child, type);
  }

  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    List<AbstractRegexInsn> insns = new ArrayList<>();
    List<AbstractRegexInsn> compiled = child.compile(context);
    insns.addAll(compiled);
    insns.add(type == QuantifierType.GREEDY
            ? new ForkInsn(-(compiled.size()), 1)
            : new ForkInsn(1, -(compiled.size())));
    return insns;
  }
}
