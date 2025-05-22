package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.common.SourceLocation;
import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.Objects;

public class SExprNodeContainer extends SExprVisitor {
  public AbstractSExprNode child;
  private SourceLocation lastLocation = null;

  @Override
  public void visitSymbol(String value) {
    child = new SExprSymbolNode(value);
    child.location = lastLocation;
    lastLocation = null;
  }

  @Override
  public void visitString(String value) {
    child = new SExprStringNode(value);
    child.location = lastLocation;
    lastLocation = null;
  }

  @Override
  public void visitInteger(long value) {
    child = new SExprIntegerNode(value);
    child.location = lastLocation;
    lastLocation = null;
  }

  @Override
  public void visitFloatingPoint(double value) {
    child = new SExprFloatingPointNode(value);
    child.location = lastLocation;
    lastLocation = null;
  }

  @Override
  public SExprVisitor visitList() {
    child = new SExprListNode();
    child.location = lastLocation;
    lastLocation = null;
    return child;
  }

  @Override
  public void visitSourceLocation(SourceLocation location) {
    this.lastLocation = location;
    super.visitSourceLocation(location);
  }

  @Override
  public void visitEnd() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprNodeContainer sExprNodeContainer = (SExprNodeContainer) o;
    return Objects.equals(child, sExprNodeContainer.child);
  }

  @Override
  public int hashCode() {
    return Objects.hash(child);
  }

  public void accept(SExprVisitor visitor) {
    child.accept(visitor);
  }
}
