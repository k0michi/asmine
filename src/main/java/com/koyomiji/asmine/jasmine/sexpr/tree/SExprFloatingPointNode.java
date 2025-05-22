package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.Objects;

public class SExprFloatingPointNode extends AbstractSExprNode {
  public double value;

  public SExprFloatingPointNode(double value) {
    this.value = value;
  }

  @Override
  public void visitFloatingPoint(double value) {
    this.value = value;
    super.visitFloatingPoint(value);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    visitor.visitFloatingPoint(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprFloatingPointNode that = (SExprFloatingPointNode) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
