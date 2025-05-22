package com.koyomiji.jasmine.sexpr.tree;

import com.koyomiji.jasmine.sexpr.IFormattable;
import com.koyomiji.jasmine.sexpr.SExprVisitor;
import com.koyomiji.jasmine.sexpr.SExprWriter;

public class SExprFormattableListNode extends SExprListNode implements IFormattable {
  public void visitLineBreak() {
    this.children.add(new SExprLineBreakNode());
  }

  @Override
  public SExprVisitor visitList() {
    SExprFormattableListNode child = new SExprFormattableListNode();
    children.add(child);
    return child;
  }

  public void accept(SExprWriter writer) {
    SExprWriter lVisitor = writer.visitList();

    for (AbstractSExprNode child : children) {
      child.accept(lVisitor);
    }

    lVisitor.visitEnd();
  }
}
