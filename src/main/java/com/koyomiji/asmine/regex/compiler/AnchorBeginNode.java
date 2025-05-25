package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.AnchorBeginInsn;

import java.util.List;

public class AnchorBeginNode extends AbstractRegexNode {
  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new AnchorBeginInsn());
  }
}
