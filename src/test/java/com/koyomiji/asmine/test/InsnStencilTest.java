package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.InsnStencils;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import com.koyomiji.asmine.stencil.insn.*;
import com.koyomiji.asmine.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Objects;

public class InsnStencilTest {
  static class TestStencilRegistry implements IStencilRegistry{
    @Override
    public Object resolveStencil(Object key) throws StencilEvaluationException {
      return null;
    }

    @Override
    public <T> void bindStencil(Object key, T value) {
    }

    @Override
    public <T> boolean bindStencilIfAbsent(Object key, T value) {
      return false;
    }

    @Override
    public <T> boolean compareValues(T value1, T value2) {
      return Objects.equals(value1, value2);
    }

    @Override
    public <T> boolean compareBoundToValue(Object key, T value) {
      return false;
    }
  }

  // InsnNode
  @Test
  void test_0() throws StencilEvaluationException {
    List<Pair<InsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.nop(), Opcodes.NOP),
            Pair.of(InsnStencils.aconst_null(), Opcodes.ACONST_NULL),
            Pair.of(InsnStencils.iconst_m1(), Opcodes.ICONST_M1),
            Pair.of(InsnStencils.iconst_0(), Opcodes.ICONST_0),
            Pair.of(InsnStencils.iconst_1(), Opcodes.ICONST_1),
            Pair.of(InsnStencils.iconst_2(), Opcodes.ICONST_2),
            Pair.of(InsnStencils.iconst_3(), Opcodes.ICONST_3),
            Pair.of(InsnStencils.iconst_4(), Opcodes.ICONST_4),
            Pair.of(InsnStencils.iconst_5(), Opcodes.ICONST_5),
            Pair.of(InsnStencils.lconst_0(), Opcodes.LCONST_0),
            Pair.of(InsnStencils.lconst_1(), Opcodes.LCONST_1),
            Pair.of(InsnStencils.fconst_0(), Opcodes.FCONST_0),
            Pair.of(InsnStencils.fconst_1(), Opcodes.FCONST_1),
            Pair.of(InsnStencils.fconst_2(), Opcodes.FCONST_2),
            Pair.of(InsnStencils.dconst_0(), Opcodes.DCONST_0),
            Pair.of(InsnStencils.dconst_1(), Opcodes.DCONST_1),
            Pair.of(InsnStencils.iaload(), Opcodes.IALOAD),
            Pair.of(InsnStencils.laload(), Opcodes.LALOAD),
            Pair.of(InsnStencils.faload(), Opcodes.FALOAD),
            Pair.of(InsnStencils.daload(), Opcodes.DALOAD),
            Pair.of(InsnStencils.aaload(), Opcodes.AALOAD),
            Pair.of(InsnStencils.baload(), Opcodes.BALOAD),
            Pair.of(InsnStencils.caload(), Opcodes.CALOAD),
            Pair.of(InsnStencils.saload(), Opcodes.SALOAD),
            Pair.of(InsnStencils.iastore(), Opcodes.IASTORE),
            Pair.of(InsnStencils.lastore(), Opcodes.LASTORE),
            Pair.of(InsnStencils.fastore(), Opcodes.FASTORE),
            Pair.of(InsnStencils.dastore(), Opcodes.DASTORE),
            Pair.of(InsnStencils.aastore(), Opcodes.AASTORE),
            Pair.of(InsnStencils.bastore(), Opcodes.BASTORE),
            Pair.of(InsnStencils.castore(), Opcodes.CASTORE),
            Pair.of(InsnStencils.sastore(), Opcodes.SASTORE),
            Pair.of(InsnStencils.pop(), Opcodes.POP),
            Pair.of(InsnStencils.pop2(), Opcodes.POP2),
            Pair.of(InsnStencils.dup(), Opcodes.DUP),
            Pair.of(InsnStencils.dup_x1(), Opcodes.DUP_X1),
            Pair.of(InsnStencils.dup_x2(), Opcodes.DUP_X2),
            Pair.of(InsnStencils.dup2(), Opcodes.DUP2),
            Pair.of(InsnStencils.dup2_x1(), Opcodes.DUP2_X1),
            Pair.of(InsnStencils.dup2_x2(), Opcodes.DUP2_X2),
            Pair.of(InsnStencils.swap(), Opcodes.SWAP),
            Pair.of(InsnStencils.iadd(), Opcodes.IADD),
            Pair.of(InsnStencils.ladd(), Opcodes.LADD),
            Pair.of(InsnStencils.fadd(), Opcodes.FADD),
            Pair.of(InsnStencils.dadd(), Opcodes.DADD),
            Pair.of(InsnStencils.isub(), Opcodes.ISUB),
            Pair.of(InsnStencils.lsub(), Opcodes.LSUB),
            Pair.of(InsnStencils.fsub(), Opcodes.FSUB),
            Pair.of(InsnStencils.dsub(), Opcodes.DSUB),
            Pair.of(InsnStencils.imul(), Opcodes.IMUL),
            Pair.of(InsnStencils.lmul(), Opcodes.LMUL),
            Pair.of(InsnStencils.fmul(), Opcodes.FMUL),
            Pair.of(InsnStencils.dmul(), Opcodes.DMUL),
            Pair.of(InsnStencils.idiv(), Opcodes.IDIV),
            Pair.of(InsnStencils.ldiv(), Opcodes.LDIV),
            Pair.of(InsnStencils.fdiv(), Opcodes.FDIV),
            Pair.of(InsnStencils.ddiv(), Opcodes.DDIV),
            Pair.of(InsnStencils.irem(), Opcodes.IREM),
            Pair.of(InsnStencils.lrem(), Opcodes.LREM),
            Pair.of(InsnStencils.frem(), Opcodes.FREM),
            Pair.of(InsnStencils.drem(), Opcodes.DREM),
            Pair.of(InsnStencils.ineg(), Opcodes.INEG),
            Pair.of(InsnStencils.lneg(), Opcodes.LNEG),
            Pair.of(InsnStencils.fneg(), Opcodes.FNEG),
            Pair.of(InsnStencils.dneg(), Opcodes.DNEG),
            Pair.of(InsnStencils.ishl(), Opcodes.ISHL),
            Pair.of(InsnStencils.lshl(), Opcodes.LSHL),
            Pair.of(InsnStencils.ishr(), Opcodes.ISHR),
            Pair.of(InsnStencils.lshr(), Opcodes.LSHR),
            Pair.of(InsnStencils.iushr(), Opcodes.IUSHR),
            Pair.of(InsnStencils.lushr(), Opcodes.LUSHR),
            Pair.of(InsnStencils.iand(), Opcodes.IAND),
            Pair.of(InsnStencils.land(), Opcodes.LAND),
            Pair.of(InsnStencils.ior(), Opcodes.IOR),
            Pair.of(InsnStencils.lor(), Opcodes.LOR),
            Pair.of(InsnStencils.ixor(), Opcodes.IXOR),
            Pair.of(InsnStencils.lxor(), Opcodes.LXOR),
            Pair.of(InsnStencils.i2l(), Opcodes.I2L),
            Pair.of(InsnStencils.i2f(), Opcodes.I2F),
            Pair.of(InsnStencils.i2d(), Opcodes.I2D),
            Pair.of(InsnStencils.l2i(), Opcodes.L2I),
            Pair.of(InsnStencils.l2f(), Opcodes.L2F),
            Pair.of(InsnStencils.l2d(), Opcodes.L2D),
            Pair.of(InsnStencils.f2i(), Opcodes.F2I),
            Pair.of(InsnStencils.f2l(), Opcodes.F2L),
            Pair.of(InsnStencils.f2d(), Opcodes.F2D),
            Pair.of(InsnStencils.d2i(), Opcodes.D2I),
            Pair.of(InsnStencils.d2l(), Opcodes.D2L),
            Pair.of(InsnStencils.d2f(), Opcodes.D2F),
            Pair.of(InsnStencils.i2b(), Opcodes.I2B),
            Pair.of(InsnStencils.i2c(), Opcodes.I2C),
            Pair.of(InsnStencils.i2s(), Opcodes.I2S),
            Pair.of(InsnStencils.lcmp(), Opcodes.LCMP),
            Pair.of(InsnStencils.fcmpl(), Opcodes.FCMPL),
            Pair.of(InsnStencils.fcmpg(), Opcodes.FCMPG),
            Pair.of(InsnStencils.dcmpl(), Opcodes.DCMPL),
            Pair.of(InsnStencils.dcmpg(), Opcodes.DCMPG),
            Pair.of(InsnStencils.ireturn(), Opcodes.IRETURN),
            Pair.of(InsnStencils.lreturn(), Opcodes.LRETURN),
            Pair.of(InsnStencils.freturn(), Opcodes.FRETURN),
            Pair.of(InsnStencils.dreturn(), Opcodes.DRETURN),
            Pair.of(InsnStencils.areturn(), Opcodes.ARETURN),
            Pair.of(InsnStencils.return_(), Opcodes.RETURN),
            Pair.of(InsnStencils.arraylength(), Opcodes.ARRAYLENGTH),
            Pair.of(InsnStencils.athrow(), Opcodes.ATHROW),
            Pair.of(InsnStencils.monitorenter(), Opcodes.MONITORENTER),
            Pair.of(InsnStencils.monitorexit(), Opcodes.MONITOREXIT)
    );

    for (Pair<InsnStencil, Integer> pair : stencils) {
      AbstractInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(InsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      InsnNode insnNode = (InsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
    }
  }

  // IntInsnStencil
  @Test
  void test_1() throws StencilEvaluationException {
    List<Pair<IntInsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.bipush(1), Opcodes.BIPUSH),
            Pair.of(InsnStencils.sipush(1), Opcodes.SIPUSH),
            Pair.of(InsnStencils.newarray(1), Opcodes.NEWARRAY)
    );

    for (Pair<IntInsnStencil, Integer> pair : stencils) {
      AbstractInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(IntInsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      IntInsnNode insnNode = (IntInsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
      Assertions.assertEquals(1, insnNode.operand);
    }
  }

  // VarInsnStencil
  @Test
  void test_2() throws StencilEvaluationException {
    List<Pair<VarInsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.iload(1), Opcodes.ILOAD),
            Pair.of(InsnStencils.lload(1), Opcodes.LLOAD),
            Pair.of(InsnStencils.fload(1), Opcodes.FLOAD),
            Pair.of(InsnStencils.dload(1), Opcodes.DLOAD),
            Pair.of(InsnStencils.aload(1), Opcodes.ALOAD),
            Pair.of(InsnStencils.istore(1), Opcodes.ISTORE),
            Pair.of(InsnStencils.lstore(1), Opcodes.LSTORE),
            Pair.of(InsnStencils.fstore(1), Opcodes.FSTORE),
            Pair.of(InsnStencils.dstore(1), Opcodes.DSTORE),
            Pair.of(InsnStencils.astore(1), Opcodes.ASTORE),
            Pair.of(InsnStencils.ret(1), Opcodes.RET)
    );

    for (Pair<VarInsnStencil, Integer> pair : stencils) {
      AbstractInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(VarInsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      VarInsnNode insnNode = (VarInsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
      Assertions.assertEquals(1, insnNode.var);
    }
  }

  // TypeInsnStencil
  @Test
  void test_3() throws StencilEvaluationException {
    List<Pair<TypeInsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.new_("A"), Opcodes.NEW),
            Pair.of(InsnStencils.anewarray("A"), Opcodes.ANEWARRAY),
            Pair.of(InsnStencils.checkcast("A"), Opcodes.CHECKCAST),
            Pair.of(InsnStencils.instanceof_("A"), Opcodes.INSTANCEOF)

    );

    for (Pair<TypeInsnStencil, Integer> pair : stencils) {
      TypeInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(TypeInsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      TypeInsnNode insnNode = (TypeInsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
      Assertions.assertEquals("A", insnNode.desc);
    }
  }

  // FieldInsnStencil
  @Test
  void test_4() throws StencilEvaluationException {
    List<Pair<FieldInsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.getfield("A", "B", "C"), Opcodes.GETFIELD),
            Pair.of(InsnStencils.putfield("A", "B", "C"), Opcodes.PUTFIELD),
            Pair.of(InsnStencils.getstatic("A", "B", "C"), Opcodes.GETSTATIC),
            Pair.of(InsnStencils.putstatic("A", "B", "C"), Opcodes.PUTSTATIC)
    );

    for (Pair<FieldInsnStencil, Integer> pair : stencils) {
      FieldInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(FieldInsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      FieldInsnNode insnNode = (FieldInsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
      Assertions.assertEquals("A", insnNode.owner);
      Assertions.assertEquals("B", insnNode.name);
      Assertions.assertEquals("C", insnNode.desc);
    }
  }

  // MethodInsnStencil
  @Test
  void test_5() throws StencilEvaluationException {
    List<Pair<MethodInsnStencil, Integer>> stencils = ArrayListHelper.of(
            Pair.of(InsnStencils.invokestatic("A", "B", "C", false), Opcodes.INVOKESTATIC),
            Pair.of(InsnStencils.invokevirtual("A", "B", "C", false), Opcodes.INVOKEVIRTUAL),
            Pair.of(InsnStencils.invokespecial("A", "B", "C", false), Opcodes.INVOKESPECIAL),
            Pair.of(InsnStencils.invokeinterface("A", "B", "C", false), Opcodes.INVOKEINTERFACE)
    );

    for (Pair<MethodInsnStencil, Integer> pair : stencils) {
      MethodInsnStencil stencil = pair.first;
      Integer opcode = pair.second;
      Assertions.assertInstanceOf(MethodInsnNode.class, stencil.evaluate(new TestStencilRegistry()));
      MethodInsnNode insnNode = (MethodInsnNode) stencil.evaluate(new TestStencilRegistry());
      Assertions.assertEquals(opcode.intValue(), insnNode.getOpcode());
      Assertions.assertEquals("A", insnNode.owner);
      Assertions.assertEquals("B", insnNode.name);
      Assertions.assertEquals("C", insnNode.desc);
    }
  }
}
