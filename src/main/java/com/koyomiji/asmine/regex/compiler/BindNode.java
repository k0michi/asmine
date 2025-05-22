package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.BindBeginInsn;
import com.koyomiji.asmine.regex.BindEndInsn;

public class BindNode extends AbstractRegexNode {
  public Object key;
  public AbstractRegexNode child;

  public BindNode(Object key, AbstractRegexNode child) {
    this.key = key;
    this.child = child;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    if (!context.isInsideBound()) {
      context.setBindNode(key, this);

      context.emit(new BindBeginInsn());
    }

    child.compile(context);

    if (!context.isInsideBound()) {
      context.emit(new BindEndInsn(key));
    }
  }
}
