package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.compat.OpcodesCompat;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An alternative representation of MethodNode that is easier to modify
 */
public class ExternalizedMethodNode extends MethodNode {
  public List<ExternalizedFrameNode> frames = new ArrayList<>();
  public List<LineNumberNode> lineNumbers = new ArrayList<>();
  private final Map<LabelNode, LabelNode> labelMap = new HashMap<>();
  private LabelNode lastLabel;

  public ExternalizedMethodNode() {
    this(OpcodesCompat.ASM_LATEST);
  }

  public ExternalizedMethodNode(int api) {
    super(api);
    this.instructions = new ExternalizedInsnList(this);
  }

  public ExternalizedMethodNode(int access, String name, String desc, String signature, String[] exceptions) {
    this(OpcodesCompat.ASM_LATEST, access, name, desc, signature, exceptions);
  }

  public ExternalizedMethodNode(int api, int access, String name, String desc, String signature, String[] exceptions) {
    super(api, access, name, desc, signature, exceptions);
    this.instructions = new ExternalizedInsnList(this);
  }

  private LabelNode getLabel() {
    if (lastLabel != null) {
      return lastLabel;
    }

    visitLabel(new Label());
    return lastLabel;
  }

  private LabelNode resolveLabel(LabelNode label) {
    return labelMap.get(label);
  }

  private LabelNode resolveLabel(Label label) {
    return resolveLabel(getLabelNode(label));
  }

  @Override
  public void visitInsn(int opcode) {
    super.visitInsn(opcode);
    lastLabel = null;
  }

  @Override
  public void visitIntInsn(int opcode, int operand) {
    super.visitIntInsn(opcode, operand);
    lastLabel = null;
  }

  @Override
  public void visitVarInsn(int opcode, int varIndex) {
    super.visitVarInsn(opcode, varIndex);
    lastLabel = null;
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    super.visitTypeInsn(opcode, type);
    lastLabel = null;
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
    super.visitFieldInsn(opcode, owner, name, descriptor);
    lastLabel = null;
  }

  @Override
  public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
    lastLabel = null;
  }

  @Override
  public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
    super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    lastLabel = null;
  }

  @Override
  public void visitJumpInsn(int opcode, Label label) {
    super.visitJumpInsn(opcode, label);
    lastLabel = null;
  }

  @Override
  public void visitLabel(Label label) {
    if (lastLabel == null) {
      super.visitLabel(label);
      lastLabel = getLabelNode(label);
      labelMap.put(lastLabel, lastLabel);
    } else {
      labelMap.put(getLabelNode(label), lastLabel);
    }
  }

  @Override
  public void visitLdcInsn(Object value) {
    super.visitLdcInsn(value);
    lastLabel = null;
  }

  @Override
  public void visitIincInsn(int varIndex, int increment) {
    super.visitIincInsn(varIndex, increment);
    lastLabel = null;
  }

  @Override
  public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    super.visitTableSwitchInsn(min, max, dflt, labels);
    lastLabel = null;
  }

  @Override
  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    super.visitLookupSwitchInsn(dflt, keys, labels);
    lastLabel = null;
  }

  @Override
  public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    super.visitMultiANewArrayInsn(descriptor, numDimensions);
    lastLabel = null;
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    this.lineNumbers.add(new LineNumberNode(line, resolveLabel(start)));
  }

  @Override
  public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
    LabelNode label = getLabel();
    this.frames.add(new ExternalizedFrameNode(type, numLocal, local, numStack, stack, label));
  }
}
