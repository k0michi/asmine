package com.koyomiji.asmine.sexpr.tree;

import com.koyomiji.asmine.common.SourceLocation;
import com.koyomiji.asmine.sexpr.SExprVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SExprListNode extends AbstractSExprNode {
  public List<AbstractSExprNode> children = new ArrayList<>();
  private SourceLocation lastLocation = null;

  public SExprListNode() {
  }

  public SExprListNode(List<AbstractSExprNode> children) {
    this.children = children;
  }

  @Override
  public void visitSymbol(String value) {
    SExprSymbolNode child = new SExprSymbolNode(value);
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    super.visitSymbol(value);
  }

  @Override
  public void visitString(String value) {
    SExprStringNode child = new SExprStringNode(value);
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    super.visitString(value);
  }

  @Override
  public void visitDataString(byte[] value) {
    SExprDataStringNode child = new SExprDataStringNode(value);
    child.visitDataString(value);
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    super.visitDataString(value);
  }

  @Override
  public void visitInteger(long value) {
    SExprIntegerNode child = new SExprIntegerNode(value);
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    super.visitInteger(value);
  }

  @Override
  public void visitFloatingPoint(double value) {
    SExprFloatingPointNode child = new SExprFloatingPointNode(value);
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    super.visitFloatingPoint(value);
  }

  @Override
  public SExprVisitor visitList() {
    AbstractSExprNode child = new SExprListNode();
    children.add(child);
    child.location = lastLocation;
    lastLocation = null;
    return child;
  }

  @Override
  public void visitSourceLocation(SourceLocation location) {
    lastLocation = location;
    super.visitSourceLocation(location);
  }

  @Override
  public void visitEnd() {
  }

  public void add(AbstractSExprNode node) {
    children.add(node);
  }

  public void insert(AbstractSExprNode node){
    children.add(0, node);
  }

  public void insertAfter(AbstractSExprNode previous, AbstractSExprNode node) {
    if (previous == null) {
      insert(node);
    } else {
      int index = children.indexOf(previous);
      if (index != -1) {
        children.add(index + 1, node);
      } else {
        throw new IllegalArgumentException("Node not found in the list");
      }
    }
  }

  public void insertBefore(AbstractSExprNode next, AbstractSExprNode node) {
    if (next == null) {
      add(node);
    } else {
      int index = children.indexOf(next);
      if (index != -1) {
        children.add(index, node);
      } else {
        throw new IllegalArgumentException("Node not found in the list");
      }
    }
  }

  public boolean remove(AbstractSExprNode node) {
    return children.remove(node);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SExprListNode that = (SExprListNode) o;
    return Objects.equals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(children);
  }

  @Override
  public void accept(SExprVisitor visitor) {
    if (location != null) {
      visitor.visitSourceLocation(location);
    }

    SExprVisitor lVisitor = visitor.visitList();

    for (AbstractSExprNode child : children) {
      child.accept(lVisitor);
    }

    lVisitor.visitEnd();
  }
}
