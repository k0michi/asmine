package com.koyomiji.jasmine.sexpr.tree;

import com.koyomiji.jasmine.common.SourceLocation;
import com.koyomiji.jasmine.sexpr.SExprVisitor;

public abstract class AbstractSExprNode extends SExprVisitor {
  public SourceLocation location;

  public AbstractSExprNode() {
    super(null);
  }

  public abstract void accept(SExprVisitor visitor);
}
