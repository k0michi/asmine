package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.SkipPseudoInsn;

public class SkipPseudoNode extends AbstractRegexNode {
  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new SkipPseudoInsn());
  }
}
