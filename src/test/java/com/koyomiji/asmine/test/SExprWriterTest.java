package com.koyomiji.asmine.test;

import com.koyomiji.asmine.sexpr.SExprVisitor;
import com.koyomiji.asmine.sexpr.SExprWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SExprWriterTest {
  @Test
  void test_0() {
    SExprWriter writer =new SExprWriter();
    SExprVisitor l = writer.visitList();
    l.visitSymbol("a");
    l.visitEnd();
    Assertions.assertEquals("(a)", writer.toString());
  }

  @Test
  void test_1() {
    SExprWriter writer =new SExprWriter();
    SExprVisitor l = writer.visitList();
    l.visitString("a");
    l.visitEnd();
    Assertions.assertEquals("(\"a\")", writer.toString());
  }

//  @Test
//  void test_escape_0() {
//    SExprWriter writer =new SExprWriter();
//    SExprVisitor l = writer.visitList();
//    l.visitSymbol("a a");
//    l.visitEnd();
//    Assertions.assertEquals("(a\\ a)", writer.toString());
//  }

  @Test
  void test_escape_1() {
    SExprWriter writer =new SExprWriter();
    SExprVisitor l = writer.visitList();
    l.visitString("a a");
    l.visitEnd();
    Assertions.assertEquals("(\"a a\")", writer.toString());
  }

  @Test
  void test_indent_0() {
    SExprWriter writer =new SExprWriter();
    SExprWriter l = writer.visitList();
    l.visitSymbol("a");
    l.visitLineBreak();
    l.visitSymbol("b");
    l.visitEnd();
    Assertions.assertEquals("(a\n" +
            "  b\n" +
            ")", writer.toString());
  }

  @Test
  void test_indent_1() {
    SExprWriter writer =new SExprWriter();
    SExprWriter l = writer.visitList();
    l.visitSymbol("a");
    l.visitLineBreak();
    l.visitSymbol("b");
    l.visitLineBreak();
    SExprWriter l1 = l.visitList();
    l1.visitSymbol("c");
    l1.visitLineBreak();
    l1.visitSymbol("d");
    l1.visitEnd();
    l.visitEnd();
    Assertions.assertEquals("(a\n" +
            "  b\n" +
            "  (c\n" +
            "    d\n" +
            "  )\n" +
            ")", writer.toString());
  }

  @Test
  void test_indent_2() {
    SExprWriter writer =new SExprWriter();
    SExprWriter l = writer.visitList();
    l.visitSymbol("a");
    l.visitLineBreak();
    l.visitSymbol("b");
    l.visitLineBreak();
    SExprWriter l1 = l.visitList();
    l1.visitSymbol("c");
    l1.visitLineBreak();
    l1.visitSymbol("d");
    l1.visitEnd();
    l.visitLineBreak();
    l.visitSymbol("e");
    l.visitEnd();
    Assertions.assertEquals("(a\n" +
            "  b\n" +
            "  (c\n" +
            "    d\n" +
            "  )\n" +
            "  e\n" +
            ")", writer.toString());
  }
}
