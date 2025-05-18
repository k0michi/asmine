package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.ForkInsn;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.JumpInsn;

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
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    List<AbstractRegexInsn> insns = new ArrayList<>();
    List<PseudoLabelInsn>  labels = new ArrayList<>();
    PseudoLabelInsn end = new PseudoLabelInsn();

    for (int i = 0; i < options.size(); i++) {
      labels.add(new PseudoLabelInsn());
    }

    insns.add(new PseudoForkInsn(labels));

    for (int i = 0; i < options.size(); i++) {
      insns.add(labels.get(i));
      insns.addAll(options.get(i).compile(context));

      if (i + 1 < options.size()) {
        insns.add(new PseudoJumpInsn(end));
      }
    }

    insns.add(end);
    return insns;
  }
}