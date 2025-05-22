package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.PrinterHelper;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;

public class FieldQuery<T> extends AbstractQuery<T> {
  protected FieldNode fieldNode;

  public FieldQuery(T parent, FieldNode fieldNode) {
    super(parent);
    this.fieldNode = fieldNode;
  }

  public static FieldQuery<FieldNode> of(FieldNode fieldNode) {
    return new FieldQuery<>(fieldNode, fieldNode);
  }

  public FieldNode getNode() {
    return fieldNode;
  }

  public FieldQuery<T> require() {
    if (fieldNode == null) {
      throw new QueryException("Field not found");
    }

    return this;
  }

  public FieldQuery<T> print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public FieldQuery<T> print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, fieldNode, printWriter);
    return this;
  }
}
