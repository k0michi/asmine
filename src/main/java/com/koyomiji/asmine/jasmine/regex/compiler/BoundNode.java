package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.*;

import java.util.ArrayList;
import java.util.List;

public class BoundNode extends AbstractRegexNode {
  public Object key;

  public BoundNode(Object key) {
    this.key = key;
  }

  @Override
  public void compile(RegexCompilerContext context) {
    context.emit(new BoundBeginInsn());
    context.pushBound();
    context.getBindNode(key).compile(context);
    context.popBound();
    context.emit(new BoundEndInsn(key));
  }
}
