package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.sexpr.IFormattable;
import com.koyomiji.asmine.sexpr.SExprVisitor;

public class SExprLineBreakNode extends AbstractSExprNode {
  @Override
  public void accept(SExprVisitor visitor) {
    if (visitor instanceof IFormattable){
      ((IFormattable) visitor).visitLineBreak();
    }
  }
}
