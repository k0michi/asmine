package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.common.OpcodesHelper;
import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.analysis.FlowAnalyzer;
import com.koyomiji.asmine.analysis.FlowAnalyzerThread;
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
    Stack<FlowAnalyzerThread> stack = new Stack<>();
    Set<AbstractInsnNode> visited = new HashSet<>();
    Map<FrameNode, FlowAnalyzerThread> diffFrames = new HashMap<>();
    FlowAnalyzer analyzer = new FlowAnalyzer(className, this);

    for (AbstractInsnNode insn : instructions) {
      if (insn instanceof FrameNode) {
        FrameNode frameNode = (FrameNode) insn;
        List<Object> local = new ArrayList<>();

        for (int i = 0; i < maxLocals; i++) {
          local.add(OpcodesHelper.AUTO);
        }

        List<Object> stackItems = new ArrayList<>();
        FlowAnalyzerThread diffFrame = new FlowAnalyzerThread(insn, local, stackItems);
        diffFrames.put(frameNode, diffFrame);
      }
    }

    stack.addAll(analyzer.getAllEntryThreads());

    while (!stack.isEmpty()) {
      FlowAnalyzerThread thread = stack.pop();
      AbstractInsnNode insn = thread.getCurrentInsn();

      FrameNode frameNode = AbstractInsnNodeHelper.getFrame(insn);

      if (frameNode != null) {
        FlowAnalyzerThread diffFrame = diffFrames.get(frameNode);
        List<Object> actualLocals = FrameHelper.toPhysicalForm(frameNode.local);
        List<Object> actualStack = FrameHelper.toPhysicalForm(frameNode.stack);

        for (int i = 0; i < Math.max(actualLocals.size(), thread.getLocalSize()); i++) {
          Object actualLocal = i < actualLocals.size() ? actualLocals.get(i) : Opcodes.TOP;
          Object inferredLocal = thread.getLocal(i);

          if (!Objects.equals(actualLocal, inferredLocal)) {
            diffFrame.setLocal(i, actualLocal);
          }
        }

        thread.setLocals(actualLocals);

        for (int i = 0; i < Math.max(actualStack.size(), thread.getStackSize()); i++) {
          Object actualStackItem = i < actualStack.size() ? actualStack.get(i) : Opcodes.TOP;
          Object inferredStackItem = thread.getStack(i);

          if (!Objects.equals(actualStackItem, inferredStackItem)) {
            diffFrame.setStack(i, actualStackItem);
          }
        }

        thread.setStack(actualStack);
      }

      if (visited.contains(insn)) {
        continue;
      }

      visited.add(insn);
      stack.addAll(analyzer.step(thread));
    }

    for (Map.Entry<FrameNode, FlowAnalyzerThread> entry : diffFrames.entrySet()) {
      instructions.set(entry.getKey(), entry.getValue().toFrameNode());
    }
  }
}
