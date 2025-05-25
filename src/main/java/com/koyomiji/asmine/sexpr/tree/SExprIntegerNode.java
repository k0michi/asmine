package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.Objects;

public class SExprIntegerNode extends AbstractSExprNode {
  public long value;

  public SExprIntegerNode(long value) {
    this.value = value;
  }

  @Override
  public void visitInteger(long value) {
    this.value = value;
    super.visitInteger(value);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    visitor.visitInteger(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprIntegerNode that = (SExprIntegerNode) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
