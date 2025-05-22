package com.koyomiji.asmine.sexpr;

import com.koyomiji.asmine.common.SourceLocation;

public abstract class SExprVisitor {
  protected SExprVisitor sev;

  public SExprVisitor() {
    this(null);
  }

  public SExprVisitor(SExprVisitor sev) {
    this.sev = sev;
  }

  public void visitSymbol(String value) {
    if (sev != null) {
      sev.visitSymbol(value);
    }
  }

  public void visitString(String value) {
    if (sev != null) {
      sev.visitString(value);
    }
  }

  public void visitDataString(byte[] value) {
    if (sev != null) {
      sev.visitDataString(value);
    }
  }

  public void visitInteger(long value) {
    if (sev != null) {
      sev.visitInteger(value);
    }
  }

  public void visitFloatingPoint(double value) {
    if (sev != null) {
      sev.visitFloatingPoint(value);
    }
  }

  public SExprVisitor visitList() {
    if (sev != null) {
      return sev.visitList();
    }

    return null;
  }

  public void visitSourceLocation(SourceLocation location) {
    if (sev != null) {
      sev.visitSourceLocation(location);
    }
  }

  public void visitEnd() {
    if (sev != null) {
      sev.visitEnd();
    }
  }
}
