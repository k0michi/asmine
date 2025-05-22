package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.InsnInserter;
import com.koyomiji.asmine.common.InsnListHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class InsnListHelperTest {
  @Test
  void test_replaceRange_0() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.ICONST_0);

    MethodNode replaceTo = new MethodNode();
    new InsnInserter(replaceTo)
            .addInsn(Opcodes.ICONST_1);

    InsnListHelper.replaceRange(methodNode.instructions, methodNode.instructions.getFirst(), null, replaceTo.instructions);

    Assertions.assertEquals(1, methodNode.instructions.size());
    Assertions.assertTrue(methodNode.instructions.get(0) instanceof InsnNode && methodNode.instructions.get(0).getOpcode() == Opcodes.ICONST_1);
  }

  @Test
  void test_replaceRange_1() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.ICONST_0);

    MethodNode replaceTo = new MethodNode();
    new InsnInserter(replaceTo)
            .addInsn(Opcodes.ICONST_1);

    InsnListHelper.replaceRange(methodNode.instructions, methodNode.instructions.getFirst(), methodNode.instructions.getFirst(), replaceTo.instructions);

    Assertions.assertEquals(2, methodNode.instructions.size());
    Assertions.assertTrue(methodNode.instructions.get(0) instanceof InsnNode && methodNode.instructions.get(0).getOpcode() == Opcodes.ICONST_1);
    Assertions.assertTrue(methodNode.instructions.get(1) instanceof InsnNode && methodNode.instructions.get(1).getOpcode() == Opcodes.ICONST_0);
  }

  @Test
  void test_replaceRange_2() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.ICONST_0)
            .addInsn(Opcodes.ICONST_0);

    MethodNode replaceTo = new MethodNode();
    new InsnInserter(replaceTo)
            .addInsn(Opcodes.ICONST_1);

    InsnListHelper.replaceRange(methodNode.instructions, methodNode.instructions.getLast(), methodNode.instructions.getLast(), replaceTo.instructions);

    Assertions.assertEquals(3, methodNode.instructions.size());
    Assertions.assertTrue(methodNode.instructions.get(0) instanceof InsnNode && methodNode.instructions.get(0).getOpcode() == Opcodes.ICONST_0);
    Assertions.assertTrue(methodNode.instructions.get(1) instanceof InsnNode && methodNode.instructions.get(1).getOpcode() == Opcodes.ICONST_1);
    Assertions.assertTrue(methodNode.instructions.get(2) instanceof InsnNode && methodNode.instructions.get(2).getOpcode() == Opcodes.ICONST_0);
  }

  @Test
  void test_removeRange_0() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.ICONST_0)
            .addInsn(Opcodes.ICONST_1)
            .addInsn(Opcodes.ICONST_2);

    InsnListHelper.removeRange(methodNode.instructions, methodNode.instructions.getFirst(), null);

    Assertions.assertEquals(0, methodNode.instructions.size());
  }

  @Test
  void test_removeRange_1() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.ICONST_0)
            .addInsn(Opcodes.ICONST_1)
            .addInsn(Opcodes.ICONST_2);

    InsnListHelper.removeRange(methodNode.instructions, methodNode.instructions.getFirst(), methodNode.instructions.getLast());

    Assertions.assertEquals(1, methodNode.instructions.size());
    Assertions.assertTrue(methodNode.instructions.get(0) instanceof InsnNode && methodNode.instructions.get(0).getOpcode() == Opcodes.ICONST_2);
  }
}
