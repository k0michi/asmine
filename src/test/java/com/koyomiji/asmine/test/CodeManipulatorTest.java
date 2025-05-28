package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.Insns;
import com.koyomiji.asmine.query.CodeManipulator;
import com.koyomiji.asmine.query.Cursor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.MethodNode;

public class CodeManipulatorTest {
  // Replace empty with empty
  @Test
  void test_cursor_m1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.replace(0, 0);
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(1, s1.getFirstIndex());
  }

  @Test
  void test_cursor_0() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.remove(0, 1);
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(0, s1.getFirstIndex());
  }

  @Test
  void test_cursor_1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.insertAfter(0, Insns.iconst_1());
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(2, s1.getFirstIndex());
  }

  @Test
  void test_cursor_2() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.insertBefore(0, Insns.iconst_1());
    Assertions.assertEquals(1, s0.getFirstIndex());
    Assertions.assertEquals(2, s1.getFirstIndex());
  }

  @Test
  void test_cursor_3() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.insertAfter(-1, Insns.iconst_1());
    Assertions.assertEquals(1, s0.getFirstIndex());
    Assertions.assertEquals(2, s1.getFirstIndex());
  }

  @Test
  void test_cursor_4() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.replace(0, 1, Insns.iconst_1());
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(1, s1.getFirstIndex());
  }

  @Test
  void test_cursor_5() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s2 = q.getCursor(2);
    q.addLast(Insns.iconst_1());
    Assertions.assertEquals(3, s2.getFirstIndex());
  }

  // Replace empty range
  @Test
  void test_cursor_6() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.replace(0, 0, Insns.iconst_1());
    // s0 is now at 0 and 1
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(1, s0.getLastIndex());
    Assertions.assertEquals(2, s1.getFirstIndex());
  }

  // Replace with empty
  @Test
  void test_cursor_7() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    q.replace(0, 1);
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(0, s1.getFirstIndex());
  }

  @Test
  void test_cursor_8() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.iconst_0(),
            Insns.return_()
    );
    Cursor s0 = q.getCursor(0);
    Cursor s1 = q.getCursor(1);
    Cursor s2 = q.getCursor(2);
    q.insertAfter(1, Insns.iconst_1());
    Assertions.assertEquals(0, s0.getFirstIndex());
    Assertions.assertEquals(1, s1.getFirstIndex());
    Assertions.assertEquals(3, s2.getFirstIndex());
  }

  @Test
  void test_0() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.label(),
            Insns.iconst_0(),
            Insns.return_()
    );
    Assertions.assertEquals(-1, q.getCursor(-1).getFirstIndex());
    Assertions.assertEquals(0, q.getCursor(0).getFirstIndex());
    Assertions.assertEquals(1, q.getCursor(1).getFirstIndex());
    Assertions.assertEquals(2, q.getCursor(2).getFirstIndex());
    Assertions.assertEquals(3, q.getCursor(3).getFirstIndex());
    Assertions.assertEquals(4, q.getCursor(4).getFirstIndex());
  }

  // before/after
  @Test
  void test_1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addLast(
            Insns.label(),
            Insns.iconst_0(),
            Insns.return_()
    );
    Assertions.assertEquals(0, q.after(q.getCursor(-1)).getFirstIndex());
    Assertions.assertEquals(-1, q.before(q.getCursor(0)).getFirstIndex());
  }
}
