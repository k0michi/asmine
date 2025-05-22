package com.koyomiji.asmine.common;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class PrinterHelper {
  public static void print(Printer printer, ClassNode classNode, PrintWriter printWriter) {
    TraceClassVisitor v = new TraceClassVisitor(null, printer, printWriter);
    classNode.accept(v);
  }

  public static void print(Printer printer, ClassNode classNode) {
    print(printer, classNode, new PrintWriter(System.out));
  }

  public static void print(Printer printer, FieldNode fieldNode, PrintWriter printWriter) {
    TraceClassVisitor v = new TraceClassVisitor(null, printer, printWriter);
    fieldNode.accept(v);

    printer.print(printWriter);
    printWriter.flush();
  }

  public static void print(Printer printer, FieldNode fieldNode) {
    print(printer, fieldNode, new PrintWriter(System.out));
  }

  public static void print(Printer printer, MethodNode methodNode, PrintWriter printWriter) {
    TraceClassVisitor v = new TraceClassVisitor(null, printer, printWriter);
    methodNode.accept(v);

    printer.print(printWriter);
    printWriter.flush();
  }

  public static void print(Printer printer, MethodNode methodNode) {
    print(printer, methodNode, new PrintWriter(System.out));
  }

  public static void print(Printer printer, AbstractInsnNode insnNode, PrintWriter printWriter) {
    TraceMethodVisitor v = new TraceMethodVisitor(null, printer);
    insnNode.accept(v);

    printer.print(printWriter);
    printWriter.flush();
  }

  public static void print(Printer printer, AbstractInsnNode insnNode) {
    print(printer, insnNode, new PrintWriter(System.out));
  }

  public static String toString(Printer printer, ClassNode classNode) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(baos);

    print(printer, classNode, printWriter);

    clear(printer);
    return baos.toString();
  }

  public static String toString(Printer printer, FieldNode fieldNode) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(baos);

    print(printer, fieldNode, printWriter);

    clear(printer);
    return baos.toString();
  }

  public static String toString(Printer printer, MethodNode methodNode) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(baos);

    print(printer, methodNode, printWriter);

    clear(printer);
    return baos.toString();
  }

  public static String toString(Printer printer, AbstractInsnNode insnNode) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(baos);

    print(printer, insnNode, printWriter);

    clear(printer);
    return baos.toString();
  }

  public static String toString(Printer printer) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(baos);

    printer.print(printWriter);
    printWriter.flush();

    clear(printer);
    return baos.toString();
  }

  public static void clear(Printer printer) {
    printer.getText().clear();
  }
}
