package com.koyomiji.asmine.frame;

import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;

public class FrameComputer {
  private String className;
  private MethodNode methodNode;
  private Map<LabelNode, Integer> labelIndices = new HashMap<>();
  private Map<AbstractInsnNode, Set<AbstractInsnNode>> predecessors = new HashMap<>();
  private Map<AbstractInsnNode, Set<String>> predecessorExceptions = new HashMap<>();
  private AbstractInsnNode firstInsn;

  public FrameComputer(String className, MethodNode methodNode) {
    this.className = className;
    this.methodNode = methodNode;

    for (int i = 0; i < methodNode.instructions.size(); i++) {
      AbstractInsnNode insn = methodNode.instructions.get(i);

      if (insn instanceof LabelNode) {
        labelIndices.put((LabelNode) insn, i);
      }
    }

    firstInsn = skipPseudo(methodNode.instructions.getFirst());
  }

  private AbstractInsnNode skipPseudo(AbstractInsnNode insn) {
    while (insn != null && AbstractInsnNodeHelper.isPseudo(insn)) {
      insn = insn.getNext();
    }

    return insn;
  }

  private boolean isUnconditionalJump(AbstractInsnNode insn) {
    return insn.getOpcode() == Opcodes.ARETURN ||
            insn.getOpcode() == Opcodes.ATHROW ||
            insn.getOpcode() == Opcodes.DRETURN ||
            insn.getOpcode() == Opcodes.FRETURN ||
            insn.getOpcode() == Opcodes.GOTO ||
            insn.getOpcode() == Opcodes.IRETURN ||
            insn.getOpcode() == Opcodes.LOOKUPSWITCH ||
            insn.getOpcode() == Opcodes.LRETURN ||
            insn.getOpcode() == Opcodes.RETURN ||
            insn.getOpcode() == Opcodes.TABLESWITCH;
  }

  private List<AbstractInsnNode> getSuccessors(AbstractInsnNode insn) {
    List<AbstractInsnNode> results = new ArrayList<>();

    if (!isUnconditionalJump(insn)) {
      results.add(skipPseudo(insn.getNext()));
    }

    if (insn instanceof JumpInsnNode) {
      JumpInsnNode jumpInsn = (JumpInsnNode) insn;
      results.add(skipPseudo(jumpInsn.label));
    }

    if (insn instanceof LookupSwitchInsnNode) {
      LookupSwitchInsnNode switchInsn = (LookupSwitchInsnNode) insn;

      for (LabelNode label : switchInsn.labels) {
        results.add(skipPseudo(label));
      }

      results.add(skipPseudo(switchInsn.dflt));
    }

    if (insn instanceof TableSwitchInsnNode) {
      TableSwitchInsnNode switchInsn = (TableSwitchInsnNode) insn;

      for (LabelNode label : switchInsn.labels) {
        results.add(skipPseudo(label));
      }

      results.add(skipPseudo(switchInsn.dflt));
    }

    return results;
  }

  private List<Pair<AbstractInsnNode, String>> getExceptionSuccessors(AbstractInsnNode insn) {
    List<Pair<AbstractInsnNode, String>> results = new ArrayList<>();

    for (TryCatchBlockNode tryCatch : methodNode.tryCatchBlocks) {
      int beginIndex = labelIndices.get(tryCatch.start);
      int endIndex = labelIndices.get(tryCatch.end);
      int insnIndex = methodNode.instructions.indexOf(insn);

      if (insnIndex >= beginIndex && insnIndex < endIndex) {
        results.add(Pair.of(skipPseudo(tryCatch.handler), tryCatch.type));
      }
    }

    return results;
  }

  private Frame getInitialFrame() {
    return new Frame(className, methodNode);
  }

  public List<Frame> computeFrames() {
    Stack<AbstractInsnNode> stack = new Stack<>();
    Map<AbstractInsnNode, Frame> frames = new HashMap<>();
    stack.push(methodNode.instructions.getFirst());

    while (!stack.isEmpty()) {
      AbstractInsnNode insn = stack.pop();
      FrameNode existingFrameNode = null;
      List<FrameManipInsnNode> manips = new ArrayList<>();
      LabelNode labelNode = null;

      while (insn != null && AbstractInsnNodeHelper.isPseudo(insn)) {
        if (insn instanceof FrameNode) {
          existingFrameNode = (FrameNode) insn;
        }

        if (insn instanceof LabelNode) {
          labelNode = (LabelNode) insn;
        }

        if (insn instanceof FrameManipInsnNode) {
          manips.add((FrameManipInsnNode) insn);
        }

        insn = insn.getNext();
      }

      Frame newFrame = null;

      if (insn == firstInsn) {
        newFrame = getInitialFrame();
      }

      for (AbstractInsnNode pred : predecessors.getOrDefault(insn, new HashSet<>())) {
        Frame predFrame = frames.get(pred).clone();
        predFrame.execute(labelNode, pred);

        if (newFrame == null) {
          newFrame = predFrame.clone();
        } else {
          newFrame.mergeWith(predFrame);
        }
      }

      for (String e : predecessorExceptions.getOrDefault(insn, new HashSet<>())) {
        Frame frame = new Frame(new Object[]{e}, new Object[0]);

        if (newFrame == null) {
          newFrame = frame.clone();
        } else {
          newFrame.mergeWith(frame);
        }
      }

      if (existingFrameNode != null) {
        newFrame.overwriteWith(new Frame(existingFrameNode));
      }

      if (!Objects.equals(newFrame, frames.get(insn))) {
        List<AbstractInsnNode> successors = getSuccessors(insn);

        for (AbstractInsnNode successor : successors) {
          predecessors.putIfAbsent(successor, new HashSet<>());
          predecessors.get(successor).add(insn);
          stack.push(successor);
        }

        List<Pair<AbstractInsnNode, String>> exceptionSuccessors = getExceptionSuccessors(insn);

        for (Pair<AbstractInsnNode, String> exceptionSuccessor : exceptionSuccessors) {
          predecessorExceptions.putIfAbsent(exceptionSuccessor.first, new HashSet<>());
          predecessorExceptions.get(exceptionSuccessor.first).add(exceptionSuccessor.second);
          stack.push(exceptionSuccessor.first);
        }

        frames.put(insn, newFrame);
      }
    }

    List<Frame> result = new ArrayList<>();

    for (int i = 0; i < methodNode.instructions.size(); i++) {
      result.add(frames.get(methodNode.instructions.get(i)));
    }

    return result;
  }
}
