package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.AnchorEndInsn;

import java.util.List;

public class AnchorEndNode extends AbstractRegexNode {
  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new AnchorEndInsn());
  }
}
