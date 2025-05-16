package com.koyomiji.jasmine.test;

import com.koyomiji.jasmine.common.Insns;
import com.koyomiji.jasmine.query.MethodManipulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.MethodNode;

public class MethodManipulatorTest {
  @Test
  void test_symbol_0() {
    MethodManipulator q = new MethodManipulator(new MethodNode());
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
    MethodManipulator q = new MethodManipulator(new MethodNode());
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
    MethodManipulator q = new MethodManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s0 = q.getIndexSymbol(0);
    Object s1 = q.getIndexSymbol(1);
    q.insertInsnsBefore(0, Insns.iconst_1());
    Assertions.assertEquals(0, q.getIndexForSymbol(s0));
    Assertions.assertEquals(2, q.getIndexForSymbol(s1));
  }

  @Test
  void test_symbol_3() {
    MethodManipulator q = new MethodManipulator(new MethodNode());
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
    MethodManipulator q = new MethodManipulator(new MethodNode());
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
    MethodManipulator q = new MethodManipulator(new MethodNode());
    q.addInsns(
            Insns.iconst_0(),
            Insns.return_()
    );
    Object s2 = q.getIndexSymbol(2);
    q.addInsns(Insns.iconst_1());
    Assertions.assertEquals(2, q.getIndexForSymbol(s2));
  }
}
