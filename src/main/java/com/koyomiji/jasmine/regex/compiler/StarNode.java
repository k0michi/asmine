package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.ForkInsn;
import com.koyomiji.jasmine.regex.JumpInsn;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.ArrayList;
import java.util.List;

public class StarNode extends AbstractQuantifierNode {
  public StarNode(AbstractRegexNode child) {
    super(child);
  }

  public StarNode(AbstractRegexNode child, QuantifierType type) {
    super(child, type);
  }

  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    List<AbstractRegexInsn> insns = new ArrayList<>();
    PseudoLabelInsn l0 = new PseudoLabelInsn();
    PseudoLabelInsn l1 = new PseudoLabelInsn();
    PseudoLabelInsn l2 = new PseudoLabelInsn();

    insns.add(l0);
    insns.add(type == QuantifierType.GREEDY
            ? new PseudoForkInsn(l1, l2)
            : new PseudoForkInsn(l2, l1));
    insns.add(l1);
    insns.addAll(child.compile(context));
    insns.add(new PseudoJumpInsn(l0));
    insns.add(l2);
    return insns;
  }
}
