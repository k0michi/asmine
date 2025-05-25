package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.Insns;
import com.koyomiji.asmine.query.CodeManipulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.MethodNode;

public class CodeManipulatorTest {
  // Replace empty with empty
  @Test
  void test_symbol_m1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.replace(0, 0);
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(1, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_0() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.remove(0, 1);
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(0, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.insertAfter(0, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(2, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_2() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.insertBefore(0, Insns.iconst_1());
    Assertions.assertEquals(1, q.getIndexForCursor(s0));
    Assertions.assertEquals(2, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_3() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.insertAfter(-1, Insns.iconst_1());
    Assertions.assertEquals(1, q.getIndexForCursor(s0));
    Assertions.assertEquals(2, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_4() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.replace(0, 1, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(1, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_5() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s2 = q.getCursor(2);
    q.addLast(Insns.iconst_1());
    Assertions.assertEquals(3, q.getIndexForCursor(s2));
  }

  // Replace empty range
  @Test
  void test_symbol_6() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.replace(0, 0, Insns.iconst_1());
    // s0 is now at 0 and 1
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(1, q.getLastIndexForCursor(s0));
    Assertions.assertEquals(2, q.getIndexForCursor(s1));
  }

  // Replace with empty
  @Test
  void test_symbol_7() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    q.replace(0, 1);
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(0, q.getIndexForCursor(s1));
  }

  @Test
  void test_symbol_8() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getCursor(0);
    Object s1 = q.getCursor(1);
    Object s2 = q.getCursor(2);
    q.insertAfter(1, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForCursor(s0));
    Assertions.assertEquals(1, q.getIndexForCursor(s1));
    Assertions.assertEquals(3, q.getIndexForCursor(s2));
  }
}
