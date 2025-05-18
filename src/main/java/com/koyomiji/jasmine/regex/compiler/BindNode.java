package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.BindBeginInsn;
import com.koyomiji.jasmine.regex.BindEndInsn;

import java.util.ArrayList;
import java.util.List;

public class BindNode extends AbstractRegexNode {
  public Object key;
  public AbstractRegexNode child;

  public BindNode(Object key, AbstractRegexNode child) {
    this.key = key;
    this.child = child;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    if (context.insideBound == 0) {
      if (context.bindMap.containsKey(key)) {
        throw new RegexCompilerException("Duplicate key: " + key);
      }

      context.bindMap.put(key, this);

      context.emit(new BindBeginInsn());
    }

    child.compile(context);

    if (context.insideBound == 0) {
      context.emit(new BindEndInsn(key));
    }
  }
}
