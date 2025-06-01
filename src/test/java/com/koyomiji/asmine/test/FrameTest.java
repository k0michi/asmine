package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.frame.Frame;
import com.koyomiji.asmine.common.Insns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;

public class FrameTest {
  @Test
  void test_0() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.nop());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }

  @Test
  void test_1() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.aconst_null());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.NULL), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }

  @Test
  void test_2() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.iconst_0());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }

  @Test
  void test_3() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.lconst_0());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }

  @Test
  void test_4() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.fconst_0());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.FLOAT), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }

  @Test
  void test_5() {
    Frame frame = new Frame();
    frame.execute(new LabelNode(), Insns.dconst_0());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.DOUBLE), frame.getStack());
    Assertions.assertEquals(ArrayListHelper.of(), frame.getLocals());
  }
}
