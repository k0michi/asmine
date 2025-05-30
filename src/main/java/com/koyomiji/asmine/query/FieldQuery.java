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

  public int getAccess() {
    return fieldNode.access;
  }

  public FieldQuery<T> setAccess(int access) {
    fieldNode.access = access;
    return this;
  }

  public FieldQuery<T> addAccess(int access) {
    fieldNode.access |= access;
    return this;
  }

  public FieldQuery<T> removeAccess(int access) {
    fieldNode.access &= ~access;
    return this;
  }

  public String getName() {
    return fieldNode.name;
  }

  public FieldQuery<T> setName(String name) {
    fieldNode.name = name;
    return this;
  }

  public String getDescriptor() {
    return fieldNode.desc;
  }

  public FieldQuery<T> setDescriptor(String descriptor) {
    fieldNode.desc = descriptor;
    return this;
  }

  public String getSignature() {
    return fieldNode.signature;
  }

  public FieldQuery<T> setSignature(String signature) {
    fieldNode.signature = signature;
    return this;
  }

  public Object getValue() {
    return fieldNode.value;
  }

  public FieldQuery<T> setValue(Object value) {
    fieldNode.value = value;
    return this;
  }

  // TODO: Annotations
  // TODO: Attributes

  public FieldQuery<T> print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public FieldQuery<T> print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, fieldNode, printWriter);
    return this;
  }
}
