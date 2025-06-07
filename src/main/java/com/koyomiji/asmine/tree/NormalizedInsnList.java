package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.frame.ControlFlowAnalyzer;
import com.koyomiji.asmine.frame.FlowAnalyzer;
import com.koyomiji.asmine.frame.FlowAnalyzerThread;
import com.koyomiji.asmine.frame.Frame;
import com.koyomiji.asmine.tuple.Pair;
import com.koyomiji.asmine.tuple.Triplet;
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
    Map<FrameNode, FrameNode> intFrames = integrateFrames();
    AbstractInsnNode currentInsn = getFirst();
    while (currentInsn != null) {
      if (intFrames.containsKey(currentInsn)){
        intFrames.get(currentInsn).accept(methodVisitor);
      } else {
        currentInsn.accept(methodVisitor);
      }

      currentInsn = currentInsn.getNext();
    }
  }

  private Map<FrameNode, FrameNode> integrateFrames() {
    Stack<FlowAnalyzerThread> stack = new Stack<>();
    Set<AbstractInsnNode> visited = new HashSet<>();
    Map<FrameNode, FrameNode> intFrames = new HashMap<>();
    FlowAnalyzer analyzer = new FlowAnalyzer(className, methodNode);
    stack.addAll(analyzer.getAllEntryThreads());

    while (!stack.isEmpty()) {
      FlowAnalyzerThread thread = stack.pop();
      AbstractInsnNode insn = thread.getCurrentInsn();

      if (AbstractInsnNodeHelper.isReal(insn) && visited.contains(insn)) {
        continue;
      }

      visited.add(insn);

      if (insn instanceof FrameNode) {
        FrameNode frameNode = (FrameNode) insn;
        List<Object> locals = FrameHelper.toPhysicalForm(frameNode.local);
        List<Object> stackItems = FrameHelper.toPhysicalForm(frameNode.stack);

        for (int i = 0; i < locals.size(); i++) {
          if (locals.get(i) != Frame.AUTO) {
            thread.setLocal(i, locals.get(i));
          }
        }

        for (int i = 0; i < stackItems.size(); i++) {
          if (stackItems.get(i) != Frame.AUTO) {
            thread.setStack(i, stackItems.get(i));
          }
        }

        thread.compactLocals(Opcodes.TOP);
        intFrames.put(frameNode, thread.toFrameNode());
      }

      stack.addAll(analyzer.step(thread));
    }

    return intFrames;
  }
}
