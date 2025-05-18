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
    PseudoLabelInsn l0 = new PseudoLabelInsn();
    PseudoLabelInsn l1 = new PseudoLabelInsn();

    insns.add(l0);
    insns.addAll(compiled);
    insns.add(type == QuantifierType.GREEDY
            ? new PseudoForkInsn(l0, l1)
            : new PseudoForkInsn(l1, l0));
    insns.add(l1);

    return insns;
  }
}
