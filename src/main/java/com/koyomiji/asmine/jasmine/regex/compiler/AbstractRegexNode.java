package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.AbstractRegexInsn;

import java.util.List;

public abstract class AbstractRegexNode {
  public abstract void compile(RegexCompilerContext context);
}
