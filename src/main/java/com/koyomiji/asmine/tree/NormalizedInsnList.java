package com.koyomiji.asmine.tree;

import com.koyomiji.asmine.frame.ControlFlowAnalyzer;
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
    Stack<Triplet<AbstractInsnNode, LabelNode, Frame>> stack = new Stack<>();
    Set<AbstractInsnNode> visited = new HashSet<>();
    Map<FrameNode, FrameNode> intFrames = new HashMap<>();
    ControlFlowAnalyzer analyzer = new ControlFlowAnalyzer(methodNode);

    while (true) {
      AbstractInsnNode entry = null;

      for (AbstractInsnNode insn : this) {
        if (!visited.contains(insn)) {
          entry = insn;
          break;
        }
      }

      if (entry == null) {
        break;
      }

      if (entry == this.getFirst()) {
        stack.push(Triplet.of(entry, null, new Frame(className, methodNode)));
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
          Frame frameGuide = new Frame(frameNode);

          for (int i = 0; i < frameGuide.getLocalsSize(); i++) {
            if (frameGuide.getLocalsRaw().get(i) != Frame.AUTO) {
              inferred.setLocal(i, frameGuide.getLocalsRaw().get(i));
            }
          }

          for (int i = 0; i < frameGuide.getStackSize(); i++) {
            if (frameGuide.getStackRaw().get(i) != Frame.AUTO) {
              inferred.setStack(i, frameGuide.getStackRaw().get(i));
            }
          }

          intFrames.put(frameNode, inferred.toFrameNode());
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

    return intFrames;
  }
}
