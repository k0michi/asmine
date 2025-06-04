package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.frame.ControlFlowAnalyzer;
import com.koyomiji.asmine.frame.Frame;
import com.koyomiji.asmine.frame.FrameManipInsnNode;
import com.koyomiji.asmine.tuple.Pair;
import com.koyomiji.asmine.tuple.Triplet;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.*;

public class NormalizedMethodNode extends MethodNode {
  private String className;
  private Label lastLabel;

  public NormalizedMethodNode(String className) {
    super(OpcodesCompat.ASM_LATEST);
    this.className = className;
    this.instructions = new NormalizedInsnList(className, this);
  }

  public NormalizedMethodNode(int api, String className) {
    super(api);
    this.className = className;
    this.instructions = new NormalizedInsnList(className, this);
  }

  public NormalizedMethodNode(String className, int access, String name, String descriptor, String signature, String[] exceptions) {
    super(OpcodesCompat.ASM_LATEST, access, name, descriptor, signature, exceptions);
    this.className = className;
    this.instructions = new NormalizedInsnList(className, this);
  }

  public NormalizedMethodNode(int api, String className, int access, String name, String descriptor, String signature, String[] exceptions) {
    super(api, access, name, descriptor, signature, exceptions);
    this.className = className;
    this.instructions = new NormalizedInsnList(className, this);
  }

  private void visitLabelIfNeeded() {
    if (lastLabel == null) {
      lastLabel = new Label();
      visitLabel(lastLabel);
    }
  }

  private void setLastLabel(Label label) {
    lastLabel = label;
  }

  private void resetLastLabel() {
    lastLabel = null;
  }

  @Override
  public void visitInsn(int opcode) {
    visitLabelIfNeeded();
    super.visitInsn(opcode);
    resetLastLabel();
  }

  @Override
  public void visitIntInsn(int opcode, int operand) {
    visitLabelIfNeeded();
    super.visitIntInsn(opcode, operand);
    resetLastLabel();
  }

  @Override
  public void visitVarInsn(int opcode, int varIndex) {
    visitLabelIfNeeded();
    super.visitVarInsn(opcode, varIndex);
    resetLastLabel();
  }

  @Override
  public void visitTypeInsn(int opcode, String type) {
    visitLabelIfNeeded();
    super.visitTypeInsn(opcode, type);
    resetLastLabel();
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
    visitLabelIfNeeded();
    super.visitFieldInsn(opcode, owner, name, descriptor);
    resetLastLabel();
  }

