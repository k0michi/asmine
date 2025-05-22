package com.koyomiji.jasmine.sexpr.tree;

import com.koyomiji.jasmine.sexpr.SExprVisitor;

import java.util.Objects;

public class SExprSymbolNode extends AbstractSExprNode {
  public String value;

  public SExprSymbolNode() {
    super();
  }

  public SExprSymbolNode(String value) {
    super();
    this.value = value;
  }

  @Override
  public void visitSymbol(String value) {
    this.value = value;
    super.visitSymbol(value);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    visitor.visitSymbol(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprSymbolNode that = (SExprSymbolNode) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
