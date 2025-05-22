package com.koyomiji.jasmine.test;

import com.koyomiji.jasmine.common.Insns;
import com.koyomiji.jasmine.query.CodeManipulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.MethodNode;

public class CodeManipulatorTest {
  // Replace empty with empty
  @Test
  void test_symbol_m1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.replaceInsns(0, 0);
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(1, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_0() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.removeInsns(0, 1);
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(0, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_1() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.insertInsnsAfter(0, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(2, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_2() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.insertInsnsBefore(0, Insns.iconst_1());
    Assertions.assertEquals(1, q.getIndexForSymbol(s0));
    Assertions.assertEquals(2, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_3() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.insertInsnsAfter(-1, Insns.iconst_1());
    Assertions.assertEquals(1, q.getIndexForSymbol(s0));
    Assertions.assertEquals(2, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_4() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.replaceInsns(0, 1, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(1, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_5() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s2 = q.getIndexSymbol(2);
    q.addInsns(Insns.iconst_1());
    Assertions.assertEquals(3, q.getIndexForSymbol(s2));
  }

  // Replace empty range
  @Test
  void test_symbol_6() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.replaceInsns(0, 0, Insns.iconst_1());
    // s0 is now at 0 and 1
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(1, q.getLastIndexForSymbol(s0));
    Assertions.assertEquals(2, q.getIndexForSymbol(s1));
  }

  // Replace with empty
  @Test
  void test_symbol_7() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.replaceInsns(0, 1);
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(0, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_8() {
    CodeManipulator q = new CodeManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    Object s2 = q.getIndexSymbol(2);
    q.insertInsnsAfter(1, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(1, q.getIndexForSymbol(s1));
    Assertions.assertEquals(3, q.getIndexForSymbol(s2));
  }
}
