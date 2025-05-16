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
  public List<AbstractRegexInsn> compile(RegexCompilerContext context) {
    if (!context.bindMap.containsKey(key)) {
      throw new RegexCompilerException("Key not found: " + key);
    }

    ArrayList<AbstractRegexInsn> insns = new ArrayList<>();
    insns.add(new BoundBeginInsn());
    context.insideBound = true;
    insns.addAll(context.bindMap.get(key).child.compile(context));
    context.insideBound = false;
    insns.add(new BoundEndInsn(key));
    return insns;
  }
}
