package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.AnyInsn;

import java.util.List;

public class AnyNode extends AbstractRegexNode {
  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new AnyInsn());
  }
}
