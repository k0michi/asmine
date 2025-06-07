package com.koyomiji.asmine.test;

import com.koyomiji.asmine.analysis.FlowAnalyzer;
import com.koyomiji.asmine.analysis.FlowAnalyzerThread;
import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.Insns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class FlowAnalyzerTest {
  LabelNode l0 = new LabelNode();
  LabelNode l1 = new LabelNode();
  LabelNode l2 = new LabelNode();
  LabelNode l3 = new LabelNode();

  @Test
  void test_0() {
    MethodNode methodNode = new MethodNode(0, "test", "()V", null, null);
    methodNode.instructions.add(Insns.return_());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    Assertions.assertEquals(methodNode.instructions.get(0), flowAnalyzer.getEntryPoint());
    Assertions.assertEquals(1, flowAnalyzer.getAllEntryPoints().size());
    Assertions.assertEquals(methodNode.instructions.get(0), flowAnalyzer.getAllEntryPoints().get(0));
  }

  @Test
  void test_1() {
    MethodNode methodNode = new MethodNode(0, "test", "()V", null, null);
    methodNode.instructions.add(Insns.return_());
    methodNode.instructions.add(Insns.return_());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    Assertions.assertEquals(2, flowAnalyzer.getAllEntryPoints().size());
    Assertions.assertEquals(methodNode.instructions.get(0), flowAnalyzer.getAllEntryPoints().get(0));
    Assertions.assertEquals(methodNode.instructions.get(1), flowAnalyzer.getAllEntryPoints().get(1));
  }

  private FlowAnalyzerThread execute(FlowAnalyzer flowAnalyzer, int steps) {
    FlowAnalyzerThread thread = flowAnalyzer.getEntryThread();
    for (int i = 0; i < steps; i++) {
      thread = flowAnalyzer.step(thread).get(0);
    }
    return thread;
  }

  @Test
  void test_2() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.aconst_null());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.NULL), thread.getStack());
  }

  @Test
  void test_3() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getStack());
  }

  @Test
  void test_4() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.fconst_0());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.FLOAT), thread.getStack());
  }

  @Test
  void test_5() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.dconst_0());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.DOUBLE), thread.getStack());
  }

  @Test
  void test_6() {
    // TODO: LDC
  }

  // aload
  @Test
  void test_7() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "I"));
    methodNode.instructions.add(Insns.astore(0));
    methodNode.instructions.add(Insns.aload(0));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getStack());
  }

  @Test
  void test_8() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.dconst_0());
    methodNode.instructions.add(Insns.d2l());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getStack());
  }

  @Test
  void test_9() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.l2d());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.DOUBLE), thread.getStack());
  }

  @Test
  void test_10() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.newarray(Opcodes.T_INT));
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.aaload());
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.aaload());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 7);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER, Opcodes.NULL), thread.getStack());
  }

  @Test
  void test_11() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.istore(0));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_12() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.lstore(0));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_13() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.newarray(Opcodes.T_INT));
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.iastore());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 5);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_14() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.newarray(Opcodes.T_LONG));
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.lastore());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 5);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_15() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.pop());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_16() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.pop2());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_17() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.dup());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("A", "A"), thread.getStack());
  }

  @Test
  void test_18() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.dup_x1());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("B", "A", "B"), thread.getStack());
  }

  @Test
  void test_19() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.getstatic("A", "c", "LC;"));
    methodNode.instructions.add(Insns.dup_x2());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 4);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("C", "A", "B", "C"), thread.getStack());
  }

  @Test
  void test_20() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.dup2());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("A", "B", "A", "B"), thread.getStack());
  }

  @Test
  void test_21() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.getstatic("A", "c", "LC;"));
    methodNode.instructions.add(Insns.dup2_x1());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 4);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("B", "C", "A", "B", "C"), thread.getStack());
  }

  @Test
  void test_22() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.getstatic("A", "c", "LC;"));
    methodNode.instructions.add(Insns.getstatic("A", "d", "LD;"));
    methodNode.instructions.add(Insns.dup2_x2());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 5);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("C", "D", "A", "B", "C", "D"), thread.getStack());
  }

  @Test
  void test_23() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("A", "a", "LA;"));
    methodNode.instructions.add(Insns.getstatic("A", "b", "LB;"));
    methodNode.instructions.add(Insns.swap());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("B", "A"), thread.getStack());
  }

  @Test
  void test_24() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.iadd());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getStack());
  }

  @Test
  void test_25() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.ladd());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getStack());
  }

  @Test
  void test_26() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.l2f());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.FLOAT), thread.getStack());
  }

  @Test
  void test_27() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.dconst_0());
    methodNode.instructions.add(Insns.dconst_0());
    methodNode.instructions.add(Insns.dadd());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.DOUBLE), thread.getStack());
  }

  @Test
  void test_28() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_1());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.lshl());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getStack());
  }

  @Test
  void test_29() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.istore(0));
    methodNode.instructions.add(Insns.iinc(0, 1));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_30() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.i2l());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.LONG), thread.getStack());
  }

  @Test
  void test_31() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.i2f());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.FLOAT), thread.getStack());
  }

  @Test
  void test_31_() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.i2d());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.DOUBLE), thread.getStack());
  }

  @Test
  void test_32() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.fconst_0());
    methodNode.instructions.add(Insns.f2i());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getStack());
  }

  @Test
  void test_33() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.lcmp());
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getStack());
  }

  @Test
  void test_34() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("java/lang/System", "out", "Ljava/io/PrintStream;"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("java/io/PrintStream"), thread.getStack());
  }

  @Test
  void test_35() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.getstatic("java/lang/System", "out", "Ljava/io/PrintStream;"));
    methodNode.instructions.add(Insns.putstatic("java/lang/System", "out", "Ljava/io/PrintStream;"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_36() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.getfield("A", "a", "I"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(Opcodes.INTEGER), thread.getStack());
  }

  @Test
  void test_37() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.getfield("A", "a", "I"));
    methodNode.instructions.add(Insns.putfield("A", "a", "I"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 4);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(), thread.getStack());
  }

  @Test
  void test_38() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.invokevirtual("A", "a", "(IJ)Ljava/lang/String;", false));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 4);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("java/lang/String"), thread.getStack());
  }

  @Test
  void test_39() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_0());
    methodNode.instructions.add(Insns.lconst_0());
    methodNode.instructions.add(Insns.invokestatic("A", "a", "(IJ)Ljava/lang/String;", false));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("java/lang/String"), thread.getStack());
  }

  @Test
  void test_40() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(l0);
    methodNode.instructions.add(Insns.new_("A"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 1);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of(l0), thread.getStack());
  }

  @Test
  void test_41() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.newarray(Opcodes.T_INT));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("[I"), thread.getStack());
  }

  @Test
  void test_42() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.anewarray("A"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("[LA;"), thread.getStack());
  }

  @Test
  void test_43() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.aconst_null());
    methodNode.instructions.add(Insns.checkcast("A"));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 2);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("A"), thread.getStack());
  }

  @Test
  void test_44() {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_STATIC, "test", "()V", null, null);
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.iconst_1());
    methodNode.instructions.add(Insns.multianewarray("[[I", 2));
    FlowAnalyzer flowAnalyzer = new FlowAnalyzer("Test", methodNode);
    FlowAnalyzerThread thread = execute(flowAnalyzer, 3);
    Assertions.assertEquals(ArrayListHelper.of(), thread.getLocals());
    Assertions.assertEquals(ArrayListHelper.of("[[I"), thread.getStack());
  }
}
