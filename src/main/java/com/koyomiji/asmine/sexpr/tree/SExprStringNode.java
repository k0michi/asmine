package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.Objects;

public class SExprStringNode extends AbstractSExprNode {
  public String value;

  public SExprStringNode() {
    super();
  }

  public SExprStringNode(String value) {
    super();
    this.value = value;
  }

  @Override
  public void visitString(String value) {
    this.value = value;
    super.visitString(value);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    visitor.visitString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprStringNode that = (SExprStringNode) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
