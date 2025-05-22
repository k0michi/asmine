package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.ForkInsn;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.JumpInsn;

import java.util.ArrayList;
import java.util.List;

public class AlternateNode extends AbstractRegexNode {
  public List<AbstractRegexNode> options;

  public AlternateNode(List<AbstractRegexNode> options) {
    this.options = options;
  }

  public AlternateNode(AbstractRegexNode... options) {
    this.options = ArrayListHelper.of(options);
  }

  @Override
  public void compile(RegexCompilerContext context) {
    List<PseudoLabelInsn>  labels = new ArrayList<>();
    PseudoLabelInsn end = new PseudoLabelInsn();

    for (int i = 0; i < options.size(); i++) {
      labels.add(new PseudoLabelInsn());
    }

    context.emit(new PseudoForkInsn(labels));

    for (int i = 0; i < options.size(); i++) {
      context.emit(labels.get(i));
      options.get(i).compile(context);

      if (i + 1 < options.size()) {
        context.emit(new PseudoJumpInsn(end));
      }
    }

    context.emit(end);
  }
}