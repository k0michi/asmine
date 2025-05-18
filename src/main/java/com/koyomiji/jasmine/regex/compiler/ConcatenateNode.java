package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;

import java.util.ArrayList;
import java.util.List;

public class ConcatenateNode extends AbstractRegexNode {
  public List<AbstractRegexNode> children;

  public ConcatenateNode(List<AbstractRegexNode> children) {
    this.children = children;
  }

  public ConcatenateNode(AbstractRegexNode... children) {
    this.children = ArrayListHelper.of(children);
  }

  @Override
  public void compile(RegexCompilerContext context) {
    for(AbstractRegexNode c : children) {
      c.compile(context);
    }
  }
}
