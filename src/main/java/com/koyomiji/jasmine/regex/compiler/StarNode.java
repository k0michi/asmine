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
    int offset = 1;
    List<AbstractRegexInsn> insns = new ArrayList<>();
    insns.add(null);
    List<AbstractRegexInsn> compiled = child.compile(context);
    insns.addAll(compiled);
    insns.add(new JumpInsn(-(compiled.size() + 1)));
    offset += compiled.size() + 1;
    insns.set(0,
            type == QuantifierType.GREEDY
                    ? new ForkInsn(1, offset)
                    : new ForkInsn(offset, 1));
    return insns;
  }
}
