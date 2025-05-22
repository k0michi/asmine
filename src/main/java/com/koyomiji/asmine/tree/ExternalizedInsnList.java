package com.koyomiji.asmine.tree;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

public class ExternalizedInsnList extends InsnList {
  private ExternalizedMethodNode methodNode;

  public ExternalizedInsnList(ExternalizedMethodNode methodNode) {
    this.methodNode = methodNode;
  }

  @Override
  public void accept(MethodVisitor methodVisitor) {
    Map<Label, LineNumberNode> lineNumberNodes = new HashMap<>();

    for (LineNumberNode lineNumberNode : methodNode.lineNumbers) {
      lineNumberNodes.put(lineNumberNode.start.getLabel(), lineNumberNode);
    }

    Map<Label, ExternalizedFrameNode> frameNodes = new HashMap<>();

    for (ExternalizedFrameNode frameNode : methodNode.frames) {
      frameNodes.put(frameNode.start.getLabel(), frameNode);
    }

    AbstractInsnNode currentInsn = getFirst();
    while (currentInsn != null) {
      currentInsn.accept(methodVisitor);

      if (currentInsn instanceof LabelNode) {
        LabelNode labelNode = (LabelNode) currentInsn;
        Label label = labelNode.getLabel();

        if (lineNumberNodes.containsKey(label)) {
          methodVisitor.visitLineNumber(lineNumberNodes.get(label).line, label);
        }

        if (frameNodes.containsKey(label)) {
          methodVisitor.visitFrame(frameNodes.get(label).type, frameNodes.get(label).local.size(), frameNodes.get(label).local.toArray(), frameNodes.get(label).stack.size(), frameNodes.get(label).stack.toArray());
        }
      }

      currentInsn = currentInsn.getNext();
    }
  }
}
