package com.koyomiji.jasmine.sexpr.tree;

import com.koyomiji.jasmine.sexpr.IFormattable;
import com.koyomiji.jasmine.sexpr.SExprVisitor;

public class SExprLineBreakNode extends AbstractSExprNode {
  @Override
  public void accept(SExprVisitor visitor) {
    if (visitor instanceof IFormattable){
      ((IFormattable) visitor).visitLineBreak();
    }
  }
}
