package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.Insns;
import com.koyomiji.asmine.frame.Frame;
import com.koyomiji.asmine.frame.FrameComputationException;
import com.koyomiji.asmine.frame.FrameComputer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class FrameComputerTest {
  @Test
  void test_0() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.return_());
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getStack());
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getLocals());
  }

  @Test
  void test_1() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()I", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.ireturn());
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getStack());
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), computedFrames.get(1).getStack());
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(1).getLocals());
  }

  // Arguments
  @Test
  void test_2() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "(BCDFIJLA;SZ)V", null, null);
    methodNode.instructions.add(Insns.return_());
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getStack());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.DOUBLE, Opcodes.FLOAT, Opcodes.INTEGER, Opcodes.LONG, "A", Opcodes.INTEGER, Opcodes.INTEGER), computedFrames.get(0).getLocals());
  }

  // Arguments - arrays
  @Test
  void test_3() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "([B[C[D[F[I[J[LA;[S[Z)V", null, null);
    methodNode.instructions.add(Insns.return_());
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(0).getStack());
    Assertions.assertEquals(ArrayListHelper.of("[B", "[C", "[D", "[F", "[I", "[J", "[LA;", "[S", "[Z"), computedFrames.get(0).getLocals());
  }

  private LabelNode l0;
  private LabelNode l1;
  private LabelNode l2;
  private LabelNode l3;

  @Test
  void test_4() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(l0 = Insns.label());
    methodNode.instructions.add(Insns.goto_(l0));
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(1).getStack());
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(1).getLocals());
  }

  // Push infinitely
  @Test
  void test_5() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(l0 = Insns.label());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.goto_(l0));
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);

    Assertions.assertThrows(FrameComputationException.class, () -> {
      frameComputer.computeFrames();
    });
  }

  @Test
  void test_6() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.istore(0));
    methodNode.instructions.add(l0 = Insns.label());
    methodNode.instructions.add(Insns.goto_(l0));
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(3).getStack());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), computedFrames.get(3).getLocals());
  }

  @Test
  void test_7() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.istore(0));
    methodNode.instructions.add(l0 = Insns.label());
    methodNode.instructions.add(Insns.fconst_0());
    methodNode.instructions.add(Insns.fstore(0));
    methodNode.instructions.add(Insns.goto_(l0));
    FrameComputer frameComputer = new FrameComputer("Test", methodNode);
    List<Frame> computedFrames = frameComputer.computeFrames();
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(3).getStack());
    Assertions.assertEquals(ArrayListHelper.of(), computedFrames.get(3).getLocals());
  }
}
