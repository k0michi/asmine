package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.List;

public abstract class AbstractRegexNode {
  public abstract void compile(RegexCompilerContext context);
}
