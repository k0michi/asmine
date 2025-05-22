package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.ForkInsn;
import com.koyomiji.asmine.regex.JumpInsn;
import com.koyomiji.asmine.regex.AbstractRegexInsn;

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
  public void compile(RegexCompilerContext context) {
    PseudoLabelInsn l0 = new PseudoLabelInsn();
    PseudoLabelInsn l1 = new PseudoLabelInsn();
    PseudoLabelInsn l2 = new PseudoLabelInsn();

    context.emit(l0);
    context.emit(type == QuantifierType.GREEDY
            ? new PseudoForkInsn(l1, l2)
            : new PseudoForkInsn(l2, l1));
    context.emit(l1);
    child.compile(context);
    context.emit(new PseudoJumpInsn(l0));
    context.emit(l2);
  }
}