  @Override
  public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
    visitLabelIfNeeded();
    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
    resetLastLabel();
  }

  @Override
  public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
    visitLabelIfNeeded();
    super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    resetLastLabel();
  }

  @Override
  public void visitJumpInsn(int opcode, Label label) {
    visitLabelIfNeeded();
    super.visitJumpInsn(opcode, label);
    resetLastLabel();
  }

  @Override
  public void visitLabel(Label label) {
    super.visitLabel(label);
    setLastLabel(label);
  }

  @Override
  public void visitLdcInsn(Object value) {
    visitLabelIfNeeded();
    super.visitLdcInsn(value);
    resetLastLabel();
  }

  @Override
  public void visitIincInsn(int varIndex, int increment) {
    visitLabelIfNeeded();
    super.visitIincInsn(varIndex, increment);
    resetLastLabel();
  }

  @Override
  public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    visitLabelIfNeeded();
    super.visitTableSwitchInsn(min, max, dflt, labels);
    resetLastLabel();
  }

  @Override
  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    visitLabelIfNeeded();
    super.visitLookupSwitchInsn(dflt, keys, labels);
    resetLastLabel();
  }

  @Override
  public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    visitLabelIfNeeded();
    super.visitMultiANewArrayInsn(descriptor, numDimensions);
    resetLastLabel();
  }

  @Override
  public void visitLineNumber(int line, Label start) {
    super.visitLineNumber(line, start);
  }

  @Override
  public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
    visitLabelIfNeeded();
    super.visitFrame(type, numLocal, local, numStack, stack);
  }

  @Override
  public void visitMaxs(int maxStack, int maxLocals) {
    visitLabelIfNeeded();
    super.visitMaxs(maxStack, maxLocals);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
    differentiateFrames();
  }

  private void differentiateFrames() {
    Stack<Triplet<AbstractInsnNode, LabelNode, Frame>> stack = new Stack<>();
    Set<AbstractInsnNode> visited = new HashSet<>();
    Map<FrameNode, Frame> diffFrames = new HashMap<>();
    ControlFlowAnalyzer analyzer = new ControlFlowAnalyzer(this);

    for (AbstractInsnNode insn : instructions) {
      if (insn instanceof FrameNode) {
        FrameNode frameNode = (FrameNode) insn;
        Object[] local = new Object[maxLocals];

        for (int i = 0; i < maxLocals; i++) {
          local[i] = Frame.AUTO;
        }

        Object[] stackItems = new Object[0];
        Frame diffFrame = new Frame(local, stackItems);
        diffFrames.put(frameNode, diffFrame);
      }
    }

    while (true) {
      AbstractInsnNode entry = null;

      for (AbstractInsnNode insn : instructions) {
        if (!visited.contains(insn)) {
          entry = insn;
          break;
        }
      }

      if (entry == null) {
        break;
      }

      if (entry == instructions.getFirst()) {
        stack.push(Triplet.of(entry, null, new Frame(className, this)));
      } else {
        stack.push(Triplet.of(entry, null, new Frame()));
      }

      while (!stack.isEmpty()) {
        Triplet<AbstractInsnNode, LabelNode, Frame> triplet = stack.pop();
        AbstractInsnNode insn = triplet.first;
        LabelNode labelNode = triplet.second;
        Frame inferred = triplet.third;

        if (insn == null || AbstractInsnNodeHelper.isReal(insn) && visited.contains(insn)) {
          continue;
        }

        visited.add(insn);

        if (insn instanceof FrameNode) {
          FrameNode frameNode = (FrameNode) insn;
          Frame diffFrame = diffFrames.get(frameNode);
          Frame actualFrame = new Frame(frameNode);

          for (int i = 0; i < Math.max(actualFrame.getStackRaw().size(), inferred.getStackRaw().size()); i++) {
            Object inferredStack = i < inferred.getStackRaw().size() ? inferred.getStackRaw().get(i) : Opcodes.TOP;
            Object actualStack = i < actualFrame.getStackRaw().size() ? actualFrame.getStackRaw().get(i) : Opcodes.TOP;

            if (!Objects.equals(actualStack, inferredStack)) {
              diffFrame.setStack(i, actualStack);
            }
          }

          for (int i = 0; i < Math.max(actualFrame.getLocalsRaw().size(), inferred.getLocalsRaw().size()); i++) {
            Object inferredLocal = i < inferred.getLocalsRaw().size() ? inferred.getLocalsRaw().get(i) : Opcodes.TOP;
            Object actualLocal = i < actualFrame.getLocalsRaw().size() ? actualFrame.getLocalsRaw().get(i) : Opcodes.TOP;

            if (!Objects.equals(actualLocal, inferredLocal)) {
              // FIXME
              diffFrame.getLocalsRaw().set(i, actualLocal);
            }
          }

          inferred = actualFrame;
        }

        if (AbstractInsnNodeHelper.isReal(insn)) {
          inferred.execute(labelNode, insn);
        }

        if (insn instanceof LabelNode) {
          labelNode = (LabelNode) insn;
        }

        for (AbstractInsnNode suc : analyzer.getSuccessors(insn)) {
          stack.push(Triplet.of(suc, labelNode, inferred.clone()));
        }

        for (Pair<AbstractInsnNode, String> suc : analyzer.getExceptionSuccessors(insn)) {
          Frame exceptionFrame = new Frame(new Object[]{suc.second}, new Object[0]);
          stack.push(Triplet.of(suc.first, labelNode, exceptionFrame));
        }
      }
    }

    for (Map.Entry<FrameNode, Frame> entry : diffFrames.entrySet()) {
      instructions.set(entry.getKey(), entry.getValue().toFrameNode());
    }
  }
}
