package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.ForkInsn;
import com.koyomiji.asmine.regex.AbstractRegexInsn;

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
  public void compile(RegexCompilerContext context) {
    PseudoLabelInsn l0 = new PseudoLabelInsn();
    PseudoLabelInsn l1 = new PseudoLabelInsn();

    context.emit(type == QuantifierType.GREEDY
            ? new PseudoForkInsn(l0, l1)
            : new PseudoForkInsn(l1, l0));
    context.emit(l0);
    child.compile(context);
    context.emit(l1);
  }
}
