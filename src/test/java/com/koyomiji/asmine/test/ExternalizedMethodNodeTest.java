package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.InsnInserter;
import com.koyomiji.asmine.tree.ExternalizedMethodNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ExternalizedMethodNodeTest {
  ExternalizedMethodNode externalize(MethodNode methodNode) {
    ExternalizedMethodNode externalizedMethodNode = new ExternalizedMethodNode(methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, methodNode.exceptions == null ? null : methodNode.exceptions.toArray(new String[0]));
    methodNode.accept(externalizedMethodNode);
    return externalizedMethodNode;
  }

  MethodNode roundTrip(MethodNode methodNode) {
    ExternalizedMethodNode externalized = externalize(methodNode);
    MethodNode roundTrip = new MethodNode(externalized.access, externalized.name, externalized.desc, externalized.signature, externalized.exceptions == null ? null : externalized.exceptions.toArray(new String[0]));
    externalized.accept(roundTrip);
    return roundTrip;
  }

  @Test
  void test_0() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.RETURN);

    ExternalizedMethodNode externalizedMethodNode = externalize(methodNode);

    Assertions.assertEquals(1, externalizedMethodNode.instructions.size());
    Assertions.assertTrue(externalizedMethodNode.instructions.get(0) instanceof InsnNode && externalizedMethodNode.instructions.get(0).getOpcode() == Opcodes.RETURN);
    Assertions.assertEquals(0, externalizedMethodNode.lineNumbers.size());
    Assertions.assertEquals(0, externalizedMethodNode.frames.size());
  }

  @Test
  void test_1() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(label)
            .addLineNumber(1, label)
            .addInsn(Opcodes.RETURN);

    ExternalizedMethodNode externalizedMethodNode = externalize(methodNode);

    Assertions.assertEquals(2, externalizedMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, externalizedMethodNode.instructions.get(0));
    Assertions.assertTrue(externalizedMethodNode.instructions.get(1) instanceof InsnNode && externalizedMethodNode.instructions.get(1).getOpcode() == Opcodes.RETURN);
    Assertions.assertEquals(1, externalizedMethodNode.lineNumbers.size());
    Assertions.assertEquals(1, externalizedMethodNode.lineNumbers.get(0).line);
    Assertions.assertEquals(0, externalizedMethodNode.frames.size());
  }

  @Test
  void test_roundTrip_0() {
    MethodNode methodNode = new MethodNode();
    new InsnInserter(methodNode)
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(1, roundTripMethodNode.instructions.size());
    Assertions.assertTrue(roundTripMethodNode.instructions.get(0) instanceof InsnNode && roundTripMethodNode.instructions.get(0).getOpcode() == Opcodes.RETURN);
  }

  @Test
  void test_roundTrip_1() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(label)
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(2, roundTripMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, roundTripMethodNode.instructions.get(0));
    Assertions.assertTrue(roundTripMethodNode.instructions.get(1) instanceof InsnNode && roundTripMethodNode.instructions.get(1).getOpcode() == Opcodes.RETURN);
  }

  @Test
  void test_roundTrip_2() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(label)
            .addLineNumber(1, label)
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(3, roundTripMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, roundTripMethodNode.instructions.get(0));
    Assertions.assertInstanceOf(LineNumberNode.class, roundTripMethodNode.instructions.get(1));
    Assertions.assertTrue(roundTripMethodNode.instructions.get(2) instanceof InsnNode && roundTripMethodNode.instructions.get(2).getOpcode() == Opcodes.RETURN);
  }

  @Test
  void test_roundTrip_3() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(new Label())
            .addLabel(new Label())
            .addLabel(new Label())
            .addLabel(label)
            .addLineNumber(1, label)
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(3, roundTripMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, roundTripMethodNode.instructions.get(0));
    Assertions.assertInstanceOf(LineNumberNode.class, roundTripMethodNode.instructions.get(1));
    Assertions.assertTrue(roundTripMethodNode.instructions.get(2) instanceof InsnNode && roundTripMethodNode.instructions.get(2).getOpcode() == Opcodes.RETURN);
  }

  @Test
  void test_roundTrip_4() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(new Label())
            .addLabel(label)
            .addLabel(new Label())
            .addLineNumber(1, label)
            .addLabel(new Label())
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(3, roundTripMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, roundTripMethodNode.instructions.get(0));
    Assertions.assertInstanceOf(LineNumberNode.class, roundTripMethodNode.instructions.get(1));
    Assertions.assertTrue(roundTripMethodNode.instructions.get(2) instanceof InsnNode && roundTripMethodNode.instructions.get(2).getOpcode() == Opcodes.RETURN);
  }

  @Test
  void test_roundTrip_5() {
    MethodNode methodNode = new MethodNode();
    Label label = new Label();
    new InsnInserter(methodNode)
            .addLabel(new Label())
            .addLabel(label)
            .addLabel(new Label())
            .addFrame(Opcodes.F_NEW, 0, null, 0, null)
            .addLabel(new Label())
            .addInsn(Opcodes.RETURN);

    MethodNode roundTripMethodNode = roundTrip(methodNode);

    Assertions.assertEquals(3, roundTripMethodNode.instructions.size());
    Assertions.assertInstanceOf(LabelNode.class, roundTripMethodNode.instructions.get(0));
    Assertions.assertInstanceOf(FrameNode.class, roundTripMethodNode.instructions.get(1));
    Assertions.assertTrue(roundTripMethodNode.instructions.get(2) instanceof InsnNode && roundTripMethodNode.instructions.get(2).getOpcode() == Opcodes.RETURN);
  }
}
