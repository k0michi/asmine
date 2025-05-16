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
    int offset = 1;
    List<Integer> offsets = new ArrayList<>();
    List<AbstractRegexInsn> insns = new ArrayList<>();

    for(AbstractRegexNode c : options) {
      offsets.add(offset);
      List<AbstractRegexInsn> compiled = c.compile(context);
      insns.add(null);
      insns.addAll(compiled);
      offset += compiled.size() + 1;
    }

    insns.set(0, new ForkInsn(offsets));

    for (int i = 1; i < offsets.size(); i++) {
      int offsetValue = offsets.get(i) - 1;
      insns.set(offsetValue, new JumpInsn(insns.size() - offsetValue));
    }

    return insns;
  }
}