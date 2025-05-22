package com.koyomiji.asmine.common;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

public class InsnInserter {
  private final InsnList insnList;

  public InsnInserter(InsnList insnList) {
    this.insnList = insnList;
  }

  public InsnInserter(MethodNode methodNode) {
    this(methodNode.instructions);
  }

  public InsnInserter addInsn(int opcode) {
    insnList.add(new InsnNode(opcode));
    return this;
  }

  public InsnInserter addIntInsn(int opcode, int operand) {
    insnList.add(new IntInsnNode(opcode, operand));
    return this;
  }

  public InsnInserter addVarInsn(int opcode, int var) {
    insnList.add(new VarInsnNode(opcode, var));
    return this;
  }

  public InsnInserter addTypeInsn(int opcode, String type) {
    insnList.add(new TypeInsnNode(opcode, type));
    return this;
  }

  public InsnInserter addFieldInsn(int opcode, String owner, String name, String desc) {
    insnList.add(new FieldInsnNode(opcode, owner, name, desc));
    return this;
  }

  public InsnInserter addMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    insnList.add(new MethodInsnNode(opcode, owner, name, desc, itf));
    return this;
  }

  public InsnInserter addInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
    insnList.add(new InvokeDynamicInsnNode(name, desc, bsm, bsmArgs));
    return this;
  }

  public InsnInserter addJumpInsn(int opcode, Label label) {
    insnList.add(new JumpInsnNode(opcode, LabelHelper.getLabelNode(label)));
    return this;
  }

  public InsnInserter addLabel(Label label) {
    insnList.add(LabelHelper.getLabelNode(label));
    return this;
  }

  public InsnInserter addLdcInsn(Object cst) {
    insnList.add(new LdcInsnNode(cst));
    return this;
  }

  public InsnInserter addIincInsn(int var, int increment) {
    insnList.add(new IincInsnNode(var, increment));
    return this;
  }

  public InsnInserter addTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    insnList.add(new TableSwitchInsnNode(min, max, LabelHelper.getLabelNode(dflt), LabelHelper.toLabelNodes(labels)));
    return this;
  }

  public InsnInserter addLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    insnList.add(new LookupSwitchInsnNode(LabelHelper.getLabelNode(dflt), keys, LabelHelper.toLabelNodes(labels)));
    return this;
  }

  public InsnInserter addMultiANewArrayInsn(String desc, int dims) {
    insnList.add(new MultiANewArrayInsnNode(desc, dims));
    return this;
  }

  public InsnInserter addFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
    insnList.add(new FrameNode(type, numLocal, local, numStack, stack));
    return this;
  }

  public InsnInserter addLineNumber(int line, Label start) {
    insnList.add(new LineNumberNode(line, LabelHelper.getLabelNode(start)));
    return this;
  }
}
