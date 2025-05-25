package com.koyomiji.asmine.common;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class Insns {
  public static InsnNode insn(int opcode) {
    return new InsnNode(opcode);
  }

  public static IntInsnNode intInsn(int opcode, int operand) {
    return new IntInsnNode(opcode, operand);
  }

  public static VarInsnNode varInsn(int opcode, int varIndex) {
    return new VarInsnNode(opcode, varIndex);
  }

  public static TypeInsnNode typeInsn(int opcode, String type) {
    return new TypeInsnNode(opcode, type);
  }

  public static FieldInsnNode fieldInsn(int opcode, String owner, String name, String desc) {
    return new FieldInsnNode(opcode, owner, name, desc);
  }

  public static MethodInsnNode methodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    return new MethodInsnNode(opcode, owner, name, desc, itf);
  }

  public static InvokeDynamicInsnNode invokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    return new InvokeDynamicInsnNode(name, desc, bsm, bsmArgs);
  }

  public static JumpInsnNode jumpInsn(int opcode, LabelNode label) {
    return new JumpInsnNode(opcode, label);
  }

  public static LabelNode label() {
    return new LabelNode();
  }

  public static LdcInsnNode ldcInsn(Object cst) {
    return new LdcInsnNode(cst);
  }

  public static IincInsnNode iincInsn(int varIndex, int incr) {
    return new IincInsnNode(varIndex, incr);
  }

  public static TableSwitchInsnNode tableSwitchInsn(int min, int max, LabelNode dflt, LabelNode... labels) {
    return new TableSwitchInsnNode(min, max, dflt, labels);
  }

  public static LookupSwitchInsnNode lookupSwitchInsn(LabelNode dflt, int[] keys, LabelNode[] labels) {
    return new LookupSwitchInsnNode(dflt, keys, labels);
  }

  public static MultiANewArrayInsnNode multiANewArrayInsn(String desc, int dims) {
    return new MultiANewArrayInsnNode(desc, dims);
  }

  public static FrameNode frame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
    return new FrameNode(type, numLocal, local, numStack, stack);
  }

  public static LineNumberNode lineNumber(int line, LabelNode start) {
    return new LineNumberNode(line, start);
  }

  public static InsnNode nop() {
    return insn(Opcodes.NOP);
  }

  public static InsnNode aconst_null() {
    return insn(Opcodes.ACONST_NULL);
  }

  public static InsnNode iconst_m1() {
    return insn(Opcodes.ICONST_M1);
  }

  public static InsnNode iconst_0() {
    return insn(Opcodes.ICONST_0);
  }

  public static InsnNode iconst_1() {
    return insn(Opcodes.ICONST_1);
  }

  public static InsnNode iconst_2() {
    return insn(Opcodes.ICONST_2);
  }

  public static InsnNode iconst_3() {
    return insn(Opcodes.ICONST_3);
  }

  public static InsnNode iconst_4() {
    return insn(Opcodes.ICONST_4);
  }

  public static InsnNode iconst_5() {
    return insn(Opcodes.ICONST_5);
  }

  public static InsnNode lconst_0() {
    return insn(Opcodes.LCONST_0);
  }

  public static InsnNode lconst_1() {
    return insn(Opcodes.LCONST_1);
  }

  public static InsnNode fconst_0() {
    return insn(Opcodes.FCONST_0);
  }

  public static InsnNode fconst_1() {
    return insn(Opcodes.FCONST_1);
  }

  public static InsnNode fconst_2() {
    return insn(Opcodes.FCONST_2);
  }

  public static InsnNode dconst_0() {
    return insn(Opcodes.DCONST_0);
  }

  public static InsnNode dconst_1() {
    return insn(Opcodes.DCONST_1);
  }

  public static IntInsnNode bipush(int operand) {
    return intInsn(Opcodes.BIPUSH, operand);
  }

  public static IntInsnNode sipush(int operand) {
    return intInsn(Opcodes.SIPUSH, operand);
  }

  public static LdcInsnNode ldc(Object cst) {
    return ldcInsn(cst);
  }

  public static VarInsnNode iload(int varIndex) {
    return varInsn(Opcodes.ILOAD, varIndex);
  }

  public static VarInsnNode lload(int varIndex) {
    return varInsn(Opcodes.LLOAD, varIndex);
  }

  public static VarInsnNode fload(int varIndex) {
    return varInsn(Opcodes.FLOAD, varIndex);
  }

  public static VarInsnNode dload(int varIndex) {
    return varInsn(Opcodes.DLOAD, varIndex);
  }

  public static VarInsnNode aload(int varIndex) {
    return varInsn(Opcodes.ALOAD, varIndex);
  }

  public static InsnNode iaload() {
    return insn(Opcodes.IALOAD);
  }

  public static InsnNode laload() {
    return insn(Opcodes.LALOAD);
  }

  public static InsnNode faload() {
    return insn(Opcodes.FALOAD);
  }

  public static InsnNode daload() {
    return insn(Opcodes.DALOAD);
  }

  public static InsnNode aaload() {
    return insn(Opcodes.AALOAD);
  }

  public static InsnNode baload() {
    return insn(Opcodes.BALOAD);
  }

  public static InsnNode caload() {
    return insn(Opcodes.CALOAD);
  }

  public static InsnNode saload() {
    return insn(Opcodes.SALOAD);
  }

  public static VarInsnNode istore(int varIndex) {
    return varInsn(Opcodes.ISTORE, varIndex);
  }

  public static VarInsnNode lstore(int varIndex) {
    return varInsn(Opcodes.LSTORE, varIndex);
  }

  public static VarInsnNode fstore(int varIndex) {
    return varInsn(Opcodes.FSTORE, varIndex);
  }

  public static VarInsnNode dstore(int varIndex) {
    return varInsn(Opcodes.DSTORE, varIndex);
  }

  public static VarInsnNode astore(int varIndex) {
    return varInsn(Opcodes.ASTORE, varIndex);
  }

  public static InsnNode iastore() {
    return insn(Opcodes.IASTORE);
  }

  public static InsnNode lastore() {
    return insn(Opcodes.LASTORE);
  }

  public static InsnNode fastore() {
    return insn(Opcodes.FASTORE);
  }

  public static InsnNode dastore() {
    return insn(Opcodes.DASTORE);
  }

  public static InsnNode aastore() {
    return insn(Opcodes.AASTORE);
  }

  public static InsnNode bastore() {
    return insn(Opcodes.BASTORE);
  }

  public static InsnNode castore() {
    return insn(Opcodes.CASTORE);
  }

  public static InsnNode sastore() {
    return insn(Opcodes.SASTORE);
  }

  public static InsnNode pop() {
    return insn(Opcodes.POP);
  }

  public static InsnNode pop2() {
    return insn(Opcodes.POP2);
  }

  public static InsnNode dup() {
    return insn(Opcodes.DUP);
  }

  public static InsnNode dup_x1() {
    return insn(Opcodes.DUP_X1);
  }

  public static InsnNode dup_x2() {
    return insn(Opcodes.DUP_X2);
  }

  public static InsnNode dup2() {
    return insn(Opcodes.DUP2);
  }

  public static InsnNode dup2_x1() {
    return insn(Opcodes.DUP2_X1);
  }

  public static InsnNode dup2_x2() {
    return insn(Opcodes.DUP2_X2);
  }

  public static InsnNode swap() {
    return insn(Opcodes.SWAP);
  }

  public static InsnNode iadd() {
    return insn(Opcodes.IADD);
  }

  public static InsnNode ladd() {
    return insn(Opcodes.LADD);
  }

  public static InsnNode fadd() {
    return insn(Opcodes.FADD);
  }

  public static InsnNode dadd() {
    return insn(Opcodes.DADD);
  }

  public static InsnNode isub() {
    return insn(Opcodes.ISUB);
  }

  public static InsnNode lsub() {
    return insn(Opcodes.LSUB);
  }

  public static InsnNode fsub() {
    return insn(Opcodes.FSUB);
  }

  public static InsnNode dsub() {
    return insn(Opcodes.DSUB);
  }

  public static InsnNode imul() {
    return insn(Opcodes.IMUL);
  }

  public static InsnNode lmul() {
    return insn(Opcodes.LMUL);
  }

  public static InsnNode fmul() {
    return insn(Opcodes.FMUL);
  }

  public static InsnNode dmul() {
    return insn(Opcodes.DMUL);
  }

  public static InsnNode idiv() {
    return insn(Opcodes.IDIV);
  }

  public static InsnNode ldiv() {
    return insn(Opcodes.LDIV);
  }

  public static InsnNode fdiv() {
    return insn(Opcodes.FDIV);
  }

  public static InsnNode ddiv() {
    return insn(Opcodes.DDIV);
  }

  public static InsnNode irem() {
    return insn(Opcodes.IREM);
  }

  public static InsnNode lrem() {
    return insn(Opcodes.LREM);
  }

  public static InsnNode frem() {
    return insn(Opcodes.FREM);
  }

  public static InsnNode drem() {
    return insn(Opcodes.DREM);
  }

  public static InsnNode ineg() {
    return insn(Opcodes.INEG);
  }

  public static InsnNode lneg() {
    return insn(Opcodes.LNEG);
  }

  public static InsnNode fneg() {
    return insn(Opcodes.FNEG);
  }

  public static InsnNode dneg() {
    return insn(Opcodes.DNEG);
  }

  public static InsnNode ishl() {
    return insn(Opcodes.ISHL);
  }

  public static InsnNode lshl() {
    return insn(Opcodes.LSHL);
  }

  public static InsnNode ishr() {
    return insn(Opcodes.ISHR);
  }

  public static InsnNode lshr() {
    return insn(Opcodes.LSHR);
  }

  public static InsnNode iushr() {
    return insn(Opcodes.IUSHR);
  }

  public static InsnNode lushr() {
    return insn(Opcodes.LUSHR);
  }

  public static InsnNode iand() {
    return insn(Opcodes.IAND);
  }

  public static InsnNode land() {
    return insn(Opcodes.LAND);
  }

  public static InsnNode ior() {
    return insn(Opcodes.IOR);
  }

  public static InsnNode lor() {
    return insn(Opcodes.LOR);
  }

  public static InsnNode ixor() {
    return insn(Opcodes.IXOR);
  }

  public static InsnNode lxor() {
    return insn(Opcodes.LXOR);
  }

  public static IincInsnNode iinc(int varIndex, int incr) {
    return iincInsn(varIndex, incr);
  }

  public static InsnNode i2l() {
    return insn(Opcodes.I2L);
  }

  public static InsnNode i2f() {
    return insn(Opcodes.I2F);
  }

  public static InsnNode i2d() {
    return insn(Opcodes.I2D);
  }

  public static InsnNode l2i() {
    return insn(Opcodes.L2I);
  }

  public static InsnNode l2f() {
    return insn(Opcodes.L2F);
  }

  public static InsnNode l2d() {
    return insn(Opcodes.L2D);
  }

  public static InsnNode f2i() {
    return insn(Opcodes.F2I);
  }

  public static InsnNode f2l() {
    return insn(Opcodes.F2L);
  }

  public static InsnNode f2d() {
    return insn(Opcodes.F2D);
  }

  public static InsnNode d2i() {
    return insn(Opcodes.D2I);
  }

  public static InsnNode d2l() {
    return insn(Opcodes.D2L);
  }

  public static InsnNode d2f() {
    return insn(Opcodes.D2F);
  }

  public static InsnNode i2b() {
    return insn(Opcodes.I2B);
  }

  public static InsnNode i2c() {
    return insn(Opcodes.I2C);
  }

  public static InsnNode i2s() {
    return insn(Opcodes.I2S);
  }

  public static InsnNode lcmp() {
    return insn(Opcodes.LCMP);
  }

  public static InsnNode fcmpl() {
    return insn(Opcodes.FCMPL);
  }

  public static InsnNode fcmpg() {
    return insn(Opcodes.FCMPG);
  }

  public static InsnNode dcmpl() {
    return insn(Opcodes.DCMPL);
  }

  public static InsnNode dcmpg() {
    return insn(Opcodes.DCMPG);
  }

  public static JumpInsnNode ifeq(LabelNode label) {
    return jumpInsn(Opcodes.IFEQ, label);
  }

  public static JumpInsnNode ifne(LabelNode label) {
    return jumpInsn(Opcodes.IFNE, label);
  }

  public static JumpInsnNode iflt(LabelNode label) {
    return jumpInsn(Opcodes.IFLT, label);
  }

  public static JumpInsnNode ifge(LabelNode label) {
    return jumpInsn(Opcodes.IFGE, label);
  }

  public static JumpInsnNode ifgt(LabelNode label) {
    return jumpInsn(Opcodes.IFGT, label);
  }

  public static JumpInsnNode ifle(LabelNode label) {
    return jumpInsn(Opcodes.IFLE, label);
  }

  public static JumpInsnNode if_icmpeq(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPEQ, label);
  }

  public static JumpInsnNode if_icmpne(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPNE, label);
  }

  public static JumpInsnNode if_icmplt(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPLT, label);
  }

  public static JumpInsnNode if_icmpge(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPGE, label);
  }

  public static JumpInsnNode if_icmpgt(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPGT, label);
  }

  public static JumpInsnNode if_icmple(LabelNode label) {
    return jumpInsn(Opcodes.IF_ICMPLE, label);
  }

  public static JumpInsnNode if_acmpeq(LabelNode label) {
    return jumpInsn(Opcodes.IF_ACMPEQ, label);
  }

  public static JumpInsnNode if_acmpne(LabelNode label) {
    return jumpInsn(Opcodes.IF_ACMPNE, label);
  }

  public static JumpInsnNode goto_(LabelNode label) {
    return jumpInsn(Opcodes.GOTO, label);
  }

  public static JumpInsnNode jsr(LabelNode label) {
    return jumpInsn(Opcodes.JSR, label);
  }

  public static VarInsnNode ret(int varIndex) {
    return varInsn(Opcodes.RET, varIndex);
  }

  public static TableSwitchInsnNode tableSwitch(int min, int max, LabelNode dflt, LabelNode... labels) {
    return tableSwitchInsn(min, max, dflt, labels);
  }

  public static LookupSwitchInsnNode lookupSwitch(LabelNode dflt, int[] keys, LabelNode[] labels) {
    return lookupSwitchInsn(dflt, keys, labels);
  }

  public static InsnNode ireturn() {
    return insn(Opcodes.IRETURN);
  }

  public static InsnNode lreturn() {
    return insn(Opcodes.LRETURN);
  }

  public static InsnNode freturn() {
    return insn(Opcodes.FRETURN);
  }

  public static InsnNode dreturn() {
    return insn(Opcodes.DRETURN);
  }

  public static InsnNode areturn() {
    return insn(Opcodes.ARETURN);
  }

  public static InsnNode return_() {
    return insn(Opcodes.RETURN);
  }

  public static FieldInsnNode getstatic(String owner, String name, String desc) {
    return fieldInsn(Opcodes.GETSTATIC, owner, name, desc);
  }

  public static FieldInsnNode putstatic(String owner, String name, String desc) {
    return fieldInsn(Opcodes.PUTSTATIC, owner, name, desc);
  }

  public static FieldInsnNode getfield(String owner, String name, String desc) {
    return fieldInsn(Opcodes.GETFIELD, owner, name, desc);
  }

  public static FieldInsnNode putfield(String owner, String name, String desc) {
    return fieldInsn(Opcodes.PUTFIELD, owner, name, desc);
  }

  public static MethodInsnNode invokevirtual(String owner, String name, String desc, boolean itf) {
    return methodInsn(Opcodes.INVOKEVIRTUAL, owner, name, desc, itf);
  }

  public static MethodInsnNode invokespecial(String owner, String name, String desc, boolean itf) {
    return methodInsn(Opcodes.INVOKESPECIAL, owner, name, desc, itf);
  }

  public static MethodInsnNode invokestatic(String owner, String name, String desc, boolean itf) {
    return methodInsn(Opcodes.INVOKESTATIC, owner, name, desc, itf);
  }

  public static MethodInsnNode invokeinterface(String owner, String name, String desc, boolean itf) {
    return methodInsn(Opcodes.INVOKEINTERFACE, owner, name, desc, itf);
  }

  public static InvokeDynamicInsnNode invokedynamic(String name, String desc, Handle bsm, Object... bsmArgs) {
    return invokeDynamicInsn(name, desc, bsm, bsmArgs);
  }

  public static TypeInsnNode new_(String type) {
    return typeInsn(Opcodes.NEW, type);
  }

  public static IntInsnNode newarray(int type) {
    return intInsn(Opcodes.NEWARRAY, type);
  }

  public static TypeInsnNode anewarray(String type) {
    return typeInsn(Opcodes.ANEWARRAY, type);
  }

  public static InsnNode arraylength() {
    return insn(Opcodes.ARRAYLENGTH);
  }

  public static InsnNode athrow() {
    return insn(Opcodes.ATHROW);
  }

  public static TypeInsnNode checkcast(String type) {
    return typeInsn(Opcodes.CHECKCAST, type);
  }

  public static TypeInsnNode instanceof_(String type) {
    return typeInsn(Opcodes.INSTANCEOF, type);
  }

  public static InsnNode monitorenter() {
    return insn(Opcodes.MONITORENTER);
  }

  public static InsnNode monitorexit() {
    return insn(Opcodes.MONITOREXIT);
  }

  public static MultiANewArrayInsnNode multianewarray(String desc, int dims) {
    return multiANewArrayInsn(desc, dims);
  }

  public static JumpInsnNode ifnull(LabelNode label) {
    return jumpInsn(Opcodes.IFNULL, label);
  }

  public static JumpInsnNode ifnonnull(LabelNode label) {
    return jumpInsn(Opcodes.IFNONNULL, label);
  }
}
