package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.AnchorBeginInsn;

import java.util.List;

public class AnchorBeginNode extends AbstractRegexNode {
  @Override
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    return ArrayListHelper.of(new AnchorBeginInsn());
  }
}
