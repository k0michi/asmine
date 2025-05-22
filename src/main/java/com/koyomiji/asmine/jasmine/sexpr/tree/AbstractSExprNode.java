package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.common.SourceLocation;
import com.koyomiji.asmine.sexpr.SExprVisitor;

public abstract class AbstractSExprNode extends SExprVisitor {
  public SourceLocation location;

  public AbstractSExprNode() {
    super(null);
  }

  public abstract void accept(SExprVisitor visitor);
}
