package com.koyomiji.asmine.test;

import com.koyomiji.asmine.sexpr.tree.SExprListNode;
import com.koyomiji.asmine.sexpr.tree.SExprNodeContainer;
import com.koyomiji.asmine.sexpr.SExprReader;
import com.koyomiji.asmine.sexpr.SExprWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SExprNodeContainerTest {
  void roundtrip(String sexpr) throws IOException {
    SExprNodeContainer node = new SExprNodeContainer();
    SExprReader reader = new SExprReader(sexpr);
    reader.accept(node);
    SExprWriter writer = new SExprWriter();
    node.accept(writer);
    Assertions.assertEquals(sexpr, writer.toString());
  }

  SExprNodeContainer parse(String sexpr) throws IOException {
    SExprNodeContainer node = new SExprNodeContainer();
    SExprReader reader = new SExprReader(sexpr);
    reader.accept(node);
    return node;
  }

  @Test
  void test_roundtrip_0() throws IOException {
    roundtrip("a");
  }

  @Test
  void test_roundtrip_1() throws IOException {
    roundtrip("(a)");
  }

  @Test
  void test_roundtrip_2() throws IOException {
    roundtrip("(a b c)");
  }

  @Test
  void test_roundtrip_3() throws IOException {
    roundtrip("(a b c (d e f))");
  }

  @Test
  void test_roundtrip_4() throws IOException {
    roundtrip("(a \"b\" \"c \" (d e f))");
  }

  @Test
  void test_0() throws IOException {
    SExprNodeContainer parsed = parse("(0)");
    Assertions.assertEquals(0, parsed.child.location.getOffset());
    Assertions.assertEquals(1, (((SExprListNode) parsed.child).children.get(0)).location.getOffset());
  }

  @Test
  void test_1() throws IOException {
    SExprNodeContainer parsed = parse("0");
    Assertions.assertEquals(0, parsed.child.location.getOffset());
  }
}
