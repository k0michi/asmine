package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.analysis.FlowAnalyzer;
import com.koyomiji.asmine.analysis.FlowAnalyzerThread;
import com.koyomiji.asmine.common.OpcodesHelper;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;

public class NormalizedInsnList extends InsnList {
  private String className;
  private NormalizedMethodNode methodNode;

  public NormalizedInsnList(String className, NormalizedMethodNode methodNode) {
    this.className = className;
    this.methodNode = methodNode;
  }

  @Override
  public void accept(MethodVisitor methodVisitor) {
    Map<FrameNode, FrameNode> absFrames = absolutizeFrames();
    AbstractInsnNode currentInsn = getFirst();
    while (currentInsn != null) {
      if (absFrames.containsKey(currentInsn)){
        absFrames.get(currentInsn).accept(methodVisitor);
      } else {
        currentInsn.accept(methodVisitor);
      }

      currentInsn = currentInsn.getNext();
    }
  }

  private Map<FrameNode, FrameNode> absolutizeFrames() {
    Stack<FlowAnalyzerThread> stack = new Stack<>();
    Set<AbstractInsnNode> visited = new HashSet<>();
    Map<FrameNode, FrameNode> absFrames = new HashMap<>();
    FlowAnalyzer analyzer = new FlowAnalyzer(className, methodNode);
    stack.addAll(analyzer.getAllEntryThreads());

    while (!stack.isEmpty()) {
      FlowAnalyzerThread thread = stack.pop();
      AbstractInsnNode insn = thread.getCurrentInsn();

      FrameNode frameNode = AbstractInsnNodeHelper.getFrame(insn);

      if (frameNode != null) {
        List<Object> locals = FrameHelper.toPhysicalForm(frameNode.local);
        List<Object> stackItems = FrameHelper.toPhysicalForm(frameNode.stack);

        for (int i = 0; i < locals.size(); i++) {
          if (locals.get(i) != OpcodesHelper.AUTO) {
            thread.setLocal(i, locals.get(i));
          }
        }

        for (int i = 0; i < stackItems.size(); i++) {
          if (stackItems.get(i) != OpcodesHelper.AUTO) {
            thread.setStack(i, stackItems.get(i));
          }
        }

        thread.compactLocals(Opcodes.TOP);
        absFrames.put(frameNode, thread.toFrameNode());
      }

      if (visited.contains(insn)) {
        continue;
      }

      visited.add(insn);
      stack.addAll(analyzer.step(thread));
    }

    return absFrames;
  }
}
