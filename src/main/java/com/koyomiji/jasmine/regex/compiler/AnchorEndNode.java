package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.AnchorEndInsn;

import java.util.List;

public class AnchorEndNode extends AbstractRegexNode {
  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    return ArrayListHelper.of(new AnchorEndInsn());
  }
}
