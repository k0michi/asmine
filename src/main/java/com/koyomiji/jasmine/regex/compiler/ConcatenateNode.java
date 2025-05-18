package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.InvokeInsn;
import com.koyomiji.jasmine.regex.ReturnInsn;

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
    if (context.hasConcatFunction(this)){
      int id = context.getConcatFunction(this);
      context.emit(new InvokeInsn(id));
    } else {
      RegexCompilerContext newContext = context.newConcatFunctionContext(this);

      for(AbstractRegexNode c : children) {
        c.compile(newContext);
      }

      newContext.emit(new ReturnInsn());

      context.emit(new InvokeInsn(newContext.getFunction()));
    }
  }
}
