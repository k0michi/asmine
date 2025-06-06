package com.koyomiji.asmine.frame;

import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlFlowAnalyzer {
  private MethodNode methodNode;

  public ControlFlowAnalyzer(MethodNode methodNode) {
    this.methodNode = methodNode;
  }

  public List<AbstractInsnNode> getSuccessors(AbstractInsnNode insn) {
    List<AbstractInsnNode> results = new ArrayList<>();

    if (!AbstractInsnNodeHelper.isUnconditionalJump(insn)) {
      results.add(insn.getNext());
    }

    if (insn instanceof JumpInsnNode) {
      JumpInsnNode jumpInsn = (JumpInsnNode) insn;
      results.add(jumpInsn.label);
    }

    if (insn instanceof LookupSwitchInsnNode) {
      LookupSwitchInsnNode switchInsn = (LookupSwitchInsnNode) insn;

      results.addAll(switchInsn.labels);

      results.add(switchInsn.dflt);
    }

    if (insn instanceof TableSwitchInsnNode) {
      TableSwitchInsnNode switchInsn = (TableSwitchInsnNode) insn;

      results.addAll(switchInsn.labels);

      results.add(switchInsn.dflt);
    }

    return results;
  }

  private Map<LabelNode, Integer> getLabelIndices() {
    Map<LabelNode, Integer> labelIndices = new HashMap<>();

    for (int i = 0; i < methodNode.instructions.size(); i++) {
      AbstractInsnNode insn = methodNode.instructions.get(i);

      if (insn instanceof LabelNode) {
        labelIndices.put((LabelNode) insn, i);
      }
    }

    return labelIndices;
  }

  public List<Pair<AbstractInsnNode, String>> getExceptionSuccessors(AbstractInsnNode insn) {
    List<Pair<AbstractInsnNode, String>> results = new ArrayList<>();
    Map<LabelNode, Integer> labelIndices = getLabelIndices();

    for (TryCatchBlockNode tryCatch : methodNode.tryCatchBlocks) {
      int beginIndex = labelIndices.get(tryCatch.start);
      int endIndex = labelIndices.get(tryCatch.end);
      int insnIndex = methodNode.instructions.indexOf(insn);

      if (insnIndex >= beginIndex && insnIndex < endIndex) {
        results.add(Pair.of(tryCatch.handler, tryCatch.type));
      }
    }

    return results;
  }
}
