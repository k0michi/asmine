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
    int offset = 1;
    List<AbstractRegexInsn> insns = new ArrayList<>();
    insns.add(null);
    List<AbstractRegexInsn> compiled = child.compile(context);
    insns.addAll(compiled);
    offset += compiled.size();
    insns.set(0,
            type == QuantifierType.GREEDY
                    ? new ForkInsn(1, offset)
                    : new ForkInsn(offset, 1));
    return insns;
  }
}
