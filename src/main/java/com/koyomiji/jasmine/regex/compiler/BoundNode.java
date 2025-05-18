package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.*;

import java.util.ArrayList;
import java.util.List;

public class BoundNode extends AbstractRegexNode {
  public Object key;

  public BoundNode(Object key) {
    this.key = key;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    if (!context.bindMap.containsKey(key)) {
      throw new RegexCompilerException("Key not found: " + key);
    }

    context.emit(new BoundBeginInsn());
    context.insideBound++;
    context.bindMap.get(key).child.compile(context);
    context.insideBound--;
    context.emit(new BoundEndInsn(key));
  }
}
