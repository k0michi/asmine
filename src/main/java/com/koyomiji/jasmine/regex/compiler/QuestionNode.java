package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.ForkInsn;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.ArrayList;
import java.util.List;

public class QuestionNode extends AbstractQuantifierNode {
  public QuestionNode(AbstractRegexNode child) {
    super(child);
  }

  public QuestionNode(AbstractRegexNode child, QuantifierType type) {
    super(child, type);
  }

  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    List<AbstractRegexInsn> insns = new ArrayList<>();
    PseudoLabelInsn l0 = new PseudoLabelInsn();
    PseudoLabelInsn l1 = new PseudoLabelInsn();

    insns.add(type == QuantifierType.GREEDY
            ? new PseudoForkInsn(l0, l1)
            : new PseudoForkInsn(l1, l0));
    insns.add(l0);
    insns.addAll(child.compile(context));
    insns.add(l1);
    return insns;
  }
}
