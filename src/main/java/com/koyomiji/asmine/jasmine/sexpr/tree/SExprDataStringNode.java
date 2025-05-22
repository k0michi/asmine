package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.Arrays;
import java.util.Objects;

public class SExprDataStringNode extends AbstractSExprNode {
  public byte[] value;

  public SExprDataStringNode(byte[] value) {
    this.value = value;
  }

  @Override
  public void visitDataString(byte[] value) {
    this.value = value;
    super.visitDataString(value);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    visitor.visitDataString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprDataStringNode that = (SExprDataStringNode) o;
    return Objects.deepEquals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(value);
  }
}
