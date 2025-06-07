package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.analysis.Frame;
import com.koyomiji.asmine.tree.NormalizedMethodNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.MethodNode;

public class NormalizedMethodNodeTest {
  @Test
  void test_0() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(0, 0);
    nMethodNode.visitEnd();
    Assertions.assertEquals(-1, nMethodNode.instructions.get(0).getOpcode());
    Assertions.assertEquals(Opcodes.RETURN, nMethodNode.instructions.get(1).getOpcode());
    Assertions.assertEquals(-1, nMethodNode.instructions.get(2).getOpcode());
  }

  Label l0 = new Label();
  Label l1 = new Label();
  Label l2 = new Label();
  Label l3 = new Label();

  @Test
  void test_1() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 0);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 1, new Object[]{Opcodes.INTEGER}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 1);
    nMethodNode.visitEnd();
    Assertions.assertEquals(Opcodes.ICONST_0, nMethodNode.instructions.get(1).getOpcode());
    Assertions.assertEquals(Opcodes.ISTORE, nMethodNode.instructions.get(3).getOpcode());
    Assertions.assertEquals(Opcodes.GOTO, nMethodNode.instructions.get(5).getOpcode());
    Assertions.assertEquals(-1, nMethodNode.instructions.get(7).getOpcode());
    Assertions.assertEquals(ArrayListHelper.of(Frame.AUTO), ((FrameNode)nMethodNode.instructions.get(7)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)nMethodNode.instructions.get(7)).stack);
    Assertions.assertEquals(Opcodes.RETURN, nMethodNode.instructions.get(8).getOpcode());
  }

  @Test
  void test_2() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitVarInsn(Opcodes.ASTORE, 0);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitVarInsn(Opcodes.ASTORE, 0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 1, new Object[]{"A"}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 1);
    nMethodNode.visitEnd();
    Assertions.assertEquals(ArrayListHelper.of(Frame.AUTO), ((FrameNode)nMethodNode.instructions.get(13)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)nMethodNode.instructions.get(13)).stack);
  }

  @Test
  void test_3() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitVarInsn(Opcodes.ASTORE, 0);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LB;");
    nMethodNode.visitVarInsn(Opcodes.ASTORE, 0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 1, new Object[]{"C"}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 1);
    nMethodNode.visitEnd();
    Assertions.assertEquals(-1, nMethodNode.instructions.get(13).getOpcode());
    Assertions.assertEquals(ArrayListHelper.of("C"), ((FrameNode)nMethodNode.instructions.get(13)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)nMethodNode.instructions.get(13)).stack);
  }

  @Test
  void test_4() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.LCONST_0);
    nMethodNode.visitVarInsn(Opcodes.LSTORE, 0);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LB;");
    nMethodNode.visitVarInsn(Opcodes.ASTORE, 1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(0, 2);
    nMethodNode.visitEnd();
    Assertions.assertEquals(-1, nMethodNode.instructions.get(13).getOpcode());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.TOP, Opcodes.TOP), ((FrameNode)nMethodNode.instructions.get(13)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)nMethodNode.instructions.get(13)).stack);
  }

  @Test
  void test_5() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{Opcodes.INTEGER});
    nMethodNode.visitInsn(Opcodes.IRETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), ((FrameNode)nMethodNode.instructions.get(3)).stack);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)nMethodNode.instructions.get(3)).local);
  }

  @Test
  void test_roundtrip_0() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{Opcodes.INTEGER});
    nMethodNode.visitInsn(Opcodes.IRETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), ((FrameNode)methodNode.instructions.get(3)).stack);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(3)).local);
  }

  @Test
  void test_roundtrip_1() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 0);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 1, new Object[]{Opcodes.INTEGER}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 1);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), ((FrameNode)methodNode.instructions.get(7)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(7)).stack);
  }

  @Test
  void test_roundtrip_2() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", 0, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 0);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 2, new Object[]{"Test", Opcodes.INTEGER}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(0, 2);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(0, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of("Test", Opcodes.INTEGER), ((FrameNode)methodNode.instructions.get(7)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(7)).stack);
  }

  @Test
  void test_roundtrip_3() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.ICONST_1);
    nMethodNode.visitLabel(l1);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{Opcodes.INTEGER});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(12)).local);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), ((FrameNode)methodNode.instructions.get(12)).stack);
  }

  @Test
  void test_roundtrip_4() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 0);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 0);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitVarInsn(Opcodes.ISTORE, 1);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l0);
    nMethodNode.visitMaxs(1, 2);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(5)).local);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(5)).stack);
  }

  @Test
  void test_roundtrip_5() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LB;");
    nMethodNode.visitLabel(l1);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{"C"});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(12)).local);
    Assertions.assertEquals(ArrayListHelper.of("C"), ((FrameNode)methodNode.instructions.get(12)).stack);
  }

  @Test
  void test_roundtrip_6() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitJumpInsn(Opcodes.IFEQ, l0);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitLabel(l1);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{"A"});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(12)).local);
    Assertions.assertEquals(ArrayListHelper.of("A"), ((FrameNode)methodNode.instructions.get(12)).stack);
  }

  // TableSwitch
  @Test
  void test_roundtrip_7() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitTableSwitchInsn(0, 1, l0, l0, l1);
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitLabel(l1);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{"A"});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(12)).local);
    Assertions.assertEquals(ArrayListHelper.of("A"), ((FrameNode)methodNode.instructions.get(12)).stack);
  }

  // LookupSwitch
  @Test
  void test_roundtrip_8() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitInsn(Opcodes.ICONST_0);
    nMethodNode.visitLookupSwitchInsn(l0, new int[]{0, 1}, new Label[]{l0, l1});
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l1);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitFieldInsn(Opcodes.GETSTATIC, "X", "Y", "LA;");
    nMethodNode.visitLabel(l1);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{"A"});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(12)).local);
    Assertions.assertEquals(ArrayListHelper.of("A"), ((FrameNode)methodNode.instructions.get(12)).stack);
  }

  @Test
  void test_roundtrip_9() {
    NormalizedMethodNode nMethodNode = new NormalizedMethodNode("Test", Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.visitLabel(l0);
    nMethodNode.visitInsn(Opcodes.NOP);
    nMethodNode.visitLabel(l1);
    nMethodNode.visitJumpInsn(Opcodes.GOTO, l3);
    nMethodNode.visitLabel(l2);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 1, new Object[]{"A"});
    nMethodNode.visitInsn(Opcodes.POP);
    nMethodNode.visitLabel(l3);
    nMethodNode.visitFrame(Opcodes.F_NEW, 0, new Object[]{}, 0, new Object[]{});
    nMethodNode.visitInsn(Opcodes.RETURN);
    nMethodNode.visitTryCatchBlock(l0,l1,l2,"A");
    nMethodNode.visitMaxs(1, 0);
    nMethodNode.visitEnd();
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    nMethodNode.accept(methodNode);
    Assertions.assertEquals(ArrayListHelper.of(), ((FrameNode)methodNode.instructions.get(5)).local);
    Assertions.assertEquals(ArrayListHelper.of("A"), ((FrameNode)methodNode.instructions.get(5)).stack);
  }
}
